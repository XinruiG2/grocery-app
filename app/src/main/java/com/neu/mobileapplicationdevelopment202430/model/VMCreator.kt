package com.neu.mobileapplicationdevelopment202430.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.neu.mobileapplicationdevelopment202430.viewmodel.IngredientsVM

class VMCreator(private val repository: FoodRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(IngredientsVM::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return IngredientsVM(repository) as T
        }
        throw IllegalArgumentException("Unknown VM")
    }
}
