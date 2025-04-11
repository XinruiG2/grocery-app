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
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.neu.mobileapplicationdevelopment202430.model.IngredientItem
import com.neu.mobileapplicationdevelopment202430.model.ReminderItem
import com.neu.mobileapplicationdevelopment202430.view.IngredientItemCard
import com.neu.mobileapplicationdevelopment202430.view.ReminderItemCard
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class IngredientItemUiTest {
    @get: Rule
    val composeTestRule = createComposeRule()

    private lateinit var testIngredient: IngredientItem

    @Before
    fun setUp() {
        testIngredient = IngredientItem(
            name = "Egg",
            numDaysGoodFor = 7,
            numRecipesUsedIn = 2,
            imageUrl = "https://pngimg.com/uploads/egg/small/egg_PNG13.png"
        )

        composeTestRule.setContent {
            MaterialTheme {
                IngredientItemCard(item = testIngredient)
            }
        }
    }

    @Test
    fun testNameExists() {
        composeTestRule.onNodeWithText(testIngredient.name).assertIsDisplayed()
    }

    @Test
    fun testExpirationTextExists() {
        val expirationText = "Good for: ${testIngredient.numDaysGoodFor} days"
        composeTestRule.onNodeWithText(expirationText).assertIsDisplayed()
    }

    @Test
    fun testRecipesTextExists() {
        val recipesText = "Used in: ${testIngredient.numRecipesUsedIn} recipes"
        composeTestRule.onNodeWithText(recipesText).assertIsDisplayed()
    }

    @Test
    fun testCardExists() {
        composeTestRule.onNodeWithTag("ingredientCard").assertExists()
    }

    @Test
    fun testImageExists() {
        composeTestRule.onNodeWithTag("image").assertExists()
    }
}