package com.neu.mobileapplicationdevelopment202430.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.neu.mobileapplicationdevelopment202430.viewmodel.IngredientsVM
import com.neu.mobileapplicationdevelopment202430.viewmodel.LoginVM
import com.neu.mobileapplicationdevelopment202430.viewmodel.RecipeVM
import com.neu.mobileapplicationdevelopment202430.viewmodel.RegisterVM

class IngredientsVMCreator(private val repository: FoodRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(IngredientsVM::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return IngredientsVM(repository) as T
        }
        throw IllegalArgumentException("Unknown VM")
    }
}

class RecipesVMCreator(private val repository: FoodRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeVM::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecipeVM(repository) as T
        }
        throw IllegalArgumentException("Unknown VM")
    }
}

class LoginVMCreator(private val repository: FoodRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginVM::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginVM(repository) as T
        }
        throw IllegalArgumentException("Unknown VM")
    }
}

class RegisterVMCreator(private val repository: FoodRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterVM::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegisterVM(repository) as T
        }
        throw IllegalArgumentException("Unknown VM")
    }
}
