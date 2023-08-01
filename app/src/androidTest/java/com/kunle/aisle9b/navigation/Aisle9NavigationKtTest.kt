package com.kunle.aisle9b.navigation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.navigation.testing.TestNavHostController
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class Aisle9NavigationKtTest {

    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var navController: TestNavHostController

    @Before
    fun setUp() {
//        composeRule.setContent {
//            navController = TestNavHostController(LocalContext.current)
//            navController.navigatorProvider.addNavigator(ComposeNavigator())
//            Aisle9bTheme {
//                Aisle9Navigation(
//                    generalVM = GeneralVM(),
//                    navController = navController,
//                    source = {},
//                    topBar = {}
//                )
//            }
//        }
    }

    @Test
    fun verifyStartScreen() {
        composeRule.onNodeWithContentDescription("Grocery Screen").assertIsDisplayed()
    }

    @Test
    fun navigateToCustomListScreen_onClick() {

    }
}