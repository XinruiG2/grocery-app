package com.neu.mobileapplicationdevelopment202430.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neu.mobileapplicationdevelopment202430.model.FoodRepository
import com.neu.mobileapplicationdevelopment202430.model.FridgeItem
import com.neu.mobileapplicationdevelopment202430.model.GroceryListItem
import com.neu.mobileapplicationdevelopment202430.model.RecipeItem
import com.neu.mobileapplicationdevelopment202430.model.toFridgeItem
import com.neu.mobileapplicationdevelopment202430.model.toGroceryItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GroceryVM(private val repository: FoodRepository, private val userId : Int) : ViewModel() {
    private val _groceryItems = MutableLiveData<List<GroceryListItem>?>(emptyList())
    val groceryItems: LiveData<List<GroceryListItem>?> get() = _groceryItems
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun loadGroceryItems() {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val apiUserInformation = repository.getUserInformationFromApi(userId)
                if (apiUserInformation != null) {
                    val groceryItems = apiUserInformation.grocery_list.map { it.toGroceryItem() }
                    repository.saveGroceryItemsToDatabase(groceryItems)
                    withContext(Dispatchers.Main) {
                        _groceryItems.value = groceryItems
                    }
                }
            } catch (e: Exception) {
                Log.e("GroceryVM", "FOUND AN ERROR!: ${e.message}")

                val storedGroceryItems = repository.getGroceryItemsFromDatabase()
                withContext(Dispatchers.Main) {
                    if (!storedGroceryItems.isNullOrEmpty()) {
                        _groceryItems.value = storedGroceryItems
                    } else {
                        _errorMessage.value = "Grocery Items unavailable right now"
                    }
                }
            } finally {
                withContext(Dispatchers.Main) {
                    _isLoading.value = false
                }
            }
        }
    }

    fun addGroceryItem(userId: Int, name: String, quantity: Int) {
        val groceryItemToAdd = GroceryListItem(name, quantity)

        viewModelScope.launch {
            val existingItems = repository.getGroceryItemsFromDatabase()
//            repository.addGroceryItem(userId, groceryItemToAdd)
//
//            val updatedGroceryItems = repository.getGroceryItemsFromDatabase()
//            Log.d("Grocery VM", updatedGroceryItems.toString())
//            withContext(Dispatchers.Main) {
//                _groceryItems.value = updatedGroceryItems
//            }
            if (existingItems != null) {
                val existingItem = existingItems.find { it.name == name }
                if (existingItem == null) {
                    repository.addGroceryItem(userId, groceryItemToAdd)

                    val updatedGroceryItems = repository.getGroceryItemsFromDatabase()?.distinctBy { it.name }
                    Log.d("Grocery VM", updatedGroceryItems.toString())
                    withContext(Dispatchers.Main) {
                        _groceryItems.value = updatedGroceryItems
                    }
                } else {
                    val newQuantity = existingItem.quantity + quantity
                    repository.updateGroceryItemQuantity(userId, name, newQuantity)
                    val updatedGroceryItems = repository.getGroceryItemsFromDatabase()?.distinctBy { it.name }
                    withContext(Dispatchers.Main) {
                        _groceryItems.value = updatedGroceryItems
                    }
                }
            } else {
                Log.d("Grocery VM", "Item not found")
            }
        }
    }

    fun deleteGroceryItem(userId: Int, item: GroceryListItem) {
        viewModelScope.launch {
            repository.deleteGroceryItem(userId, item)

            val updatedGroceryItems = repository.getGroceryItemsFromDatabase()?.distinctBy { it.name }
            Log.d("Grocery VM", updatedGroceryItems.toString())
            withContext(Dispatchers.Main) {
                _groceryItems.value = updatedGroceryItems
            }
        }
    }
}