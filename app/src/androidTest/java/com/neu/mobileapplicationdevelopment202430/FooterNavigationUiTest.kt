package com.neu.mobileapplicationdevelopment202430

import android.annotation.SuppressLint
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.neu.mobileapplicationdevelopment202430.model.FridgeItem
import com.neu.mobileapplicationdevelopment202430.model.ReminderItem
import com.neu.mobileapplicationdevelopment202430.navigation.NavigationItem
import com.neu.mobileapplicationdevelopment202430.view.FooterNavigation
import com.neu.mobileapplicationdevelopment202430.view.FridgeItemCard
import com.neu.mobileapplicationdevelopment202430.view.FridgeScreen
import com.neu.mobileapplicationdevelopment202430.view.ReminderItemCard
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FooterNavigationUiTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testIconsDisplayed() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            MaterialTheme {
                FooterNavigation(navController = navController)
            }
        }

        composeTestRule.onNodeWithTag("nav_grocery").assertIsDisplayed()
        composeTestRule.onNodeWithTag("nav_reminders").assertIsDisplayed()
        composeTestRule.onNodeWithTag("nav_fridge").assertIsDisplayed()
        composeTestRule.onNodeWithTag("nav_ingredients").assertIsDisplayed()
        composeTestRule.onNodeWithTag("nav_recipes").assertIsDisplayed()
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Test
    fun testNavigation() {
        lateinit var navController: NavHostController

        composeTestRule.setContent {
            navController = rememberNavController()

            MaterialTheme {
                Scaffold(
                    bottomBar = {
                        FooterNavigation(navController = navController)
                    }
                ) {
                    androidx.navigation.compose.NavHost(
                        navController = navController,
                        startDestination = NavigationItem.Reminders.route
                    ) {
                        composable(NavigationItem.GroceryList.route) { Text("Grocery") }
                        composable(NavigationItem.Reminders.route) { Text("Reminders") }
                        composable(NavigationItem.Fridge.route) { Text("Fridge") }
                        composable(NavigationItem.Ingredients.route) { Text("Ingredients") }
                        composable(NavigationItem.Recipes.route) { Text("Recipes") }
                    }
                }
            }
        }

        composeTestRule.onNodeWithTag("nav_recipes").performClick()
        composeTestRule.runOnIdle {
            assert(navController.currentDestination?.route == NavigationItem.Recipes.route)
        }
    }
}