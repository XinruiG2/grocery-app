package com.neu.mobileapplicationdevelopment202430.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FridgeDao {
    @Query("SELECT * FROM fridgeItems")
    suspend fun getAll(): List<FridgeItemEntity>

    @Query("SELECT * FROM fridgeItems WHERE name = :name LIMIT 1")
    suspend fun getByName(name: String): FridgeItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: FridgeItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<FridgeItemEntity>)

    @Update
    suspend fun update(item: FridgeItemEntity)

    @Delete
    suspend fun delete(item: FridgeItemEntity)

    @Query("DELETE FROM fridgeItems")
    suspend fun clearAll()
}