package com.neu.mobileapplicationdevelopment202430

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import org.junit.Rule
import org.junit.Test
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.neu.mobileapplicationdevelopment202430.view.RecipesScreen
import org.junit.Before
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RecipesScreenUiTest {
    @get: Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        composeTestRule.setContent {
            MaterialTheme {
                RecipesScreen(navController = rememberNavController())
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
    fun testSearchBarAfterLoading() {
        //composeTestRule.onNodeWithTag("loading").assertExists()

        composeTestRule.waitUntil(
            timeoutMillis = 5000,
            condition = {
                composeTestRule.onAllNodesWithTag("loading").fetchSemanticsNodes().isEmpty()
            }
        )

        val searchExists = composeTestRule.onAllNodesWithTag("searchBar").fetchSemanticsNodes().isNotEmpty()
        if (searchExists) {
            val searchBar = composeTestRule.onNodeWithTag("searchBar")
            searchBar.assertExists()
            searchBar.performTextInput("pasta")
            searchBar.assertTextContains("pasta")
        }
    }

    @Test
    fun testConditionalAppearsAfterLoading() {
        //composeTestRule.onNodeWithTag("loading").assertExists()

        composeTestRule.waitUntil(
            timeoutMillis = 5000,
            condition = {
                composeTestRule.onAllNodesWithTag("loading").fetchSemanticsNodes().isEmpty()
            }
        )

        val listExists = composeTestRule.onAllNodesWithTag("recipesList").fetchSemanticsNodes().isNotEmpty()
        val errorExists = composeTestRule.onAllNodesWithTag("error").fetchSemanticsNodes().isNotEmpty()
        val emptyExists = composeTestRule.onAllNodesWithTag("emptyRecipes").fetchSemanticsNodes().isNotEmpty()
        val searchExists = composeTestRule.onAllNodesWithTag("searchBar").fetchSemanticsNodes().isNotEmpty()

        assert(listExists || errorExists || emptyExists || searchExists)
    }

    @Test
    fun testRecipeCardAppearsWhenListIsNotEmpty() {
        composeTestRule.waitUntil(
            timeoutMillis = 5000,
            condition = {
                composeTestRule.onAllNodesWithTag("loading").fetchSemanticsNodes().isEmpty()
            }
        )

        val listExists = composeTestRule.onAllNodesWithTag("recipesList").fetchSemanticsNodes().isNotEmpty()
        if (listExists) {
            val cards = composeTestRule.onAllNodesWithTag("recipeCard")
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
            composeTestRule.onNodeWithText("Recipes unavailable right now").assertExists()
        }
    }

    @Test
    fun testFooterNavigationExists() {
        composeTestRule.onNodeWithTag("footerNav").assertExists()
    }
}