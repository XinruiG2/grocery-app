package com.neu.mobileapplicationdevelopment202430.model

import retrofit2.http.Query
import retrofit2.http.GET
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path

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

    @POST("updateGroceryItemForUser")
    suspend fun updateGroceryItemForUser(@Body updateRequest: GroceryItemUpdateForUserRequest): Response<ApiResponse>

    @POST("addToFridgeItems/{user_id}")
    suspend fun addToFridgeItems(@Path("user_id") userId: Int, @Body fridgeItem: FridgeItem): Response<ApiResponse>

    @POST("addToGroceryList/{user_id}")
    suspend fun addToGroceryList(@Path("user_id") userId: Int, @Body groceryItem: GroceryListItem): Response<ApiResponse>

    @DELETE("deleteFromGroceryList/{user_id}/{item_name}/{item_quantity}")
    suspend fun deleteFromGroceryList(
        @Path("user_id") userId: Int,
        @Path("item_name") itemName: String,
        @Path("item_quantity") itemQuantity: Int
    ): Response<ApiResponse>
}