package com.kunle.aisle9b

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.kunle.aisle9b.navigation.Aisle9Navigation
import com.kunle.aisle9b.navigation.BottomNavigationBar9
import com.kunle.aisle9b.screens.ShoppingViewModel
import com.kunle.aisle9b.ui.theme.Aisle9bTheme
import com.kunle.aisle9b.util.AdditionalScreenOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val shoppingViewModel: ShoppingViewModel by viewModels()
            ShoppingApp(shoppingViewModel = shoppingViewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingApp(shoppingViewModel: ShoppingViewModel) {
    val navController = rememberNavController()
    var screenHeader by remember { mutableStateOf("") }

    //addFakeToDatabase(list = sampleFoodData,viewModel = shoppingViewModel)
    Aisle9bTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = screenHeader) },
                    actions = {
                        AdditionalScreenOptions(navController = navController)
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }, bottomBar = {
                BottomNavigationBar9(
                    items = shoppingViewModel.screenList,
                    navController = navController,
                    onItemClick = {
                        navController.navigate(it.route)
                    })
            }) {
            Aisle9Navigation(navController, shoppingViewModel, Modifier.padding(it)) { headline ->
                screenHeader = headline
            }
        }
    }
}
