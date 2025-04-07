package com.neu.mobileapplicationdevelopment202430.model

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.collections.map

class FoodRepository(private val productDao: FoodDao) {
    val ingredients: Flow<List<IngredientItem>> = productDao.getAllIngredients()
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

}
