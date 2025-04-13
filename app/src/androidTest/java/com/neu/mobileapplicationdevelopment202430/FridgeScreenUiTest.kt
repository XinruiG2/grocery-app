package com.neu.mobileapplicationdevelopment202430

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.neu.mobileapplicationdevelopment202430.view.FridgeScreen
import com.neu.mobileapplicationdevelopment202430.view.RemindersScreen
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FridgeScreenUiTest {
    @get: Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        composeTestRule.setContent {
            MaterialTheme {
                FridgeScreen(navController = androidx.navigation.compose.rememberNavController())
            }
        }
    }

    @Test
    fun testTitleExists() {
        composeTestRule.onNodeWithTag("title").assertExists()
    }

    @Test
    fun testLoadingExists() {
        composeTestRule.onNodeWithTag("loading").assertExists()
    }

    @Test
    fun testReminderListExists() {
        composeTestRule.onNodeWithTag("fridgeItems").assertDoesNotExist()
    }

    @Test
    fun testErrorMessageExists() {
        composeTestRule.onNodeWithTag("error").assertDoesNotExist()
    }

    @Test
    fun testLoadingFunctionality() {

        composeTestRule.waitUntil(
            timeoutMillis = 5000,
            condition = {
                composeTestRule.onAllNodesWithTag("loading").fetchSemanticsNodes().isEmpty()
            }
        )

        val listExists = composeTestRule.onAllNodesWithTag("fridgeItems").fetchSemanticsNodes().isNotEmpty()
        val errorExists = composeTestRule.onAllNodesWithTag("error").fetchSemanticsNodes().isNotEmpty()
        val emptyExists = composeTestRule.onAllNodesWithTag("emptyFridge").fetchSemanticsNodes().isNotEmpty()

        assert(listExists || errorExists || emptyExists)
    }

    @Test
    fun testFridgeCardAppearsWhenListIsNotEmpty() {
        composeTestRule.waitUntil(
            timeoutMillis = 5000,
            condition = {
                composeTestRule.onAllNodesWithTag("loading").fetchSemanticsNodes().isEmpty()
            }
        )

        val listExists = composeTestRule.onAllNodesWithTag("fridgeItems").fetchSemanticsNodes().isNotEmpty()
        if (listExists) {
            val cards = composeTestRule.onAllNodesWithTag("fridgeCard")
            assert(cards.fetchSemanticsNodes().isNotEmpty())
        }
    }

    @Test
    fun testErrorMessageText() {
        composeTestRule.waitUntil(
            timeoutMillis = 5000,
            condition = {
                composeTestRule.onAllNodesWithTag("loading").fetchSemanticsNodes().isEmpty()
            }
        )

        val errorExists = composeTestRule.onAllNodesWithTag("error").fetchSemanticsNodes().isNotEmpty()
        if (errorExists) {
            composeTestRule.onNodeWithText("Fridge Items unavailable right now").assertExists()
        }
    }

    @Test
    fun testFooterNavigationExists() {
//        composeTestRule.waitUntil(
//            timeoutMillis = 5000,
//            condition = {
//                composeTestRule.onAllNodesWithTag("loading").fetchSemanticsNodes().isEmpty()
//            }
//        )

        composeTestRule.onNodeWithTag("footerNav").assertExists()
    }

}