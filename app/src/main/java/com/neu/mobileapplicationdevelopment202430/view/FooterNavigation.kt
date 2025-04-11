package com.neu.mobileapplicationdevelopment202430.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.neu.mobileapplicationdevelopment202430.R
import com.neu.mobileapplicationdevelopment202430.navigation.NavigationItem

@Composable
fun FooterNavigation(navController: NavHostController, modifier: Modifier = Modifier) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentSelection = navBackStackEntry?.destination?.route
    val iconSelectedColor = colorResource(id = R.color.purple_500)

    BottomAppBar(
        backgroundColor = Color.White,
        contentColor = Color.Black,
        modifier = modifier.height(56.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                modifier = Modifier.testTag("nav_grocery"),
                onClick = {navController.navigate(NavigationItem.GroceryList.route)}) {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = "Grocery List",
                    tint = if (currentSelection == NavigationItem.GroceryList.route) iconSelectedColor else Color.Black
                )
            }
            IconButton(
                modifier = Modifier.testTag("nav_reminders"),
                onClick = {navController.navigate(NavigationItem.Reminders.route)}) {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = "Reminders",
                    tint = if (currentSelection == NavigationItem.Reminders.route) iconSelectedColor else Color.Black
                )
            }
            IconButton(
                modifier = Modifier.testTag("nav_fridge"),
                onClick = {navController.navigate(NavigationItem.Fridge.route)}) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "What's in my Fridge",
                    tint = if (currentSelection == NavigationItem.Fridge.route) iconSelectedColor else Color.Black
                )
            }
            IconButton(
                modifier = Modifier.testTag("nav_ingredients"),
                onClick = {navController.navigate(NavigationItem.Ingredients.route)}) {
                Icon(
                    imageVector = Icons.Default.Apps,
                    contentDescription = "Ingredients",
                    tint = if (currentSelection == NavigationItem.Ingredients.route) iconSelectedColor else Color.Black
                )
            }
            IconButton(
                modifier = Modifier.testTag("nav_recipes"),
                onClick = {navController.navigate(NavigationItem.Recipes.route)}) {
                Icon(
                    imageVector = Icons.Default.Book,
                    contentDescription = "Recipes",
                    tint = if (currentSelection == NavigationItem.Recipes.route) iconSelectedColor else Color.Black
                )
            }
        }
    }
}
