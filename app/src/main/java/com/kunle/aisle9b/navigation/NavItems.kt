package com.kunle.aisle9b.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    var name: String,
    val route: String,
    val icon: ImageVector,
    var badgeCount: Int = 0
)

data class ModalNavItem(
    var name: String,
    val route: String,
    val icon: ImageVector
)

val navDrawerList = listOf(
    ModalNavItem(
        name = "Settings",
        route = GroceryScreens.SettingsScreen.name,
        icon = Icons.Filled.Settings,
    ),
    ModalNavItem(
        name = "Recipes",
        route = GroceryScreens.RecipeScreen.name,
        icon = Icons.Filled.MenuBook,
    )
)