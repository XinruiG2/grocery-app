package com.neu.mobileapplicationdevelopment202430.navigation

enum class Screen {
    REGISTER,
    LOGIN,
    GROCERY_LIST,
    INGREDIENTS,
    REMINDERS,
    FRIDGE,
    RECIPES
}

sealed class NavigationItem(val route: String) {
    object Login : NavigationItem(Screen.LOGIN.name)
    object Register : NavigationItem(Screen.REGISTER.name)
    object GroceryList : NavigationItem(Screen.GROCERY_LIST.name)
    object Ingredients : NavigationItem(Screen.INGREDIENTS.name)
    object Reminders : NavigationItem(Screen.REMINDERS.name)
    object Fridge : NavigationItem(Screen.FRIDGE.name)
    object Recipes : NavigationItem(Screen.RECIPES.name)
}