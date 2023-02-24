package com.kunle.aisle9b.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Surface
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ListScreen(shoppingViewModel: ShoppingViewModel) {
    Surface(modifier = Modifier.fillMaxSize(1f)) {
        Column {
            Spacer(modifier = Modifier.height(10.dp))
            TextField(value = , onValueChange = )
        }
    }
}


@Preview
@Composable
fun MyPreview() {
    
}