package com.neu.mobileapplicationdevelopment202430.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {
    // smth for inserting grocery items --> after we figure out user thing?

    // also smth for what's in my fridge maybe

    @Query("SELECT * FROM ingredients")
    fun getAllIngredients(): Flow<List<IngredientEntity>>
}