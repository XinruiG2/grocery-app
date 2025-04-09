package com.neu.mobileapplicationdevelopment202430.model

import retrofit2.http.Query
import retrofit2.http.GET
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface FoodApiService {
    @GET("getIngredients")
    suspend fun getIngredients(): Response<List<IngredientEntity>>

    @GET("getRecipes")
    suspend fun getRecipes(): Response<List<RecipeEntity>>

    @GET("getStoredUserData/{user_id}")
    suspend fun getStoredUserData(@retrofit2.http.Path("user_id") userId: Int): Response<FullUserEntity>

    @POST("login")
    suspend fun login(@Body user: UserEntity): Response<LoginResponse>

    @POST("signup")
    suspend fun signup(@Body user: UserEntity): Response<SignupResponse>

    @POST("updateFridgeItemForUser")
    suspend fun updateFridgeItemForUser(@Body updateRequest: FridgeItemUpdateForUserRequest): Response<ApiResponse>
}