package com.neu.mobileapplicationdevelopment202430.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ingredients")
data class IngredientEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "numDaysGoodFor") val numDaysGoodFor: Int,
    @ColumnInfo(name = "numRecipesUsedIn") val numRecipesUsedIn: Int,
    @ColumnInfo(name = "imageUrl") val imageUrl: String
)
