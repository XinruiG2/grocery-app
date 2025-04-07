package com.neu.mobileapplicationdevelopment202430.model

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlin.collections.map

class FoodRepository(private val foodDao: FoodDao) {
    val ingredients: Flow<List<IngredientItem>> = foodDao.getAllIngredients()
        .map { entityList -> entityList.map { it.toIngredient() } }

    suspend fun getIngredientsFromApi(): List<IngredientItem> {
        val response = MyRetrofitBuilder.getApiService().getIngredients()
        if (response.isSuccessful) {
            Log.d("FoodRepository", "Response Body: ${response.body()}")
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

}
