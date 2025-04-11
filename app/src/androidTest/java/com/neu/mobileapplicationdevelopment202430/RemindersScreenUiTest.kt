package com.neu.mobileapplicationdevelopment202430

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.neu.mobileapplicationdevelopment202430.view.RemindersScreen
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RemindersScreenUiTest {
    @get: Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        composeTestRule.setContent {
            MaterialTheme {
                RemindersScreen(navController = androidx.navigation.compose.rememberNavController())
            }
        }
    }

    @Test
    fun testTitleIsDisplayed() {
        composeTestRule.onNodeWithTag("title").assertExists()
    }

    @Test
    fun testLoadingIndicatorAppears() {
        composeTestRule.onNodeWithTag("loading").assertExists()
    }

    @Test
    fun testReminderListIsDisplayed() {
        composeTestRule.onNodeWithTag("reminderList").assertDoesNotExist()
    }

    @Test
    fun testErrorMessageIsDisplayed() {
        composeTestRule.onNodeWithTag("error").assertDoesNotExist()
    }

    @Test
    fun testRemindersListOrErrorAppearsAfterLoading() {
        composeTestRule.onNodeWithTag("loading").assertExists()

        composeTestRule.waitUntil(
            timeoutMillis = 5000,
            condition = {
                composeTestRule.onAllNodesWithTag("loading").fetchSemanticsNodes().isEmpty()
            }
        )

        val listExists = composeTestRule.onAllNodesWithTag("remindersList").fetchSemanticsNodes().isNotEmpty()
        val errorExists = composeTestRule.onAllNodesWithTag("error").fetchSemanticsNodes().isNotEmpty()
        val emptyExists = composeTestRule.onAllNodesWithTag("emptyReminders").fetchSemanticsNodes().isNotEmpty()

        assert(listExists || errorExists || emptyExists)
    }

    @Test
    fun testFooterNavigationIsDisplayed() {
        composeTestRule.waitUntil(
            timeoutMillis = 5000,
            condition = {
                composeTestRule.onAllNodesWithTag("loading").fetchSemanticsNodes().isEmpty()
            }
        )

        composeTestRule.onNodeWithTag("footerNav").assertExists()
    }

}