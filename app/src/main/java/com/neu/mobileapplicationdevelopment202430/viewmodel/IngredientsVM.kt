package com.neu.mobileapplicationdevelopment202430.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.MutableLiveData
import com.google.gson.reflect.TypeToken
import com.google.gson.Gson
import com.neu.mobileapplicationdevelopment202430.model.IngredientItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import android.util.Log
import kotlinx.coroutines.withContext
import com.neu.mobileapplicationdevelopment202430.model.FoodRepository
import kotlinx.coroutines.CoroutineDispatcher

class IngredientsVM(private val repository: FoodRepository,
                    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
                    private val mainDispatcher: CoroutineDispatcher = Dispatchers.Main) : ViewModel() {
    private val _ingredients = MutableLiveData<List<IngredientItem>?>(emptyList())
    val ingredients: LiveData<List<IngredientItem>?> get() = _ingredients
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun loadIngredients() {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch(ioDispatcher) {
            try {
                val apiIngredients = repository.getIngredientsFromApi()
                if (apiIngredients.isNotEmpty()) {
                    repository.saveIngredientsToDatabase(apiIngredients)
                    withContext(mainDispatcher) {
                        _ingredients.value = apiIngredients
                    }
                }
            } catch (e: Exception) {
                //Log.e("IngredientsVM", "FOUND AN ERROR!: ${e.message}")

                val storedIngredients = repository.getIngredientsFromDatabase()
                withContext(mainDispatcher) {
                    if (!storedIngredients.isNullOrEmpty()) {
                        _ingredients.value = storedIngredients
                    } else {
                        // should we show the error message?
                        _errorMessage.value = "Ingredients unavailable right now"
                    }
                }
            } finally {
                withContext(mainDispatcher) {
                    _isLoading.value = false
                }
            }
        }
    }
}
