package com.neu.mobileapplicationdevelopment202430

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.neu.mobileapplicationdevelopment202430.model.*
import com.neu.mobileapplicationdevelopment202430.viewmodel.FridgeVM
import com.neu.mobileapplicationdevelopment202430.viewmodel.GroceryVM
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class FridgeVMTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: FoodRepository
    private lateinit var groceryVM: GroceryVM
    private lateinit var fridgeVM: FridgeVM

    private val testUserId = 123

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        repository = mockk(relaxed = true)
        groceryVM = mockk(relaxed = true)
        fridgeVM = FridgeVM(repository, testUserId, groceryVM)

        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
    }




    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun addToOrUpdateGroceryList_addsNewFridgeItem() = runTest {
        val groceryItem = GroceryListItem("Cheese", 2)
        val ingredient = IngredientItem("Cheese", 0, 0, "https://image.com")

        coEvery { repository.getByName("Cheese") } returns null
        coEvery { repository.addFridgeItem(any(), any()) } just Runs
        coEvery { repository.getFridgeItemsFromDatabase() } returns listOf(
            FridgeItem("Cheese", "2024-04-10", 2, "https://image.com")
        )
        coEvery { groceryVM.deleteGroceryItem(any(), any()) } just Runs

        fridgeVM.addToOrUpdateGroceryList(
            listOf(groceryItem),
            listOf(ingredient)
        )

        val result = fridgeVM.fridgeItems.getOrAwaitValue()
        assertEquals(1, result?.size)
        assertEquals("Cheese", result?.first()?.name)
    }

    @Test
    fun addToOrUpdateGroceryList_updatesExistingQuantity() = runTest {
        val groceryItem = GroceryListItem("Butter", 1)
        val ingredient = IngredientItem("Butter", 0, 0, "https://image.com")

        val existingFridgeEntity = FridgeEntity(
            id = 1,
            name = "Butter",
            dateBought = "2024-04-10",
            quantity = 3,
            imageUrl = ""
        )

        coEvery { repository.getByName("Butter") } returns existingFridgeEntity
        coEvery { repository.updateFridgeItemQuantity(any(), any(), any()) } just Runs
        coEvery { repository.getFridgeItemsFromDatabase() } returns listOf(
            FridgeItem("Butter", "2024-04-10", 4, "")
        )
        coEvery { groceryVM.deleteGroceryItem(any(), any()) } just Runs

        fridgeVM.addToOrUpdateGroceryList(
            listOf(groceryItem),
            listOf(ingredient)
        )

        val result = fridgeVM.fridgeItems.getOrAwaitValue()
        assertEquals(4, result?.first()?.quantity)
    }
}
