package com.neu.mobileapplicationdevelopment202430.model

class FridgeRepository(private val dao: FridgeDao) {
    suspend fun getAll() = dao.getAll()
    suspend fun getByName(name: String) = dao.getByName(name)
    suspend fun insert(item: FridgeItemEntity) = dao.insert(item)
    suspend fun insertAll(items: List<FridgeItemEntity>) = dao.insertAll(items)
    suspend fun update(item: FridgeItemEntity) = dao.update(item)
    suspend fun delete(item: FridgeItemEntity) = dao.delete(item)
    suspend fun clearAll() = dao.clearAll()
}