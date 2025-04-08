package com.neu.mobileapplicationdevelopment202430.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [IngredientEntity::class, RecipeEntity::class], version = 2)
abstract class FoodDatabase : RoomDatabase() {
    abstract fun foodDao(): FoodDao

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
