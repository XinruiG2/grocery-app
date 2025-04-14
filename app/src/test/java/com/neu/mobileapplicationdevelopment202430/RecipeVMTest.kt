/*package com.neu.mobileapplicationdevelopment202430

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.neu.mobileapplicationdevelopment202430.model.*
import com.neu.mobileapplicationdevelopment202430.viewmodel.RecipeVM
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class RecipeVMTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: FoodRepository
    private lateinit var viewModel: RecipeVM

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        repository = mockk(relaxed = true)
        viewModel = RecipeVM(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // Test for checking if recipes list is updated correctly
    @Test
    fun loadRecipes_updatesRecipeList() = runTest {
        val newRecipe = RecipeItem(
            name = "Spaghetti",
            description = "Boil pasta and add sauce.",
            ingredients = "Pasta, Sauce",
            imageUrl = "https://image.com/spaghetti.jpg"
        )

        // Mock repository call for fetching recipes
        coEvery { repository.getRecipesFromDatabase() } returns listOf(newRecipe)

        viewModel.loadRecipes()
        advanceUntilIdle()

        val result = viewModel.recipes.getOrAwaitValue()
        assertTrue(result?.any { it.name == "Spaghetti" } ?: false)
    }

    // Test for handling empty recipe list
    @Test
    fun loadRecipes_whenNoRecipesAvailable_setsErrorMessage() = runTest {
        coEvery { repository.getRecipesFromDatabase() } returns emptyList()

        viewModel.loadRecipes()
        advanceUntilIdle()

        val error = viewModel.errorMessage.getOrAwaitValue()
        assertEquals("Recipes unavailable right now", error)
    }

    // Test for error handling when loading recipes from the API
    @Test
    fun loadRecipes_errorDuringLoad_setsErrorMessage() = runTest {
        coEvery { repository.getRecipesFromDatabase() } throws Exception("Failed to load recipes")

        viewModel.loadRecipes()
        advanceUntilIdle()

        val error = viewModel.errorMessage.getOrAwaitValue()
        assertTrue(error!!.contains("Failed to load recipes"))
    }

    // Test for verifying the loading state during recipe loading
    @Test
    fun loadRecipes_setsLoadingState() = runTest {
        val newRecipe = RecipeItem(
            name = "Pizza",
            description = "Bake dough with toppings.",
            ingredients = "Dough, Cheese, Sauce",
            imageUrl = "https://image.com/pizza.jpg"
        )

        // Mock repository to return a list of recipes
        coEvery { repository.getRecipesFromDatabase() } returns listOf(newRecipe)

        viewModel.loadRecipes()
        advanceUntilIdle()

        // Check that the loading state was set to true initially and false after loading
        assertTrue(viewModel.isLoading.getOrAwaitValue())
        assertFalse(viewModel.isLoading.getOrAwaitValue())
    }

    // Test for checking if the recipes list is empty when there are no recipes in the database
    @Test
    fun loadRecipes_setsEmptyListWhenNoRecipes() = runTest {
        coEvery { repository.getRecipesFromDatabase() } returns emptyList()

        viewModel.loadRecipes()
        advanceUntilIdle()

        val result = viewModel.recipes.getOrAwaitValue()
        assertTrue(result.isNullOrEmpty())
    }

    // Test for ensuring that the recipes list is updated after fetching data from the database
    @Test
    fun loadRecipes_updatesRecipeListFromDatabase() = runTest {
        val newRecipe = RecipeItem(
            name = "Salad",
            description = "Mix vegetables.",
            ingredients = "Lettuce, Tomato, Cucumber",
            imageUrl = "https://image.com/salad.jpg"
        )

        coEvery { repository.getRecipesFromDatabase() } returns listOf(newRecipe)

        viewModel.loadRecipes()
        advanceUntilIdle()

        val result = viewModel.recipes.getOrAwaitValue()
        assertTrue(result?.any { it.name == "Salad" } ?: false)
    }

    // Test for checking error message when API fails to fetch recipes
    @Test
    fun loadRecipes_apiError_setsErrorMessage() = runTest {
        coEvery { repository.getRecipesFromDatabase() } throws Exception("API Error")

        viewModel.loadRecipes()
        advanceUntilIdle()

        val error = viewModel.errorMessage.getOrAwaitValue()
        assertTrue(error!!.contains("API Error"))
    }
}




 */