package com.kunle.aisle9b

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.kunle.aisle9b.models.SettingsEnum
import com.kunle.aisle9b.navigation.Aisle9Navigation
import com.kunle.aisle9b.navigation.BottomNavigationBar9
import com.kunle.aisle9b.navigation.GroceryScreens
import com.kunle.aisle9b.screens.SharedVM
import com.kunle.aisle9b.screens.customLists.CustomListButtonBar
import com.kunle.aisle9b.screens.meals.MealButtonBar
import com.kunle.aisle9b.ui.theme.Aisle9bTheme
import com.kunle.aisle9b.util.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val sharedVM: SharedVM by viewModels()
            ShoppingApp(sharedVM = sharedVM)
        }
    }
}

@Composable
fun ShoppingApp(sharedVM: SharedVM) {
    val navController = rememberNavController()
    var topBar by remember { mutableStateOf(TopBarOptions.Default) }
    var multiFloatingState by remember { mutableStateOf(MultiFloatingState.Collapsed) }
    var source by remember { mutableStateOf(GroceryScreens.GroceryListScreen) }

    val settings = sharedVM.settingsList.collectAsState().value

    sharedVM.darkModeSetting.value = settings.firstOrNull() {
        it.settingsName == SettingsEnum.DarkMode.name
    }?.value ?: isSystemInDarkTheme()

    sharedVM.categoriesOnSetting.value = settings.firstOrNull {
        it.settingsName == SettingsEnum.Categories.name
    }?.value ?: false

    sharedVM.keepScreenOnSetting.value = settings.firstOrNull() {
        it.settingsName == SettingsEnum.ScreenPermOn.name
    }?.value ?: false

    Aisle9bTheme(darkTheme = sharedVM.darkModeSetting.value) {
        Scaffold(
            topBar = {
                when (topBar) {
                    TopBarOptions.Back ->
                        BackTopAppBar(
                            source = source
                        ) {
                            when (source) {
                                GroceryScreens.MealScreen ->
                                    sharedVM.mealButtonBar.value = MealButtonBar.Default
                                GroceryScreens.CustomListScreen ->
                                    sharedVM.customListButtonBar.value =
                                        CustomListButtonBar.Default
                                else ->
                                    navController.popBackStack()
                            }
                            topBar = TopBarOptions.Default
                        }
                    TopBarOptions.Default ->
                        DefaultTopAppBar(
                            navController = navController,
                            source = source
                        )
                }
            },
            floatingActionButton = {
                when (source) {
                    GroceryScreens.CustomListScreen ->
                        ExpandingFAB(
                            onAddClick = { navController.navigate(GroceryScreens.AddCustomListScreen.name) },
                            onTransferClick = {
                                sharedVM.customListButtonBar.value =
                                    CustomListButtonBar.Transfer
                                topBar = TopBarOptions.Back
                            },
                            onDeleteClick = {
                                sharedVM.customListButtonBar.value = CustomListButtonBar.Delete
                                topBar = TopBarOptions.Back
                            },
                            multiFloatingState = multiFloatingState,
                            onMultiFabStateChange = { multiFloatingState = it }
                        )
                    GroceryScreens.MealScreen ->
                        ExpandingFAB(
                            onAddClick = { navController.navigate(GroceryScreens.AddMealsScreenTEST.name) },
                            onTransferClick = {
                                sharedVM.mealButtonBar.value = MealButtonBar.Transfer
                                topBar = TopBarOptions.Back
                            },
                            onDeleteClick = {
                                sharedVM.mealButtonBar.value = MealButtonBar.Delete
                                topBar = TopBarOptions.Back
                            },
                            multiFloatingState = multiFloatingState,
                            onMultiFabStateChange = { multiFloatingState = it }
                        )
                    GroceryScreens.AddMealsScreenTEST ->
                        SaveFAB { sharedVM.saveCreatedMealonFABClick() }
                    GroceryScreens.RecipeDetailsScreen ->
                        SaveFAB {
                            sharedVM.saveAPIMealonFABClick()
                        }
                    else -> {}

                }
            },
            bottomBar = {
                BottomNavigationBar9(
                    mealsName = "Meals (${sharedVM.numOfMeals.collectAsState().value})",
                    navController = navController,
                    badgeCount = sharedVM.groceryBadgeCount.collectAsState().value,
                    onItemClick = {
                        navController.navigate(it.route)
                    })
            })
        {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Aisle9Navigation(
                    modifier = Modifier.padding(it),
                    source = { screen -> source = screen },
                    topBar = { top -> topBar = top },
                    navController = navController,
                    sharedVM = sharedVM,
                )
            }
        }
    }
}


enum class TopBarOptions {
    Default,
    Back;
}

enum class MultiFloatingState {
    Expanded,
    Collapsed;
}
