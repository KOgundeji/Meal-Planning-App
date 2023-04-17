package com.kunle.aisle9b

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.kunle.aisle9b.data.addFakeToDatabase
import com.kunle.aisle9b.data.sampleFoodData
import com.kunle.aisle9b.models.AppSetting
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


    shoppingViewModel.groceryBadgeCount.value = shoppingViewModel.groceryList.collectAsState().value.size
    shoppingViewModel.screenList[2].name = "Meals (${shoppingViewModel.mealList.collectAsState().value.size})"

//    addFakeToDatabase(list = sampleFoodData,viewModel = shoppingViewModel)
    shoppingViewModel.darkModeSetting.value = shoppingViewModel.settingsList.collectAsState()
        .value.firstOrNull() {
            it.settingsName == AppSetting.DarkMode.name
        }?.value ?: isSystemInDarkTheme()

    Aisle9bTheme(darkTheme = shoppingViewModel.darkModeSetting.value) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = screenHeader,
                                fontWeight = FontWeight.Bold,
                                fontSize = 26.sp
                            )
                            AdditionalScreenOptions(navController = navController)
                        }
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
                    shoppingViewModel = shoppingViewModel,
                    badgeCount = shoppingViewModel.groceryBadgeCount.value,
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
