package com.kunle.aisle9b

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kunle.aisle9b.navigation.Aisle9Navigation
import com.kunle.aisle9b.screens.ListScreen
import com.kunle.aisle9b.ui.theme.Aisle9bTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Aisle9App {
                Aisle9Navigation()
            }
        }
    }
}

@Composable
private fun Aisle9App(content: @Composable () -> Unit) {
    Aisle9bTheme {
        content()
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Aisle9App {
        Aisle9Navigation()
    }
}