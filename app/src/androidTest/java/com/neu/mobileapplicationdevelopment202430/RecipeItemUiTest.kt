package com.neu.mobileapplicationdevelopment202430

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.neu.mobileapplicationdevelopment202430.model.RecipeItem
import com.neu.mobileapplicationdevelopment202430.model.ReminderItem
import com.neu.mobileapplicationdevelopment202430.view.RecipeItemCard
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RecipeItemUiTest {
    @get: Rule
    val composeTestRule = createComposeRule()

    private lateinit var testRecipe: RecipeItem
    var clicked = false

    @Before
    fun setUp() {
        testRecipe = RecipeItem(
            name = "Carbonara",
            description = "Savory egg pasta",
            ingredients = "Egg, Cheese, Pasta, Bacon",
            imageUrl = "https://pngimg.com/uploads/egg/small/egg_PNG13.png"
        )

        composeTestRule.setContent {
            MaterialTheme {
                RecipeItemCard(item = testRecipe, onReadMore = { clicked = true })
            }
        }
    }

    @Test
    fun testNameExists() {
        composeTestRule.onNodeWithText(testRecipe.name).assertIsDisplayed()
    }

    @Test
    fun testDescriptionExists() {
        composeTestRule.onNodeWithText(testRecipe.description).assertIsDisplayed()
    }

    @Test
    fun testCardExists() {
        composeTestRule.onNodeWithTag("recipeCard").assertExists()
    }

    @Test
    fun testImageExists() {
        composeTestRule.onNodeWithTag("image").assertExists()
    }

    @Test
    fun testReadMoreExists() {
        composeTestRule.onNodeWithTag("readMoreButton").assertExists()

    }

    @Test
    fun testReadMoreFunctionality() {
        composeTestRule.onNodeWithTag("readMoreButton").performClick()
        assert(clicked)
    }

}