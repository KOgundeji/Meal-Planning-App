package com.kunle.aisle9b.models.tabs

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

data class TabItem (
    val title: String,
    val screen: @Composable () -> Unit
)
