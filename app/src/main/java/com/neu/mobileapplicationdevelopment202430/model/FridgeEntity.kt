package com.neu.mobileapplicationdevelopment202430.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fridgeItems")
data class FridgeEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "dateBought") val dateBought: String,
    @ColumnInfo(name = "quantity") var quantity: Int,
    @ColumnInfo(name = "imageUrl") val imageUrl: String
)