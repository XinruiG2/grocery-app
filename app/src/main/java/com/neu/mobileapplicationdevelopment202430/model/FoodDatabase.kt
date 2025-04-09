package com.neu.mobileapplicationdevelopment202430.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [IngredientEntity::class, RecipeEntity::class, FridgeItemEntity::class], version = 3)
abstract class FoodDatabase : RoomDatabase() {
    abstract fun foodDao(): FoodDao
    abstract fun fridgeDao(): FridgeDao

    companion object {
        @Volatile
        private var instance: FoodDatabase? = null

        fun getDatabase(context: Context): FoodDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    FoodDatabase::class.java, "food_database"
                ).fallbackToDestructiveMigration().build().also { instance = it }
            }
        }
    }
}
