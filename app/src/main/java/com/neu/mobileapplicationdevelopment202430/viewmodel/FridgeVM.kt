package com.neu.mobileapplicationdevelopment202430.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neu.mobileapplicationdevelopment202430.model.FridgeItem
import com.neu.mobileapplicationdevelopment202430.model.FridgeItemEntity
import com.neu.mobileapplicationdevelopment202430.model.FridgeRepository
import com.neu.mobileapplicationdevelopment202430.model.GroceryListItem
import com.neu.mobileapplicationdevelopment202430.model.IngredientItem
import com.neu.mobileapplicationdevelopment202430.model.toFridgeItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale

class FridgeVM(private val repository: FridgeRepository) : ViewModel() {
    private val _fridgeItems = MutableStateFlow<List<FridgeItem>>(emptyList())
    val fridgeItems: StateFlow<List<FridgeItem>> = _fridgeItems

    init {
        loadFridgeItems()
    }

    private fun loadFridgeItems() {
        viewModelScope.launch {
            _fridgeItems.value = repository.getAll().map { it.toFridgeItem() }
        }
    }

    suspend fun addToOrUpdateGroceryList(
        groceryItems: List<GroceryListItem>,
        ingredients: List<IngredientItem>) {
        val ingredientsMap = ingredients.associateBy { it.name }
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        for (groceryItem in groceryItems) {
            val ingredient = ingredientsMap[groceryItem.name]
            val imageUrl = ingredient?.imageUrl ?: ""

            val existing = repository.getByName(groceryItem.name)

            if (existing != null) {
                val updatedFridgeItem = existing.copy(
                    quantity = existing.quantity + groceryItem.quantity
                )
                repository.update(updatedFridgeItem)
            } else {
                val newFridgeItem = FridgeItemEntity(
                    name = groceryItem.name,
                    dateBought = currentDate,
                    quantity = groceryItem.quantity,
                    imageUrl = imageUrl
                )

                repository.insert(newFridgeItem)
            }
        }

        _fridgeItems.value = repository.getAll().map { it.toFridgeItem() }
    }

    fun updateQuantity(name: String, newQuantity: Int) {
        viewModelScope.launch {
            val existing = repository.getByName(name)
            if (existing != null) {
                val updated = existing.copy(quantity = newQuantity)
                repository.update(updated)
                _fridgeItems.value = repository.getAll().map { it.toFridgeItem() }
            }
        }
    }

    fun clearAll() {
        viewModelScope.launch {
            repository.clearAll()
            _fridgeItems.value = emptyList()
        }
    }
}