package com.neu.mobileapplicationdevelopment202430.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neu.mobileapplicationdevelopment202430.model.FoodRepository
import com.neu.mobileapplicationdevelopment202430.model.FridgeEntity
import com.neu.mobileapplicationdevelopment202430.model.FridgeItem
import com.neu.mobileapplicationdevelopment202430.model.GroceryListItem
import com.neu.mobileapplicationdevelopment202430.model.IngredientItem
import com.neu.mobileapplicationdevelopment202430.model.RecipeItem
import com.neu.mobileapplicationdevelopment202430.model.toFridgeItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FridgeVM(private val repository: FoodRepository, private val userId : Int, private val groceryVM: GroceryVM) : ViewModel() {
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
            delay(500)
            try {
                val apiUserInformation = repository.getUserInformationFromApi(userId)
                if (apiUserInformation != null) {
                    val fridgeItems = apiUserInformation.fridge_items.map { it.toFridgeItem() }
                    repository.saveFridgeItemsToDatabase(fridgeItems)
                    withContext(Dispatchers.Main) {
                        _fridgeItems.value = fridgeItems
                    }
                }
            } catch (e: Exception) {
//                Log.e("FridgeVM", "FOUND AN ERROR!: ${e.message}")

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

    fun updateItemQuantity(userId: Int, name: String, newQuantity: Int) {
        val updatedList = _fridgeItems.value?.map {
            if (it.name == name) it.copy(quantity = newQuantity) else it
        } ?: return

        _fridgeItems.value = updatedList

        viewModelScope.launch {
            repository.updateFridgeItemQuantity(userId, name, newQuantity)
        }
    }

    suspend fun addToOrUpdateGroceryList(
        groceryItems: List<GroceryListItem>,
        ingredients: List<IngredientItem>) {
        val ingredientsMap = ingredients.associateBy { it.name }
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

//        Log.e("FridgeVM", "In add or update method")

        for (groceryItem in groceryItems) {
            val ingredient = ingredientsMap[groceryItem.name]
            val imageUrl = ingredient?.imageUrl ?: ""

            val existing = repository.getByName(groceryItem.name)
//            Log.e("FridgeVM", "existing? ${existing}")

            if (existing != null) {
                val newQuantity = existing.quantity + groceryItem.quantity
                Log.e("FridgeVM", "updating")
                repository.updateFridgeItemQuantity(userId, groceryItem.name, newQuantity)
            } else {
                Log.e("FridgeVM", "adding")
                val newFridgeItem = FridgeItem(
                    name = groceryItem.name,
                    dateBought = currentDate,
                    quantity = groceryItem.quantity,
                    imageUrl = imageUrl
                )
                repository.addFridgeItem(userId, newFridgeItem)
            }

            groceryVM.deleteGroceryItem(userId, groceryItem)
        }

        _fridgeItems.value = repository.getFridgeItemsFromDatabase()
    }
}