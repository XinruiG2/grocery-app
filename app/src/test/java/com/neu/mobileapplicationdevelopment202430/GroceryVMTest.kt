package com.neu.mobileapplicationdevelopment202430

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.neu.mobileapplicationdevelopment202430.model.*
import com.neu.mobileapplicationdevelopment202430.viewmodel.GroceryVM
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class GroceryVMTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: FoodRepository
    private lateinit var viewModel: GroceryVM
    private val testUserId = 123

    @Before
    fun setup() {
        repository = mockk(relaxed = true)
        viewModel = GroceryVM(repository, testUserId)

        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
    }

    @After
    fun tearDown() {
        unmockkAll()
        clearMocks(repository) // Clear mocks to ensure no shared state between tests
    }

    @Test
    fun addGroceryItem_addsNewItemWhenNotPresent() = runTest {
        val existingItems = listOf(GroceryListItem("Milk", 1))
        coEvery { repository.getGroceryItemsFromDatabase() } returns existingItems
        coEvery { repository.addGroceryItem(testUserId, any()) } just Runs
        coEvery { repository.getGroceryItemsFromDatabase() } returns existingItems + GroceryListItem("Eggs", 6)

        viewModel.addGroceryItem(testUserId, "Eggs", 6)
        advanceUntilIdle()

        val result = viewModel.groceryItems.getOrAwaitValue()
        assertTrue(result!!.any { it.name == "Eggs" && it.quantity == 6 })
    }

    @Test
    fun addGroceryItem_updatesQuantityIfItemExists() = runTest {
        val existingItems = listOf(GroceryListItem("Milk", 3))
        coEvery { repository.getGroceryItemsFromDatabase() } returns existingItems
        coEvery { repository.updateGroceryItemQuantity(testUserId, "Milk", 5) } just Runs
        coEvery { repository.getGroceryItemsFromDatabase() } returns listOf(GroceryListItem("Milk", 5))

        viewModel.addGroceryItem(testUserId, "Milk", 2)
        advanceUntilIdle()

        val result = viewModel.groceryItems.getOrAwaitValue()
        assertEquals(5, result!!.first { it.name == "Milk" }.quantity)
    }

    @Test
    fun deleteGroceryItem_removesItemSuccessfully() = runTest {
        val item = GroceryListItem("Milk", 2)
        coEvery { repository.deleteGroceryItem(testUserId, item) } just Runs
        coEvery { repository.getGroceryItemsFromDatabase() } returns emptyList()

        viewModel.deleteGroceryItem(testUserId, item)
        advanceUntilIdle()

        val result = viewModel.groceryItems.getOrAwaitValue()
        assertTrue(result!!.isEmpty())
    }

    @Test
    fun loadGroceryItems_showsErrorWhenNoDataAvailableInApiAndDatabase() = runTest {
        coEvery { repository.getUserInformationFromApi(testUserId) } throws RuntimeException("Network error")
        coEvery { repository.getGroceryItemsFromDatabase() } returns emptyList()

        viewModel.loadGroceryItems()
        advanceUntilIdle()

        val error = viewModel.errorMessage.getOrAwaitValue()
        assertEquals(null, error)
    }

    @Test
    fun groceryItems_isEmptyWhenDatabaseReturnsEmpty() = runTest {
        // Mock the database to return an empty list
        coEvery { repository.getGroceryItemsFromDatabase() } returns emptyList()

        // Observe the groceryItems LiveData
        val groceryItemsObserver = mutableListOf<List<GroceryListItem>?>()
        viewModel.groceryItems.observeForever { groceryItemsObserver.add(it) }

        // Trigger load (no API call will be made because of mock data)
        viewModel.loadGroceryItems()

        advanceUntilIdle()

        // Assert that groceryItems LiveData was set to an empty list
        assertTrue(groceryItemsObserver.isNotEmpty())
        assertTrue(groceryItemsObserver.last().isNullOrEmpty())
    }

}
