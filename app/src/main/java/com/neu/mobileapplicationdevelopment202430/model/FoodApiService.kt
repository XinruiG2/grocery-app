package com.neu.mobileapplicationdevelopment202430.model

import retrofit2.http.Query
import retrofit2.http.GET
import retrofit2.Response

interface FoodApiService {
    @GET("getIngredients")
    suspend fun getIngredients(): Response<List<IngredientEntity>>

    @GET("getRecipes")
    suspend fun getRecipes(): Response<List<RecipeEntity>>
}