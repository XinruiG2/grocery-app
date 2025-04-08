package com.neu.mobileapplicationdevelopment202430.model

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlin.collections.map

class FoodRepository(private val foodDao: FoodDao) {
    val ingredients: Flow<List<IngredientItem>> = foodDao.getAllIngredients()
        .map { entityList -> entityList.map { it.toIngredient() } }
    val recipes: Flow<List<RecipeItem>> = foodDao.getAllRecipes()
        .map { entityList -> entityList.map { it.toRecipe() } }

    suspend fun getIngredientsFromApi(): List<IngredientItem> {
        val response = MyRetrofitBuilder.getApiService().getIngredients()
        if (response.isSuccessful) {
            Log.d("FoodRepository Ingredients", "Response Body: ${response.body()}")
            return response.body()?.map { it.toIngredient() } ?: emptyList()
        } else {
            response.errorBody()?.let {
                Log.e("FoodRepository", "Error Body: ${it.string()}")
            }
            Log.e("FoodRepository", "Error: ${response.message()}")
            throw Exception("Error: ${response.message()}")
        }
    }

    suspend fun saveIngredientsToDatabase(ingredients: List<IngredientItem>) {
        val entities = ingredients.map { it.toEntity() }
        foodDao.insertIngredients(entities)
    }

    suspend fun getIngredientsFromDatabase(): List<IngredientItem>? {
        try {
            val ingredientEntities = foodDao.getAllIngredients().firstOrNull()
            Log.d("Food Repository", "Fetched: $ingredientEntities")

            if (ingredientEntities.isNullOrEmpty()) {
                return emptyList()
            }

            return ingredientEntities.map { it.toIngredient() }
        } catch (e: Exception) {
            Log.e("Food Repository", "Error: ${e.message}")
            return emptyList()
        }
    }

    suspend fun getRecipesFromApi(): List<RecipeItem> {
        val response = MyRetrofitBuilder.getApiService().getRecipes()
        if (response.isSuccessful) {
            Log.d("FoodRepository Recipes", "Response Body: ${response.body()}")
            return response.body()?.map { it.toRecipe() } ?: emptyList()
        } else {
            response.errorBody()?.let {
                Log.e("FoodRepository", "Error Body: ${it.string()}")
            }
            Log.e("FoodRepository", "Error: ${response.message()}")
            throw Exception("Error: ${response.message()}")
        }
    }

    suspend fun saveRecipesToDatabase(recipes: List<RecipeItem>) {
        val entities = recipes.map { it.toEntity() }
        foodDao.insertRecipes(entities)
    }

    suspend fun getRecipesFromDatabase(): List<RecipeItem>? {
        try {
            val recipeEntities = foodDao.getAllRecipes().firstOrNull()
            Log.d("Food Repository", "Fetched: $recipeEntities")

            if (recipeEntities.isNullOrEmpty()) {
                return emptyList()
            }

            return recipeEntities.map { it.toRecipe() }
        } catch (e: Exception) {
            Log.e("Food Repository", "Error: ${e.message}")
            return emptyList()
        }
    }

    suspend fun login(username: String, password: String): LoginResponse? {
        val user = UserEntity(username, password)
        val response = MyRetrofitBuilder.getApiService().login(user)
        return if (response.isSuccessful) {
            Log.d("Login in Repository", response.body().toString())
            response.body()
        } else {
            null
        }
    }

    suspend fun signup(username: String, password: String): SignupResponse? {
        val user = UserEntity(username, password)
        val response = MyRetrofitBuilder.getApiService().signup(user)
        return if (response.isSuccessful) {
            Log.d("Login in Repository", response.body().toString())
            response.body()
        } else {
            null
        }
    }

}
