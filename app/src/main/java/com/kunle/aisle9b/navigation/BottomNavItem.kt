package com.kunle.aisle9b.navigation

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    var name: String,
    val route: String,
    val icon: ImageVector,
    var badgeCount: Int = 0
)
