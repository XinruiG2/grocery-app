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

class IngredientsVM(private val repository: FoodRepository) : ViewModel() {
    private val _ingredients = MutableLiveData<List<IngredientItem>?>(emptyList())
    val ingredients: LiveData<List<IngredientItem>?> get() = _ingredients
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun loadProducts() {
        _isLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val apiIngredients = repository.getIngredientsFromApi()
                if (apiIngredients.isNotEmpty()) {
                    //save to db
                    withContext(Dispatchers.Main) {
                        _ingredients.value = apiIngredients
                    }
                }
            } catch (e: Exception) {
                Log.e("IngredientsVM", "FOUND AN ERROR!: ${e.message}")
            } finally {
                withContext(Dispatchers.Main) {
                    _isLoading.value = false
                }
            }
        }
    }
}
