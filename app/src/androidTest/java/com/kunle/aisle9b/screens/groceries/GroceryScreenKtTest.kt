package com.kunle.aisle9b.screens.groceries

import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ApplicationProvider
import com.kunle.aisle9b.dumpSemanticNodes
import com.kunle.aisle9b.repositories.general.FakeGeneralRepository
import com.kunle.aisle9b.repositories.groceries.FakeGroceryRepository
import com.kunle.aisle9b.screens.GeneralVM
import com.kunle.aisle9b.ui.theme.Aisle9bTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GroceryScreenKtTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var generalViewModel: GeneralVM
    private lateinit var groceryViewModel: GroceryVM

    @Before
    fun setUp() {
        generalViewModel = GeneralVM(FakeGeneralRepository())
        groceryViewModel = GroceryVM(FakeGroceryRepository())
        composeTestRule.setContent {
            Aisle9bTheme {
                GroceryScreen(
                    generalVM = generalViewModel,
                    groceryVM = groceryViewModel,
                    navController = rememberNavController(),
                    topBar = { },
                    source = { }
                )
            }
        }
        composeTestRule.dumpSemanticNodes()
    }

    @Test
    fun emptyScreenShowsCorrectItemsTest() {
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