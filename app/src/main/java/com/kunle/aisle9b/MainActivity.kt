package com.kunle.aisle9b

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.kunle.aisle9b.data.addFakeToDatabase
import com.kunle.aisle9b.data.sampleFoodData
import com.kunle.aisle9b.navigation.Aisle9Navigation
import com.kunle.aisle9b.navigation.BottomNavigationBar9
import com.kunle.aisle9b.navigation.GroceryScreens
import com.kunle.aisle9b.screens.SharedVM
import com.kunle.aisle9b.ui.theme.Aisle9bTheme
import com.kunle.aisle9b.util.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val shoppingVM: SharedVM by viewModels()
            ShoppingApp(sharedVM = shoppingVM)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingApp(sharedVM: SharedVM) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var topBar by remember { mutableStateOf(TopBarOptions.Default) }
    var multiFloatingState by remember { mutableStateOf(MultiFloatingState.Collapsed) }
    var source by remember { mutableStateOf(GroceryScreens.GroceryListScreen) }

//    val settings = sharedVM.settingsList.collectAsState().value

//    sharedVM.groceryBadgeCount.value = sharedVM.groceryList.collectAsState().value.size

//    sharedVM.darkModeSetting.value = settings.firstOrNull() {
//        it.settingsName == AppSetting.DarkMode.name
//    }?.value ?: isSystemInDarkTheme()
//
//    sharedVM.categoriesOn.value = settings.firstOrNull {
//        it.settingsName == AppSetting.Categories.name
//    }?.value ?: false
//
//    sharedVM.keepScreenOn.value = settings.firstOrNull() {
//        it.settingsName == AppSetting.ScreenPermOn.name
//    }?.value ?: false

    Aisle9bTheme(darkTheme = sharedVM.darkModeSetting.value) {
        Scaffold(
            topBar = {
                when (topBar) {
                    TopBarOptions.BackButton -> {
                        BackTopAppBar(
                            source = source
                        ) {
                            topBar = TopBarOptions.Default
                        }
                    }
                    TopBarOptions.Default -> {
                        DefaultTopAppBar(
                            drawerState = drawerState,
                            source = source
                        )
                    }
                }
            },
            floatingActionButton = {
                FAB(
                    onAddClick = { navController.navigate(GroceryScreens.AddMealsScreen.name) },
                    onTransferClick = {
                        topBar = TopBarOptions.BackButton
                    },
                    onDeleteClick = {
                        topBar = TopBarOptions.BackButton
                    },
                    multiFloatingState = multiFloatingState,
                    onMultiFabStateChange = { multiFloatingState = it }
                )
            },
            bottomBar = {
                BottomNavigationBar9(
                    mealsName = "Meals (${sharedVM.numOfMeals.value})",
                    navController = navController,
                    badgeCount = sharedVM.groceryBadgeCount.value,
                    onItemClick = {
                        navController.navigate(it.route)
                    })
            })
        {
            Aisle9Navigation(
                modifier = Modifier.padding(it),
                source = { screen -> source = screen },
                topBar = {top -> topBar = top },
                navController = navController,
                sharedVM = sharedVM,
            )
        }
    }
}


enum class TopBarOptions {
    Default,
    BackButton;
}

enum class MultiFloatingState {
    Expanded,
    Collapsed;
}
