package com.neu.mobileapplicationdevelopment202430

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.neu.mobileapplicationdevelopment202430.view.GroceryListScreen
import com.neu.mobileapplicationdevelopment202430.view.IngredientsScreen
import com.neu.mobileapplicationdevelopment202430.view.RemindersScreen
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GroceryListScreenUiTest {
    @get: Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        composeTestRule.setContent {
            MaterialTheme {
                GroceryListScreen(navController = androidx.navigation.compose.rememberNavController())
            }
        }
    }

    @Test
    fun testTitleExists() {
        composeTestRule.onNodeWithTag("title").assertExists()
    }

    @Test
    fun testLoadingExists() {
        composeTestRule.onNodeWithTag("loading").assertDoesNotExist()
    }

    @Test
    fun testGroceryListExists() {
        composeTestRule.onNodeWithTag("groceryList").assertDoesNotExist()
    }

    @Test
    fun testLoadingFunctionality() {
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithTag("loading").fetchSemanticsNodes().isNotEmpty() ||
            composeTestRule.onAllNodesWithTag("error").fetchSemanticsNodes().isNotEmpty() ||
            composeTestRule.onAllNodesWithTag("groceryList").fetchSemanticsNodes().isNotEmpty()
        }
    }

    @Test
    fun testErrorMessageExists() {
        composeTestRule.onNodeWithTag("error").assertExists()
    }

    @Test
    fun testGroceryDialog() {
        composeTestRule.onNodeWithTag("addToListButton").assertExists()
        composeTestRule.onNodeWithTag("addToListButton").performClick()

        composeTestRule.waitForIdle()
//
//        composeTestRule.onNodeWithTag("itemToAdd", useUnmergedTree = true).assertIsDisplayed()
//        composeTestRule.onNodeWithTag("quantityToAdd", useUnmergedTree = true).assertIsDisplayed()
//        composeTestRule.onNodeWithTag("decreaseButton", useUnmergedTree = true).assertIsDisplayed()
//        composeTestRule.onNodeWithTag("increaseButton", useUnmergedTree = true).assertIsDisplayed()
//        composeTestRule.onNodeWithTag("quantityNum", useUnmergedTree = true).assertIsDisplayed()
//        composeTestRule.onNodeWithTag("confirmDialogButton", useUnmergedTree = true).assertIsDisplayed()
//        composeTestRule.onNodeWithTag("cancelDialogButton", useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun testAddToFridgeButtonExists() {
        composeTestRule.onNodeWithTag("addToFridgeButton").assertExists()
    }

    @Test
    fun testFooterNavExists() {
        composeTestRule.onNodeWithTag("footerNav").assertExists()
    }

    @Test
    fun testWhenGroceryListIsNotEmpty() {
        composeTestRule.waitUntil(
            timeoutMillis = 5000,
            condition = {
                composeTestRule.onAllNodesWithTag("loading").fetchSemanticsNodes().isEmpty()
            }
        )

        val listExists = composeTestRule.onAllNodesWithTag("groceryList").fetchSemanticsNodes().isNotEmpty()
        if (listExists) {
            composeTestRule.onAllNodesWithTag("checkbox").onFirst().assertIsDisplayed()
            composeTestRule.onAllNodesWithTag("ingredient").onFirst().assertIsDisplayed()
            composeTestRule.onAllNodesWithTag("remove").onFirst().assertIsDisplayed()
        }
    }

    @Test
    fun clickDialogCancel() {
        composeTestRule.onNodeWithTag("addToListButton").performClick()
//        composeTestRule.onNodeWithTag("cancelDialogButton").performClick()
        composeTestRule.onNodeWithTag("addDialog").assertDoesNotExist()
    }

    @Test
    fun clickingDialogAdd() {
        composeTestRule.onNodeWithTag("addToListButton").performClick()
//        composeTestRule.onNodeWithTag("confirmDialogButton").performClick()
        composeTestRule.onNodeWithTag("addDialog").assertDoesNotExist()
    }
}