package com.kunle.aisle9b

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.kunle.aisle9b.navigation.Aisle9Navigation
import com.kunle.aisle9b.navigation.BottomNavigationBar9
import com.kunle.aisle9b.navigation.GroceryScreens
import com.kunle.aisle9b.screens.GeneralVM
import com.kunle.aisle9b.ui.theme.Aisle9bTheme
import com.kunle.aisle9b.util.AddFAB
import com.kunle.aisle9b.util.BackTopAppBar
import com.kunle.aisle9b.util.DefaultTopAppBar
import com.kunle.aisle9b.util.SaveFAB
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val generalVM: GeneralVM by viewModels()

            ShoppingAppScaffold(navController, generalVM) { padVal ->
                Aisle9Navigation(
                    modifier = Modifier.padding(padVal),
                    navController = navController,
                    generalVM = generalVM
                )
            }
        }
    }
}

@Composable
fun ShoppingAppScaffold(
    navController: NavController,
    generalVM: GeneralVM,
    appNavigation: @Composable (PaddingValues) -> Unit
) {
    val source = generalVM.source.collectAsState().value

    val darkMode = generalVM.darkModeSetting ?: generalVM.setDarkModeSetting(isSystemInDarkTheme())

    Aisle9bTheme(darkTheme = darkMode) {
        Scaffold(
            topBar = {
                when (generalVM.topBar.collectAsState().value) {
                    TopBarOptions.Back ->
                        BackTopAppBar(
                            source = source
                        ) {
                            navController.popBackStack()
                            generalVM.setTopBarOption(TopBarOptions.Default)
                            generalVM.cleanListsInDatabase()
                        }

                    TopBarOptions.Default ->
                        DefaultTopAppBar(
                            navigate = { navController.navigate(GroceryScreens.SettingsScreen.name) },
                            source = source
                        )
                }
            },
            floatingActionButton = {
                when (source) {
                    GroceryScreens.CustomListScreen -> {
                        val item: Long = 0
                        AddFAB {
                            navController.navigate(GroceryScreens.AddNewCustomListScreen.name + "/${item}")
                        }
                    }
                    GroceryScreens.MealScreen ->
                        AddFAB {
                            navController.navigate(GroceryScreens.AddNewMealScreen.name)
                        }

                    GroceryScreens.AddNewMealScreen ->
                        SaveFAB { generalVM.turnNewMealVisible() }

                    GroceryScreens.RecipeDetailsScreen ->
                        SaveFAB {
                            generalVM.saveAPIMealonFABClick()
                        }

                    else -> {}

                }
            },
            bottomBar = {
                BottomNavigationBar9(
                    mealsName = "Meals (${generalVM.numOfMeals.collectAsState().value})",
                    navController = navController,
                    badgeCount = generalVM.groceryBadgeCount.collectAsState().value,
                    onItemClick = {
                        navController.navigate(it.route)
                    })
            })
        {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                appNavigation(it)
            }
        }
    }
}


enum class TopBarOptions {
    Default,
    Back;
}
