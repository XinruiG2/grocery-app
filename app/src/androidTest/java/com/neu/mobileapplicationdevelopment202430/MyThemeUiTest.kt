package com.neu.mobileapplicationdevelopment202430

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.neu.mobileapplicationdevelopment202430.model.RecipeItem
import com.neu.mobileapplicationdevelopment202430.model.ReminderItem
import com.neu.mobileapplicationdevelopment202430.view.MyTheme
import com.neu.mobileapplicationdevelopment202430.view.RecipeItemCard
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MyThemeUiTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testComponentInsideMyTheme() {
        composeTestRule.setContent {
            MyTheme {
                Text("Random", color = Color.Black)
            }
        }

        composeTestRule.onNodeWithText("Random").assertExists()
    }
}