package com.kunle.aisle9b.screens.groceries

import androidx.activity.compose.setContent
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ApplicationProvider
import com.kunle.aisle9b.MainActivity
import com.kunle.aisle9b.dagger.AppModule
import com.kunle.aisle9b.ui.theme.Aisle9bTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class GroceryScreenKtTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
        composeTestRule.activity.setContent {
            val navController = rememberNavController()
            Aisle9bTheme {
                GroceryScreen(navController = navController)
            }
        }
//        composeTestRule.dumpSemanticNodes()
    }

    @Test
    fun emptyScreenShowsCorrectItemsTest() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        context.resources
        composeTestRule.apply {
            onNodeWithContentDescription("Grocery name").assertIsDisplayed()
            onNodeWithContentDescription("Grocery quantity").assertIsDisplayed()
            onNodeWithContentDescription("Add grocery").assertIsDisplayed()

            onNodeWithText("Your Grocery List is currently empty!").assertExists()
            onNodeWithContentDescription("transfer meals button").assertIsDisplayed()
            onNodeWithContentDescription("transfer saved list button").assertIsDisplayed()
        }

    }

    @Test
    fun transferToMealScreen() {
//        val navController = getNavController()
        composeTestRule.onNodeWithText("Load Saved Grocery List").performClick()

    }

    @Test
    fun addGrocery_inputText_screenDoesntShowTransferButtons() {
        composeTestRule.apply {
            onNodeWithContentDescription("Grocery name").performTextInput("Cheese")
            onNodeWithContentDescription("Grocery quantity").performTextInput("6 oz")
            onNodeWithContentDescription("Add grocery").performClick()

            onNodeWithText("Your Grocery List is currently empty!").assertDoesNotExist()
            onNodeWithContentDescription("transfer meals button").assertIsNotDisplayed()
            onNodeWithContentDescription("transfer saved list button").assertIsNotDisplayed()
        }

    }

    @Test
    fun addGrocery_inputText_outputGroceryItem() {
        composeTestRule.onNodeWithContentDescription("Grocery name").performTextInput("Cheese")
        composeTestRule.onNodeWithContentDescription("Grocery quantity").performTextInput("6 oz")
        composeTestRule.onNodeWithContentDescription("Add grocery").performClick()

        composeTestRule.onNodeWithText("Cheese (6 oz)").assertExists()

    }

//    private fun getNavController(): NavController {
//        return composeRule.activity.findNavController()
//    }
}