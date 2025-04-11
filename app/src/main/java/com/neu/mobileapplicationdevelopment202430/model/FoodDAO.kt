package com.neu.mobileapplicationdevelopment202430.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import com.neu.mobileapplicationdevelopment202430.model.IngredientEntity

@Dao
interface FoodDao {
    @Query("SELECT * FROM ingredients")
    fun getAllIngredients(): Flow<List<IngredientEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredients(ingredients: List<IngredientEntity>)

    @Query("SELECT * FROM recipes")
    fun getAllRecipes(): Flow<List<RecipeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipes: List<RecipeEntity>)

    @Query("SELECT * FROM groceryItems")
    fun getAllGroceryItems(): Flow<List<GroceryListEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroceryItems(groceryItems: List<GroceryListEntity>)

    @Query("SELECT * FROM fridgeItems")
    fun getAllFridgeItems(): Flow<List<FridgeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFridgeItems(fridgeItems: List<FridgeEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFridgeItem(fridgeItem: FridgeEntity)

    @Query("UPDATE fridgeItems SET quantity = :quantity WHERE name = :name")
    suspend fun updateQuantityByName(name: String, quantity: Int)

    @Query("UPDATE groceryItems SET quantity = :quantity WHERE name = :name")
    suspend fun updateQuantityByNameGroceries(name: String, quantity: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroceryItem(groceryItem: GroceryListEntity)

    @Query("DELETE FROM groceryItems WHERE name = :name")
    suspend fun deleteGroceryItem(name: String)

    @Query("SELECT * FROM fridgeItems WHERE name = :name LIMIT 1")
    suspend fun getByName(name: String): FridgeEntity?

    @Query("SELECT * FROM reminders")
    fun getAllReminders(): Flow<List<ReminderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminders(reminders: List<ReminderEntity>)
}