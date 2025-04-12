import android.content.Context
import com.neu.mobileapplicationdevelopment202430.model.*
import io.mockk.*
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FoodRepositoryTest {

    private lateinit var foodDao: FoodDao
    private lateinit var context: Context
    private lateinit var repository: FoodRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        foodDao = mockk(relaxed = true)
        context = mockk(relaxed = true)



        // Set up default responses for init
        every { foodDao.getAllIngredients() } returns flowOf(emptyList())
        every { foodDao.getAllRecipes() } returns flowOf(emptyList())
        every { foodDao.getAllFridgeItems() } returns flowOf(emptyList())
        every { foodDao.getAllGroceryItems() } returns flowOf(emptyList())
        every { foodDao.getAllReminders() } returns flowOf(emptyList())

        repository = FoodRepository(foodDao, context)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun getIngredientsFromDatabase_returnsCorrectList() = runTest {
        val entities = listOf(
            IngredientEntity(id = 1, name = "Tomato", numDaysGoodFor = 3, numRecipesUsedIn = 2, imageUrl = "tomato.jpg"),
            IngredientEntity(id = 2, name = "Lettuce", numDaysGoodFor = 2, numRecipesUsedIn = 1, imageUrl = "lettuce.jpg")
        )
        every { foodDao.getAllIngredients() } returns flowOf(entities)

        val result = repository.getIngredientsFromDatabase()

        assertEquals(2, result?.size)
        assertEquals("Tomato", result?.first()?.name)
    }

    @Test
    fun getFridgeItemsFromDatabase_returnsCorrectList() = runTest {
        val entities = listOf(
            FridgeEntity(id = 1, name = "Milk", dateBought = "2025-04-10", quantity = 1, imageUrl = "milk.jpg")
        )
        every { foodDao.getAllFridgeItems() } returns flowOf(entities)

        val result = repository.getFridgeItemsFromDatabase()

        assertEquals(1, result?.size)
        assertEquals("Milk", result?.first()?.name)
    }

    @Test
    fun getGroceryItemsFromDatabase_returnsCorrectList() = runTest {
        val entities = listOf(
            GroceryListEntity(name = "Bread", quantity = 2)
        )
        every { foodDao.getAllGroceryItems() } returns flowOf(entities)

        val result = repository.getGroceryItemsFromDatabase()

        assertEquals(1, result?.size)
        assertEquals("Bread", result?.first()?.name)
    }

    @Test
    fun getRemindersFromDatabase_returnsCorrectList() = runTest {
        val entities = listOf(
            ReminderEntity(name = "Check eggs", imageUrl = "url", numDaysTilExpired = 2)
        )
        every { foodDao.getAllReminders() } returns flowOf(entities)

        val result = repository.getRemindersFromDatabase()

        assertEquals(1, result?.size)
        assertEquals("Check eggs", result?.first()?.name)
    }

    @Test
    fun getIngredientsFromDatabase_returnsEmptyListWhenNoneExist() = runTest {
        every { foodDao.getAllIngredients() } returns flowOf(emptyList())

        val result = repository.getIngredientsFromDatabase()

        assertTrue(result?.isEmpty() == true)
    }

    @Test
    fun getRemindersFromDatabase_returnsEmptyListWhenNoneExist() = runTest {
        every { foodDao.getAllReminders() } returns flowOf(emptyList())

        val result = repository.getRemindersFromDatabase()

        assertTrue(result?.isEmpty() == true)
    }

    @Test
    fun getRecipesFromDatabase_returnsCorrectList() = runTest {
        val entities = listOf(
            RecipeEntity(name = "Salad", description = "Fresh salad", ingredients = "Lettuce, Tomato", imageUrl = "salad.jpg")
        )
        every { foodDao.getAllRecipes() } returns flowOf(entities)

        val result = repository.getRecipesFromDatabase()

        assertEquals(1, result?.size)
        assertEquals("Salad", result?.first()?.name)
    }
}
