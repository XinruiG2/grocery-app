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
import com.neu.mobileapplicationdevelopment202430.model.FridgeItem
import com.neu.mobileapplicationdevelopment202430.model.ReminderItem
import com.neu.mobileapplicationdevelopment202430.view.FridgeItemCard
import com.neu.mobileapplicationdevelopment202430.view.ReminderItemCard
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FridgeItemUiTest {
    @get: Rule
    val composeTestRule = createComposeRule()

    private lateinit var testFridgeItem: FridgeItem

    @Before
    fun setUp() {
        testFridgeItem = FridgeItem(
            name = "Egg",
            dateBought = "2025-04-08",
            quantity = 2,
            imageUrl = "https://pngimg.com/uploads/egg/small/egg_PNG13.png"
        )

        composeTestRule.setContent {
            MaterialTheme {
                FridgeItemCard(item = testFridgeItem, updateQuantity = {})
            }
        }
    }

    @Test
    fun testNameExists() {
        composeTestRule.onNodeWithText(testFridgeItem.name).assertIsDisplayed()
    }

    @Test
    fun testDateBoughtTextExists() {
        val dateBoughtText = "Bought on: ${testFridgeItem.dateBought}"
        composeTestRule.onNodeWithText(dateBoughtText).assertIsDisplayed()
    }

    @Test
    fun testCardExists() {
        composeTestRule.onNodeWithTag("fridgeCard").assertExists()
    }

    @Test
    fun testImageExists() {
        composeTestRule.onNodeWithTag("image").assertExists()
    }

    @Test
    fun testInitialQuantity() {
        composeTestRule.onNodeWithText("2").assertIsDisplayed()
    }

    @Test
    fun testIncreaseQuantity() {
        composeTestRule.onNodeWithTag("increaseButton").performClick()
        composeTestRule.onNodeWithText("3").assertIsDisplayed()
    }

    @Test
    fun testDecreaseQuantity() {
        composeTestRule.onNodeWithTag("decreaseButton").performClick()
        composeTestRule.onNodeWithText("1").assertIsDisplayed()
    }

    @Test
    fun testQuantityButtonVisibility() {
        composeTestRule.onNodeWithTag("decreaseButton").assertIsDisplayed()
        composeTestRule.onNodeWithTag("increaseButton").assertIsDisplayed()
    }
}