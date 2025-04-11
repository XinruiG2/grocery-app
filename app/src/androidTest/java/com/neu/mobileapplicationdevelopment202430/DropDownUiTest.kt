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
import com.neu.mobileapplicationdevelopment202430.view.DropDown
import com.neu.mobileapplicationdevelopment202430.view.ExpandedRecipeCard
import com.neu.mobileapplicationdevelopment202430.view.FridgeItemCard
import com.neu.mobileapplicationdevelopment202430.view.ReminderItemCard
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DropDownUiTest {
    @get: Rule
    val composeTestRule = createComposeRule()
    private val options = listOf("Egg", "Apple", "Milk")

    @Before
    fun setUp() {
        composeTestRule.setContent {
            MaterialTheme {
                DropDown(
                    options = options,
                    selectedOption = "Apple",
                    onOptionSelected = {}
                )
            }
        }
    }

    @Test
    fun testInitialState() {
        composeTestRule.onNodeWithText("Apple").assertIsDisplayed()
        composeTestRule.onNodeWithText("Egg").assertDoesNotExist()
        composeTestRule.onNodeWithText("Milk").assertDoesNotExist()
    }

    @Test
    fun testDropdownExpand() {
        composeTestRule.onNodeWithContentDescription("Dropdown Symbol").performClick()
        composeTestRule.onNodeWithText("Egg").assertIsDisplayed()
        composeTestRule.onNodeWithText("Milk").assertIsDisplayed()
        composeTestRule.onNodeWithText("Egg").performClick()
        composeTestRule.onNodeWithText("Milk").assertDoesNotExist()
    }
}