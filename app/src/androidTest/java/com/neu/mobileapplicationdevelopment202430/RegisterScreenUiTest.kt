package com.neu.mobileapplicationdevelopment202430

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import com.neu.mobileapplicationdevelopment202430.view.RegisterScreen
import org.junit.Rule
import org.junit.Test
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.neu.mobileapplicationdevelopment202430.view.RemindersScreen
import org.junit.Before
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegisterScreenUiTest {
    @get: Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        composeTestRule.setContent {
            MaterialTheme {
                RegisterScreen(navController = rememberNavController())
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
    fun testUsernameAndPasswordTextFields() {
        composeTestRule.onNodeWithTag("username").assertExists()
        composeTestRule.onNodeWithTag("password").assertExists()

        val usernameInput = composeTestRule.onNodeWithTag("username")
        val passwordInput = composeTestRule.onNodeWithTag("password")

        usernameInput.performTextInput("test123")
        passwordInput.performTextInput("test123")

        usernameInput.assertTextContains("test123")
        passwordInput.assertTextContains("•••••••")
    }

    @Test
    fun testSignupButton() {
        composeTestRule.onNodeWithTag("signupButton").assertExists().assertIsEnabled().performClick()
    }

    @Test
    fun testLoginText() {
        composeTestRule.onNodeWithTag("login").assertExists().assertHasClickAction()
    }

    @Test
    fun testRegisterButtonClicking() {
        composeTestRule.onNodeWithTag("username").performTextInput("admin")
        composeTestRule.onNodeWithTag("password").performTextInput("admin")

        composeTestRule.onNodeWithTag("signupButton").performClick()

        composeTestRule.waitUntil(timeoutMillis = 3000) {
            composeTestRule.onAllNodesWithTag("loading").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.waitUntil(timeoutMillis = 7000) {
            composeTestRule.onAllNodesWithTag("loading").fetchSemanticsNodes().isEmpty()
        }
    }
}