package com.neu.mobileapplicationdevelopment202430.model

import android.content.Context
import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlin.collections.map

class FoodRepository(private val foodDao: FoodDao, private val context: Context) {
    val ingredients: Flow<List<IngredientItem>> = foodDao.getAllIngredients()
        .map { entityList -> entityList.map { it.toIngredient() } }
    val recipes: Flow<List<RecipeItem>> = foodDao.getAllRecipes()
        .map { entityList -> entityList.map { it.toRecipe() } }
    private val userInformation = UserInformation(context)

    suspend fun getIngredientsFromApi(): List<IngredientItem> {
        val response = MyRetrofitBuilder.getApiService().getIngredients()
        if (response.isSuccessful) {
            //Log.d("FoodRepository Ingredients", "Response Body: ${response.body()}")
            return response.body()?.map { it.toIngredient() } ?: emptyList()
        } else {
            response.errorBody()?.let {
                //Log.e("FoodRepository", "Error Body: ${it.string()}")
            }
           // Log.e("FoodRepository", "Error: ${response.message()}")
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
            //Log.d("Food Repository", "Fetched: $ingredientEntities")

            if (ingredientEntities.isNullOrEmpty()) {
                return emptyList()
            }

            return ingredientEntities.map { it.toIngredient() }
        } catch (e: Exception) {
            //Log.e("Food Repository", "Error: ${e.message}")
            return emptyList()
        }
    }

    suspend fun getRecipesFromApi(): List<RecipeItem> {
        val response = MyRetrofitBuilder.getApiService().getRecipes()
        if (response.isSuccessful) {
            //Log.d("FoodRepository Recipes", "Response Body: ${response.body()}")
            return response.body()?.map { it.toRecipe() } ?: emptyList()
        } else {
            response.errorBody()?.let {
               // Log.e("FoodRepository", "Error Body: ${it.string()}")
            }
            //Log.e("FoodRepository", "Error: ${response.message()}")
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
          //  Log.d("Food Repository", "Fetched: $recipeEntities")

            if (recipeEntities.isNullOrEmpty()) {
                return emptyList()
            }

            return recipeEntities.map { it.toRecipe() }
        } catch (e: Exception) {
           // Log.e("Food Repository", "Error: ${e.message}")
            return emptyList()
        }
    }

    suspend fun login(username: String, password: String): LoginResponse? {
        val user = UserEntity(username, password)
        val response = MyRetrofitBuilder.getApiService().login(user)
        if (response.isSuccessful) {
            //Log.d("Login in Repository", response.body().toString())
            response.body()?.user_id?.let { userInformation.saveUserId(it) }
            return response.body()
        } else {
            return null
        }
    }

    suspend fun signup(username: String, password: String): SignupResponse? {
        val user = UserEntity(username, password)
        val response = MyRetrofitBuilder.getApiService().signup(user)
        if (response.isSuccessful) {
            //Log.d("Login in Repository", response.body().toString())
            response.body()?.user_id?.let { userInformation.saveUserId(it) }
            return response.body()
        } else {
            return null
        }
    }

    suspend fun getUserInformationFromApi(userId: Int): FullUserEntity {
      //  Log.e("FoodRepository", "Getting user info...")
        val response = MyRetrofitBuilder.getApiService().getStoredUserData(userId)
       // Log.e("FoodRepository", response.toString())
        if (response.isSuccessful) {
           // Log.e("FoodRepository", "In successful")
            return response.body() ?: throw Exception("User not found")
        } else {
            response.errorBody()?.let {
               // Log.e("FoodRepository", "Error Body: ${it.string()}")
            }
           // Log.e("FoodRepository", "Error: ${response.message()}")
            throw Exception("Error: ${response.message()}")
        }
    }

    suspend fun getFridgeItemsFromDatabase(): List<FridgeItem>? {
        try {
            val fridgeEntities = foodDao.getAllFridgeItems().firstOrNull()
           // Log.d("Food Repository", "Fetched: $fridgeEntities")

            if (fridgeEntities.isNullOrEmpty()) {
                return emptyList()
            }

            return fridgeEntities.map { it.toFridgeItem() }
        } catch (e: Exception) {
           // Log.e("Food Repository", "Error: ${e.message}")
            return emptyList()
        }
    }

    suspend fun saveFridgeItemsToDatabase(fridgeItems: List<FridgeItem>) {
        val entities = fridgeItems.map { it.toEntity() }
        foodDao.insertFridgeItems(entities)
    }

