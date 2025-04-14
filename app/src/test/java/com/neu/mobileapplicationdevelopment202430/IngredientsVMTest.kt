import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.neu.mobileapplicationdevelopment202430.model.FoodRepository
import com.neu.mobileapplicationdevelopment202430.model.IngredientItem
import com.neu.mobileapplicationdevelopment202430.viewmodel.IngredientsVM
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import io.mockk.unmockkAll
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class IngredientsVMTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: FoodRepository
    private lateinit var viewModel: IngredientsVM
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk(relaxed = true)
        viewModel = IngredientsVM(
            repository = repository,
            ioDispatcher = testDispatcher,
            mainDispatcher = testDispatcher
        )
    }


    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun loadIngredients_successfullyLoadsFromApi() = runTest {
        val apiIngredients = listOf(
            IngredientItem(name = "Tomato", numDaysGoodFor = 3, numRecipesUsedIn = 2, imageUrl = "tomato.jpg"),
            IngredientItem(name = "Lettuce", numDaysGoodFor = 2, numRecipesUsedIn = 1, imageUrl = "lettuce.jpg")
        )

        coEvery { repository.getIngredientsFromApi() } returns apiIngredients
        coEvery { repository.saveIngredientsToDatabase(any()) } just Runs

        viewModel.loadIngredients()
        testScheduler.advanceUntilIdle()

        assertEquals(apiIngredients, viewModel.ingredients.value)
        assertEquals(false, viewModel.isLoading.value)
        assertNull(viewModel.errorMessage.value)
    }

    @Test
    fun loadIngredients_fallbackToDatabaseOnApiError() = runTest {
        val dbIngredients = listOf(
            IngredientItem(name = "Carrot", numDaysGoodFor = 5, numRecipesUsedIn = 1, imageUrl = "carrot.jpg")
        )

        coEvery { repository.getIngredientsFromApi() } throws Exception("Network error")
        coEvery { repository.getIngredientsFromDatabase() } returns dbIngredients

        viewModel.loadIngredients()
        testScheduler.advanceUntilIdle()

        assertEquals(dbIngredients, viewModel.ingredients.value)
        assertEquals(false, viewModel.isLoading.value)
        assertNull(viewModel.errorMessage.value)
    }

    @Test
    fun loadIngredients_showsErrorWhenBothApiAndDbFail() = runTest {
        coEvery { repository.getIngredientsFromApi() } throws Exception("Network error")
        coEvery { repository.getIngredientsFromDatabase() } returns emptyList()

        viewModel.loadIngredients()
        testScheduler.advanceUntilIdle()

        assertEquals(emptyList<IngredientItem>(), viewModel.ingredients.value)
        assertEquals(false, viewModel.isLoading.value)
        assertEquals("Ingredients unavailable right now", viewModel.errorMessage.value)
    }
}
