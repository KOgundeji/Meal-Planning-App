package com.kunle.aisle9b.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.lifecycle.ViewModel
import com.kunle.aisle9b.navigation.BottomNavItem
import com.kunle.aisle9b.navigation.GroceryScreens

class ShoppingViewModel : ViewModel() {
    val screenList = listOf(
        BottomNavItem(
            name = "Grocery List",
            route = GroceryScreens.GroceryScreen.name,
            icon = Icons.Filled.List
        ),
        BottomNavItem(
            name = "Meals",
            route = GroceryScreens.MealScreen.name,
            icon = Icons.Filled.Delete
        ),
        BottomNavItem(
            name = "Settings",
            route = GroceryScreens.SettingsScreen.name,
            icon = Icons.Filled.Settings
        )
    )

}