    suspend fun updateFridgeItemQuantity(userId: Int, name: String, quantity: Int) {
       // Log.e("FoodRepository", "Update given information: ${userId} ${name} ${quantity} ")

        try {
            val response = MyRetrofitBuilder.getApiService().updateFridgeItemForUser(
                FridgeItemUpdateForUserRequest(userId, name, quantity)
            )
            if (response.isSuccessful) {
                foodDao.updateQuantityByName(name, quantity)
              //  Log.d("FoodRepository", "Fridge Updated: ${response.body()?.message}")
            } else {
              //  Log.e("FoodRepository", "Update Failed: ${response.message()}")
            }
        } catch (e: Exception) {
           // Log.e("FoodRepository", "Error: ${e.message}\"")
        }
    }

    suspend fun updateGroceryItemQuantity(userId: Int, name: String, quantity: Int) {
        try {
            val response = MyRetrofitBuilder.getApiService().updateGroceryItemForUser(
                GroceryItemUpdateForUserRequest(userId, name, quantity)
            )
            if (response.isSuccessful) {
                foodDao.updateQuantityByNameGroceries(name, quantity)
             //   Log.d("FoodRepository", "Grocery Updated: ${response.body()?.message}")
            } else {
             //   Log.e("FoodRepository", "Update Failed: ${response.message()}")
            }
        } catch (e: Exception) {
           // Log.e("FoodRepository", "Error: ${e.message}\"")
        }
    }

    suspend fun addGroceryItem(userId: Int, groceryItem: GroceryListItem) {
        try {
            val response = MyRetrofitBuilder.getApiService().addToGroceryList(userId, groceryItem)
            if (response.isSuccessful) {
                foodDao.insertGroceryItem(groceryItem.toEntity())
              //  Log.d("FoodRepository", "GI added successfully: ${response.body()?.message}")
            } else {
             //   Log.e("FoodRepository", "Failed to add GI: ${response.message()}")
            }
        } catch (e: Exception) {
          //  Log.e("FoodRepository", "Error: ${e.message}")
        }
    }

    suspend fun deleteGroceryItem(userId: Int, groceryItem: GroceryListItem) {
        try {
            val response = MyRetrofitBuilder.getApiService().deleteFromGroceryList(userId, groceryItem.name, groceryItem.quantity)
            if (response.isSuccessful) {
                foodDao.deleteGroceryItem(groceryItem.name)
              //  Log.d("FoodRepository", "GI deleted successfully: ${response.body()?.message}")
            } else {
               // Log.e("FoodRepository", "Failed to delete GI: ${response.message()}")
            }
        } catch (e: Exception) {
          //  Log.e("FoodRepository", "Error: ${e.message}")
        }
    }

    suspend fun saveGroceryItemsToDatabase(groceryItems: List<GroceryListItem>) {
        val entities = groceryItems.map { it.toEntity() }
        foodDao.insertGroceryItems(entities)
    }

    suspend fun getGroceryItemsFromDatabase(): List<GroceryListItem>? {
        try {
            val groceryEntities = foodDao.getAllGroceryItems().firstOrNull()

            if (groceryEntities.isNullOrEmpty()) {
                return emptyList()
            }

            return groceryEntities.map { it.toGroceryItem() }
        } catch (e: Exception) {
          //  Log.e("Food Repository", "Error: ${e.message}")
            return emptyList()
        }
    }

    suspend fun getByName(name: String) = foodDao.getByName(name)

    suspend fun addFridgeItem(userId: Int, fridgeItem: FridgeItem) {
       // Log.d("FoodRepository", "given: ${fridgeItem.toString()}")
        try {
            val response = MyRetrofitBuilder.getApiService().addToFridgeItems(userId, fridgeItem)
            if (response.isSuccessful) {
                foodDao.insertFridgeItem(fridgeItem.toEntity())
             //   Log.d("FoodRepository", "FI added successfully: ${response.body()?.message}")
            } else {
              //  Log.e("FoodRepository", "Failed to add FI: ${response.message()}")
            }
        } catch (e: Exception) {
         //   Log.e("FoodRepository", "Error: ${e.message}")
        }
    }

    suspend fun saveRemindersToDatabase(reminders: List<ReminderItem>) {
        val entities = reminders.map { it.toEntity() }
        foodDao.insertReminders(entities)
    }

    suspend fun getRemindersFromDatabase(): List<ReminderItem>? {
        try {
            val reminderEntities = foodDao.getAllReminders().firstOrNull()
           // Log.d("Food Repository", "Fetched: $reminderEntities")

            if (reminderEntities.isNullOrEmpty()) {
                return emptyList()
            }

            return reminderEntities.map { it.toReminderItem() }
        } catch (e: Exception) {
          //  Log.e("Food Repository", "Error: ${e.message}")
            return emptyList()
        }
    }

}
