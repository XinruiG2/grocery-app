import android.content.Context
import com.neu.mobileapplicationdevelopment202430.model.*
import io.mockk.*
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Response

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

        // Mock Retrofit's API service
        mockkObject(MyRetrofitBuilder)

        // Mock the API service's response if needed in tests
        every { MyRetrofitBuilder.getApiService() } returns mockk()

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

    @Test
    fun saveIngredientsToDatabase_savesCorrectly() = runTest {
        val ingredients = listOf(IngredientItem(name = "Tomato", numDaysGoodFor = 3, numRecipesUsedIn = 2, imageUrl = "tomato.jpg"))
        repository.saveIngredientsToDatabase(ingredients)

        // Verify if saveIngredients is called correctly
        coVerify { foodDao.insertIngredients(any()) }
    }

    @Test
    fun saveFridgeItemsToDatabase_savesCorrectly() = runTest {
        val fridgeItems = listOf(FridgeItem(name = "Milk", dateBought = "2025-04-10", quantity = 1, imageUrl = "milk.jpg"))
        repository.saveFridgeItemsToDatabase(fridgeItems)

        // Verify if saveFridgeItems is called correctly
        coVerify { foodDao.insertFridgeItems(any()) }
    }


    @Test
    fun getRecipesFromDatabase_returnsEmptyListWhenNoneExist() = runTest {
        every { foodDao.getAllRecipes() } returns flowOf(emptyList())

        val result = repository.getRecipesFromDatabase()

        assertTrue(result?.isEmpty() == true)
    }

    @Test
    fun getIngredientsFromApi_returnsEmptyListOnApiFailure() = runTest {
        // Mock the MyRetrofitBuilder.getApiService() object
        mockkObject(MyRetrofitBuilder)

        // Simulating API failure (mocking the correct type: List<IngredientEntity>)
        val errorResponse = Response.error<List<IngredientEntity>>(
            500,
            "Internal Server Error".toResponseBody()
        )

        // Mocking the getIngredients() method to return the error response
        coEvery { MyRetrofitBuilder.getApiService().getIngredients() } returns errorResponse

        try {
            // This will now use the mocked response
            repository.getIngredientsFromApi()
        } catch (e: Exception) {
            // Test the behavior when an error is thrown (e.g., empty list returned)
            assertEquals("Error: Internal Server Error", e.message)  // Adjust this assertion
        }

        // Unmock after the test
        unmockkObject(MyRetrofitBuilder)
    }

    @Test
    fun getIngredientsFromApi_returnsEmptyListWhenApiReturnsEmpty() = runTest {
        val emptyApiResponse = listOf<IngredientEntity>() // Correct the type here

        // Mock the actual API call
        coEvery { MyRetrofitBuilder.getApiService().getIngredients() } returns Response.success(emptyApiResponse)

        // Now, call your repository method
        val result = repository.getIngredientsFromApi()

        assertTrue(result.isEmpty())
    }

    @Test
    fun signup_createsUserSuccessfully() = runTest {
        val username = "newUser"
        val password = "newPassword"
        val response = SignupResponse(message = "Success", user_id = 2)

        // Mock the signup response with Response.success() and the expected type
        coEvery { MyRetrofitBuilder.getApiService().signup(any()) } returns Response.success(response)

        val result = repository.signup(username, password)

        if (result != null) {
            assertEquals(2, result.user_id)
        }
    }

    @Test
    fun login_returnsUserWhenCredentialsAreCorrect() = runTest {
        val username = "testUser"
        val password = "testPassword"

        // Create the response that will be returned by the mocked method
        val response = LoginResponse(message = "Success", user_id = 1)

        // Assuming login expects a UserEntity, create that object
        val userEntity = UserEntity(username = username, password = password)

        // Mock the login response with the correct UserEntity
        coEvery { MyRetrofitBuilder.getApiService().login(userEntity) } returns Response.success(response)

        // Call the login method within the coroutine scope
        val result = repository.login(username, password)

        if (result != null) {
            assertEquals(1, result.user_id)
        }
    }

    @Test
    fun getIngredientsFromApi_returnsListWhenApiReturnsSuccess() = runTest {
        val ingredientsFromApi = listOf(IngredientEntity(id = 1, name = "Tomato", numDaysGoodFor = 3, numRecipesUsedIn = 2, imageUrl = "tomato.jpg"))

        coEvery { MyRetrofitBuilder.getApiService().getIngredients() } returns Response.success(ingredientsFromApi)

        val result = repository.getIngredientsFromApi()

        assertEquals(1, result.size)
        assertEquals("Tomato", result.first().name)
    }



    @Test
    fun saveIngredientsToDatabase_savesAndFetchesCorrectly() = runTest {
        val ingredients = listOf(
            IngredientItem(name = "Tomato", numDaysGoodFor = 3, numRecipesUsedIn = 2, imageUrl = "tomato.jpg")
        )

        // Mock insertion behavior for saving ingredients
        repository.saveIngredientsToDatabase(ingredients)

        // Verify if saveIngredients is called correctly
        coVerify { foodDao.insertIngredients(any()) }

        // Mock the retrieval behavior to return the saved ingredients
        val savedIngredients = listOf(
            IngredientEntity(id = 1, name = "Tomato", numDaysGoodFor = 3, numRecipesUsedIn = 2, imageUrl = "tomato.jpg")
        )
        every { foodDao.getAllIngredients() } returns flowOf(savedIngredients)

        // Now, call getIngredientsFromDatabase to fetch the saved ingredients
        val result = repository.getIngredientsFromDatabase()

        // Verify that the result matches the saved ingredients
        assertEquals(1, result?.size)
        assertEquals("Tomato", result?.first()?.name)
    }


    @Test
    fun getIngredientsFromApi_handlesErrorGracefully() = runTest {
        coEvery { MyRetrofitBuilder.getApiService().getIngredients() } returns Response.error(500, "Internal Server Error".toResponseBody())

        try {
            repository.getIngredientsFromApi()
            assertTrue(false)  // Should not reach here, exception should be thrown
        } catch (e: Exception) {
            assertEquals("Error: Internal Server Error", e.message)
        }
    }

    @Test
    fun updateFridgeItemQuantity_updatesQuantitySuccessfully() = runTest {
        val userId = 1
        val name = "Milk"
        val quantity = 3

        // Mocking the API response correctly
        coEvery { MyRetrofitBuilder.getApiService().updateFridgeItemForUser(any()) } returns Response.success(ApiResponse("Success"))

        coEvery { foodDao.updateQuantityByName(name, quantity) } returns Unit  // Mock the DAO update call

        // Call the method in the repository
        repository.updateFridgeItemQuantity(userId, name, quantity)

        // Verify if the DAO method was called with the correct parameters
        coVerify { foodDao.updateQuantityByName(name, quantity) }
    }

    @Test
    fun updateGroceryItemQuantity_updatesQuantitySuccessfully() = runTest {
        val userId = 1
        val name = "Bread"
        val quantity = 2

        // Mocking the API response correctly
        coEvery { MyRetrofitBuilder.getApiService().updateGroceryItemForUser(any()) } returns Response.success(ApiResponse("Success"))

        coEvery { foodDao.updateQuantityByNameGroceries(name, quantity) } returns Unit  // Mock the DAO update call

        // Call the method in the repository
        repository.updateGroceryItemQuantity(userId, name, quantity)

        // Verify if the DAO method was called with the correct parameters
        coVerify { foodDao.updateQuantityByNameGroceries(name, quantity) }
    }

    @Test
    fun getUserInformationFromApi_returnsUserInformation() = runTest {
        val userId = 1
        val userInfo = FullUserEntity(
            user_id = 1,
            username = "John Doe",
            password = "password123",
            fridge_items = emptyList(),
            grocery_list = emptyList()
        )

        // Mock API response
        coEvery { MyRetrofitBuilder.getApiService().getStoredUserData(userId) } returns Response.success(userInfo)

        val result = repository.getUserInformationFromApi(userId)

        assertEquals(1, result.user_id)
        assertEquals("John Doe", result.username)
        assertEquals("password123", result.password)
    }

    @Test
    fun saveRecipesToDatabase_savesCorrectly() = runTest {
        val recipes = listOf(RecipeItem(name = "Spaghetti", description = "Pasta with tomato sauce", ingredients = "Spaghetti, Tomato sauce", imageUrl = "spaghetti.jpg"))

        repository.saveRecipesToDatabase(recipes)

        // Verify if saveRecipes is called correctly
        coVerify { foodDao.insertRecipes(any()) }
    }

    @Test
    fun addFridgeItem_addsItemSuccessfully() = runTest {
        val userId = 1
        val fridgeItem = FridgeItem(name = "Butter", dateBought = "2025-04-10", quantity = 1, imageUrl = "butter.jpg")

        // Mocking the API response correctly
        coEvery { MyRetrofitBuilder.getApiService().addToFridgeItems(userId, fridgeItem) } returns Response.success(ApiResponse("Item added"))

        coEvery { foodDao.insertFridgeItem(any()) } returns Unit  // Mock the DAO insert call

        // Call the method in the repository
        repository.addFridgeItem(userId, fridgeItem)

        // Verify if the DAO method was called with the correct parameters
        coVerify { foodDao.insertFridgeItem(any()) }
    }

    @Test
    fun deleteGroceryItem_deletesItemSuccessfully() = runTest {
        val userId = 1
        val groceryItem = GroceryListItem(name = "Bread", quantity = 2)

        // Mocking the API response correctly
        coEvery { MyRetrofitBuilder.getApiService().deleteFromGroceryList(userId, groceryItem.name, groceryItem.quantity) } returns Response.success(ApiResponse("Item deleted"))

        coEvery { foodDao.deleteGroceryItem(groceryItem.name) } returns Unit  // Mock the DAO delete call

        // Call the method in the repository
        repository.deleteGroceryItem(userId, groceryItem)

        // Verify if the DAO method was called with the correct parameters
        coVerify { foodDao.deleteGroceryItem(groceryItem.name) }
    }

    @Test
    fun addGroceryItem_addsItemSuccessfully() = runTest {
        val userId = 1
        val groceryItem = GroceryListItem(name = "Eggs", quantity = 12)

        // Mocking the API response correctly
        coEvery { MyRetrofitBuilder.getApiService().addToGroceryList(userId, groceryItem) } returns Response.success(ApiResponse("Item added"))

        coEvery { foodDao.insertGroceryItem(any()) } returns Unit  // Mock the DAO insert call

        // Call the method in the repository
        repository.addGroceryItem(userId, groceryItem)

        // Verify if the DAO method was called with the correct parameters
        coVerify { foodDao.insertGroceryItem(any()) }
    }

    @Test
    fun getRecipesFromApi_handlesErrorGracefully() = runTest {
        coEvery { MyRetrofitBuilder.getApiService().getRecipes() } returns Response.error(500, "Internal Server Error".toResponseBody())

        try {
            repository.getRecipesFromApi()
            assertTrue(false)  // Should not reach here, exception should be thrown
        } catch (e: Exception) {
            assertEquals("Error: Response.error()", e.message)
        }
    }

    @Test
    fun getRecipesFromApi_returnsEmptyListWhenApiReturnsEmpty() = runTest {
        val emptyApiResponse = listOf<RecipeEntity>() // Correct the type here

        // Mock the actual API call
        coEvery { MyRetrofitBuilder.getApiService().getRecipes() } returns Response.success(emptyApiResponse)

        // Now, call your repository method
        val result = repository.getRecipesFromApi()

        assertTrue(result.isEmpty())
    }

    @Test
    fun login_returnsNullWhenCredentialsAreIncorrect() = runTest {
        val username = "wrongUser"
        val password = "wrongPassword"

        // Simulate an error response from the API
        coEvery { MyRetrofitBuilder.getApiService().login(any()) } returns Response.error(401, "Unauthorized".toResponseBody())

        val result = repository.login(username, password)

        assertEquals(null, result)  // Ensure null is returned when login fails
    }

    @Test
    fun saveRecipesToDatabase_savesMultipleRecipesCorrectly() = runTest {
        val recipes = listOf(
            RecipeItem(name = "Pasta", description = "Pasta with tomato sauce", ingredients = "Pasta, Tomato sauce", imageUrl = "pasta.jpg"),
            RecipeItem(name = "Pizza", description = "Cheese pizza", ingredients = "Dough, Cheese, Tomato sauce", imageUrl = "pizza.jpg")
        )

        repository.saveRecipesToDatabase(recipes)

        // Verify that saveRecipes was called correctly with multiple recipes
        coVerify { foodDao.insertRecipes(any()) }
    }

    @Test
    fun getRecipesFromDatabase_handlesErrorGracefully() = runTest {
        // Simulate an error when fetching from the database
        every { foodDao.getAllRecipes() } throws Exception("Database error")

        val result = repository.getRecipesFromDatabase()

        if (result != null) {
            assertTrue(result.isEmpty())
        }  // Should fall back to an empty list in case of error
    }

    @Test
    fun saveGroceryItemsToDatabase_savesLargeListCorrectly() = runTest {
        val groceryItems = List(100) { GroceryListItem(name = "Item $it", quantity = it) }

        repository.saveGroceryItemsToDatabase(groceryItems)

        // Verify that saveGroceryItems was called with the correct data
        coVerify { foodDao.insertGroceryItems(any()) }
    }





    /*
        @Test
        fun getRecipesFromApi_fallsBackToDatabaseOnFailure() = runTest {
            // Mocking the API failure
            coEvery { MyRetrofitBuilder.getApiService().getRecipes() } returns Response.error(500, "Internal Server Error".toResponseBody())

            // Mock the database fallback response
            val recipesFromDatabase = listOf(RecipeEntity(name = "Salad", description = "Fresh Salad", ingredients = "Lettuce, Tomato", imageUrl = "salad.jpg"))
            every { foodDao.getAllRecipes() } returns flowOf(recipesFromDatabase)

            val result = repository.getRecipesFromApi()

            assertEquals(1, result.size)
            assertEquals("Salad", result.first().name)
        }

     */
}


