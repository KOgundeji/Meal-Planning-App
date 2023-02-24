package com.kunle.aisle9b

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.kunle.aisle9b.screens.ListScreen
import com.kunle.aisle9b.screens.ShoppingViewModel
import com.kunle.aisle9b.ui.theme.Aisle9bTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Aisle9bTheme {
                val shoppingViewModel: ShoppingViewModel by viewModels()
                Aisle9App(shoppingViewModel = shoppingViewModel)
            }
        }
    }
}

@Composable
private fun Aisle9App(shoppingViewModel: ShoppingViewModel) {
    ListScreen(shoppingViewModel = shoppingViewModel)
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
}