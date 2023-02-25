package com.kunle.aisle9b.navigation

import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kunle.aisle9b.screens.ListScreen
import com.kunle.aisle9b.screens.MealScreen
import com.kunle.aisle9b.screens.SettingsScreen

@Composable
fun Aisle9Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = GroceryScreens.MealScreen.name) {
        composable(route = GroceryScreens.GroceryScreen.name) {
            ListScreen(navController)
        }
        composable(route = GroceryScreens.MealScreen.name) {
            MealScreen()
//            MealScreen(navController = navController)
        }
        composable(route = GroceryScreens.SettingsScreen.name) {
            SettingsScreen()
        }
    }

}