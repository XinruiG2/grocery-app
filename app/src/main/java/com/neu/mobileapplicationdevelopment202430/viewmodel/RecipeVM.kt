package com.neu.mobileapplicationdevelopment202430.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.MutableLiveData
import com.google.gson.reflect.TypeToken
import com.google.gson.Gson
import com.neu.mobileapplicationdevelopment202430.model.RecipeItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import android.util.Log
import kotlinx.coroutines.withContext
import com.neu.mobileapplicationdevelopment202430.model.FoodRepository

class RecipeVM(private val repository: FoodRepository) : ViewModel() {
    private val _recipes = MutableLiveData<List<RecipeItem>?>(emptyList())
    val recipes: LiveData<List<RecipeItem>?> get() = _recipes
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun loadRecipes() {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val apiRecipes = repository.getRecipesFromApi()
                if (apiRecipes.isNotEmpty()) {
                    repository.saveRecipesToDatabase(apiRecipes)
                    withContext(Dispatchers.Main) {
                        _recipes.value = apiRecipes
                    }
                }
            } catch (e: Exception) {
                //Log.e("RecipesVM", "FOUND AN ERROR!: ${e.message}")

                val storedRecipes = repository.getRecipesFromDatabase()
                withContext(Dispatchers.Main) {
                    if (!storedRecipes.isNullOrEmpty()) {
                        _recipes.value = storedRecipes
                    } else {
                        // should we show the error message?
                        _errorMessage.value = "Recipes unavailable right now"
                    }
                }
            } finally {
                withContext(Dispatchers.Main) {
                    _isLoading.value = false
                }
            }
        }
    }
}
