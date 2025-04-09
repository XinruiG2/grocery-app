package com.neu.mobileapplicationdevelopment202430.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neu.mobileapplicationdevelopment202430.model.FoodRepository
import com.neu.mobileapplicationdevelopment202430.model.FridgeItem
import com.neu.mobileapplicationdevelopment202430.model.RecipeItem
import com.neu.mobileapplicationdevelopment202430.model.toFridgeItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FridgeVM(private val repository: FoodRepository, private val userId : Int) : ViewModel() {
    private val _fridgeItems = MutableLiveData<List<FridgeItem>?>(emptyList())
    val fridgeItems: LiveData<List<FridgeItem>?> get() = _fridgeItems
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun loadFridgeItems() {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val apiUserInformation = repository.getUserInformationFromApi(userId)
                if (apiUserInformation != null) {
                    val fridgeItems = apiUserInformation.fridge_items.map { it.toFridgeItem() }
                    withContext(Dispatchers.Main) {
                        _fridgeItems.value = fridgeItems
                    }
                }
            } catch (e: Exception) {
                Log.e("FridgeVM", "FOUND AN ERROR!: ${e.message}")

                val storedFridgeItems = repository.getFridgeItemsFromDatabase()
                withContext(Dispatchers.Main) {
                    if (!storedFridgeItems.isNullOrEmpty()) {
                        _fridgeItems.value = storedFridgeItems
                    } else {
                        _errorMessage.value = "Fridge Items unavailable right now"
                    }
                }
            } finally {
                withContext(Dispatchers.Main) {
                    _isLoading.value = false
                }
            }
        }
    }

    fun updateItemQuantity(name: String, newQuantity: Int) {
        val updatedList = _fridgeItems.value?.map {
            if (it.name == name) it.copy(quantity = newQuantity) else it
        } ?: return

        _fridgeItems.value = updatedList

        viewModelScope.launch {
            repository.updateFridgeItemQuantity(name, newQuantity)
        }
    }
}