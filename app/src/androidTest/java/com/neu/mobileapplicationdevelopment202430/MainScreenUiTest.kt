package com.neu.mobileapplicationdevelopment202430

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.neu.mobileapplicationdevelopment202430.view.MainScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.neu.mobileapplicationdevelopment202430.view.RemindersScreen
import org.junit.Before

@RunWith(AndroidJUnit4::class)
class MainScreenUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        composeTestRule.setContent {
            MainScreen()
        }
    }

    @Test
    fun testMainScreen() {
        composeTestRule.onNodeWithTag("appNavHost").assertExists()
    }
}
