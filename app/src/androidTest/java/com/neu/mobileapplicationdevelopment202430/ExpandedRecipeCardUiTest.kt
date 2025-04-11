package com.neu.mobileapplicationdevelopment202430

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.neu.mobileapplicationdevelopment202430.model.FridgeItem
import com.neu.mobileapplicationdevelopment202430.model.RecipeItem
import com.neu.mobileapplicationdevelopment202430.model.ReminderItem
import com.neu.mobileapplicationdevelopment202430.view.ExpandedRecipeCard
import com.neu.mobileapplicationdevelopment202430.view.FridgeItemCard
import com.neu.mobileapplicationdevelopment202430.view.ReminderItemCard
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExpandedRecipeCardUiTest {
    @get: Rule
    val composeTestRule = createComposeRule()

    var back = false
    private val testRecipe = RecipeItem(
        name = "Carbonara",
        description = "Savory egg pasta",
        ingredients = "Egg, Cheese, Pasta, Bacon",
        imageUrl = "https://pngimg.com/uploads/egg/small/egg_PNG13.png"
    )

    @Before
    fun setUp() {
        composeTestRule.setContent {
            MaterialTheme {
                ExpandedRecipeCard(recipe = testRecipe, onBack = { back = true })
            }
        }
    }

    @Test
    fun testCardElementsExist() {
        composeTestRule.onNodeWithTag("expandedRecipeCard").assertExists()
        composeTestRule.onNodeWithTag("topBar").assertIsDisplayed()
        composeTestRule.onNodeWithTag("image").assertIsDisplayed()
        composeTestRule.onNodeWithTag("name").assert(hasText("Carbonara"))
        composeTestRule.onNodeWithTag("ingredientsHeader").assert(hasText("Ingredients:"))
    }

    @Test
    fun testIngredientsList() {
        val ingredients = composeTestRule.onAllNodesWithTag("ingredient")
        assert(ingredients.fetchSemanticsNodes().isNotEmpty())
    }
}