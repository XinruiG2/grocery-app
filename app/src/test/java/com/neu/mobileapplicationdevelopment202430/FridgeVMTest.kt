package com.neu.mobileapplicationdevelopment202430

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.neu.mobileapplicationdevelopment202430.model.*
import com.neu.mobileapplicationdevelopment202430.viewmodel.FridgeVM
import com.neu.mobileapplicationdevelopment202430.viewmodel.GroceryVM
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.*
import java.io.IOException

/**
 * Unit tests for the [FridgeVM].
 */
@OptIn(ExperimentalCoroutinesApi::class)
class FridgeVMTest {

    // Executes tasks synchronously for LiveData
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // Rule to set Main dispatcher using StandardTestDispatcher
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule() // USE THE RULE

    // Mocks
    private lateinit var repository: FoodRepository
    private lateinit var groceryVM: GroceryVM

    // Class under test
    private lateinit var fridgeVM: FridgeVM

    private val testUserId = 123

    @Before
    fun setup() {
        // Dispatcher setup/teardown is handled by MainDispatcherRule
        // Initialize mocks and ViewModel
        repository = mockk(relaxed = true)
        groceryVM = mockk(relaxed = true)
        // The ViewModel will use the test dispatcher set by the rule when accessing Dispatchers.Main
        fridgeVM = FridgeVM(repository, testUserId, groceryVM)

        // Mock Android Log methods
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any<String>()) } returns 0
        every { Log.e(any(), any<String>(), any()) } returns 0 // Overload with Throwable
    }

    @After
    fun tearDown() {
        // Dispatcher reset is handled by MainDispatcherRule
        unmockkStatic(Log::class) // Unmock static Log
    }



    @Test
    fun `addToOrUpdateGroceryList adds new fridge item when it does not exist`() = mainDispatcherRule.testScope.runTest {
        // Arrange
        val groceryItem = GroceryListItem("Cheese", 2)
        val ingredient = IngredientItem("Cheese", 0, 0, "https://image.com/cheese.jpg")
        val expectedAddedItem = FridgeItem("Cheese", "anyDate", 2, "https://image.com/cheese.jpg")

        coEvery { repository.getByName("Cheese") } returns null
        coEvery { repository.addFridgeItem(testUserId, any()) } just Runs
        coEvery { repository.getFridgeItemsFromDatabase() } returns listOf(expectedAddedItem)
        coEvery { groceryVM.deleteGroceryItem(testUserId, groceryItem) } just Runs

        // Act
        fridgeVM.addToOrUpdateGroceryList(listOf(groceryItem), listOf(ingredient))
        advanceUntilIdle() // Ensure background work completes

        // Assert
        val result = fridgeVM.fridgeItems.getOrAwaitValue()
        assertNotNull(result)
        assertEquals(1, result?.size)
        assertEquals("Cheese", result?.first()?.name)
        assertEquals(2, result?.first()?.quantity)
        assertEquals("https://image.com/cheese.jpg", result?.first()?.imageUrl)

        // Verify
        coVerify(exactly = 1) { repository.getByName("Cheese") }
        coVerify(exactly = 1) { repository.addFridgeItem(testUserId, match { it.name == "Cheese" && it.quantity == 2 }) }
        coVerify(exactly = 0) { repository.updateFridgeItemQuantity(any(), any(), any()) }
        coVerify(exactly = 1) { groceryVM.deleteGroceryItem(testUserId, groceryItem) }
        coVerify(exactly = 1) { repository.getFridgeItemsFromDatabase() }
    }

    @Test
    fun `addToOrUpdateGroceryList updates existing item quantity`() = mainDispatcherRule.testScope.runTest {
        // Arrange
        val groceryItem = GroceryListItem("Butter", 1)
        val ingredient = IngredientItem("Butter", 0, 0, "https://image.com/butter.jpg")
        val existingEntity = FridgeEntity(1, "Butter", "2024-04-10", 3, "https://image.com/old_butter.jpg")
        val expectedUpdatedItem = FridgeItem("Butter", "2024-04-10", 4, "https://image.com/old_butter.jpg")

        coEvery { repository.getByName("Butter") } returns existingEntity
        coEvery { repository.updateFridgeItemQuantity(testUserId, "Butter", 4) } just Runs
        coEvery { repository.getFridgeItemsFromDatabase() } returns listOf(expectedUpdatedItem)
        coEvery { groceryVM.deleteGroceryItem(testUserId, groceryItem) } just Runs

        // Act
        fridgeVM.addToOrUpdateGroceryList(listOf(groceryItem), listOf(ingredient))
        advanceUntilIdle()

        // Assert
        val result = fridgeVM.fridgeItems.getOrAwaitValue()
        assertNotNull(result)
        assertEquals(1, result?.size)
        assertEquals(4, result?.first()?.quantity)

        // Verify
        coVerify(exactly = 1) { repository.getByName("Butter") }
        coVerify(exactly = 1) { repository.updateFridgeItemQuantity(testUserId, "Butter", 4) }
        coVerify(exactly = 0) { repository.addFridgeItem(any(), any()) }
        coVerify(exactly = 1) { groceryVM.deleteGroceryItem(testUserId, groceryItem) }
        coVerify(exactly = 1) { repository.getFridgeItemsFromDatabase() }
    }

    @Test
    fun `addToOrUpdateGroceryList handles empty input gracefully`() = mainDispatcherRule.testScope.runTest {
        // Arrange
        val initialItems = listOf(FridgeItem("Existing", "2024-04-13", 1, "url"))
        // Prime LiveData by simulating an initial load
        coEvery { repository.getUserInformationFromApi(testUserId) } throws IOException("Dummy")
        coEvery { repository.getFridgeItemsFromDatabase() } returns initialItems
        fridgeVM.loadFridgeItems()
        advanceUntilIdle()
        fridgeVM.fridgeItems.getOrAwaitValue() // Consume initial value

        // Act
        fridgeVM.addToOrUpdateGroceryList(emptyList(), emptyList())
        advanceUntilIdle()

        // Assert
        val result = fridgeVM.fridgeItems.getOrAwaitValue()
        // The final getFridgeItemsFromDatabase() is called, should reflect initial state
        assertEquals(initialItems, result)

        // Verify
        coVerify(exactly = 0) { repository.getByName(any()) }
        coVerify(exactly = 0) { repository.addFridgeItem(any(), any()) }
        coVerify(exactly = 0) { repository.updateFridgeItemQuantity(any(), any(), any()) }
        coVerify(exactly = 0) { groceryVM.deleteGroceryItem(any(), any()) }
        // Initial load called it once, empty call called it again
        coVerify(exactly = 2) { repository.getFridgeItemsFromDatabase() }
    }

    @Test
    fun updateItemQuantity_doesNothingWhenItemDoesNotExist() = runTest {
        val nonExistentItem = "Milk"

        // Mock the repository to return an empty list (item not found)
        coEvery { repository.getFridgeItemsFromDatabase() } returns emptyList()

        // Attempt to update a non-existent item
        fridgeVM.updateItemQuantity(testUserId, nonExistentItem, 10)

        // Ensure fridgeItems remain empty as no item exists to update
        val result = fridgeVM.fridgeItems.getOrAwaitValue()
        if (result != null) {
            assertTrue(result.isEmpty())
        }
    }

    @Test
    fun addToOrUpdateGroceryList_addsNewItemWhenNotFound() = runTest {
        val groceryItem = GroceryListItem("Tomato", 3)
        val ingredient = IngredientItem("Tomato", 0, 0, "https://image.com")

        // Mock the repository to return null (item not found in fridge)
        coEvery { repository.getByName("Tomato") } returns null
        coEvery { repository.addFridgeItem(testUserId, any()) } just Runs
        coEvery { repository.getFridgeItemsFromDatabase() } returns listOf(
            FridgeItem("Tomato", "2024-04-10", 3, "https://image.com")
        )
        coEvery { groceryVM.deleteGroceryItem(testUserId, groceryItem) } just Runs

        // Add to fridge
        fridgeVM.addToOrUpdateGroceryList(listOf(groceryItem), listOf(ingredient))

        // Ensure the item was added
        val result = fridgeVM.fridgeItems.getOrAwaitValue()
        assertEquals(1, result?.size)
        assertEquals("Tomato", result?.first()?.name)
    }

    @Test
    fun loadFridgeItems_noErrorIfItemsFetchedSuccessfully() = runTest {
        val mockFridgeItems = listOf(FridgeItem("Cheese", "2024-04-10", 5, "https://image.com"))

        // Mock API response
        coEvery { repository.getFridgeItemsFromDatabase() } returns mockFridgeItems

        // Call loadFridgeItems
        fridgeVM.loadFridgeItems()

        // Verify no error message is set
        val error = fridgeVM.errorMessage.getOrAwaitValue()
        assertNull(error)
    }

}