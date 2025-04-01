package com.neu.mobileapplicationdevelopment202430.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.neu.mobileapplicationdevelopment202430.view.FridgeScreen
import com.neu.mobileapplicationdevelopment202430.view.GroceryListScreen
import com.neu.mobileapplicationdevelopment202430.view.IngredientsScreen
import com.neu.mobileapplicationdevelopment202430.view.LoginScreen
import com.neu.mobileapplicationdevelopment202430.view.RecipesScreen
import com.neu.mobileapplicationdevelopment202430.view.RegisterScreen
import com.neu.mobileapplicationdevelopment202430.view.RemindersScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = NavigationItem.Login.route
) {

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(NavigationItem.Login.route) {
            LoginScreen(navController)
        }
        composable(NavigationItem.Register.route) {
            RegisterScreen(navController)
        }
        composable(NavigationItem.GroceryList.route) {
            GroceryListScreen()
        }
        composable(NavigationItem.Ingredients.route) {
            IngredientsScreen()
        }
        composable(NavigationItem.Reminders.route) {
            RemindersScreen()
        }
        composable(NavigationItem.Fridge.route) {
            FridgeScreen()
        }
        composable(NavigationItem.Recipes.route) {
            RecipesScreen()
        }
    }
}
