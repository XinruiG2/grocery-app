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
import com.neu.mobileapplicationdevelopment202430.model.ReminderItem
import com.neu.mobileapplicationdevelopment202430.view.ReminderItemCard
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ReminderItemUiTest {
    @get: Rule
    val composeTestRule = createComposeRule()

    private lateinit var testReminder: ReminderItem

    @Before
    fun setUp() {
        testReminder = ReminderItem(
            name = "Egg",
            imageUrl = "https://pngimg.com/uploads/egg/small/egg_PNG13.png",
            numDaysTilExpired = 7
        )

        composeTestRule.setContent {
            MaterialTheme {
                ReminderItemCard(item = testReminder)
            }
        }
    }

    @Test
    fun testNameExists() {
        composeTestRule.onNodeWithText(testReminder.name).assertIsDisplayed()
    }

    @Test
    fun testExpirationTextExists() {
        val expirationText = "Expires in: ${testReminder.numDaysTilExpired} days"
        composeTestRule.onNodeWithText(expirationText).assertIsDisplayed()
    }

    @Test
    fun testCardExists() {
        composeTestRule.onNodeWithTag("reminderItemCard").assertExists()
    }

    @Test
    fun testImageExists() {
        composeTestRule.onNodeWithTag("reminderImage").assertExists()
    }
}