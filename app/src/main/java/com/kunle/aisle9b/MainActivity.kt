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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.kunle.aisle9b.navigation.Aisle9Navigation
import com.kunle.aisle9b.navigation.BottomNavigationBar9
import com.kunle.aisle9b.navigation.GroceryScreens
import com.kunle.aisle9b.screens.GeneralVM
import com.kunle.aisle9b.screens.customLists.CustomListVM
import com.kunle.aisle9b.screens.meals.MealVM
import com.kunle.aisle9b.ui.theme.Aisle9bTheme
import com.kunle.aisle9b.util.AddFAB
import com.kunle.aisle9b.util.BackTopAppBar
import com.kunle.aisle9b.util.CustomListTopAppBar
import com.kunle.aisle9b.util.DefaultTopAppBar
import com.kunle.aisle9b.util.MealTopAppBar
import com.kunle.aisle9b.util.SaveFAB
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val generalVM: GeneralVM by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val mealVM: MealVM by viewModels()
            val customListVM: CustomListVM by viewModels()
            ShoppingAppScaffold(navController, generalVM, mealVM, customListVM)
        }
    }

    override fun onStop() {
        super.onStop()
        generalVM.cleanListsInDatabase()
    }
}

@Composable
fun ShoppingAppScaffold(
    navController: NavHostController,
    generalVM: GeneralVM,
    mealVM: MealVM,
    customListVM: CustomListVM
) {
    val source = generalVM.source.collectAsState().value
    val topBarState = generalVM.topBar.collectAsState().value

    val customListSearchWord = customListVM.searchWord.collectAsState().value
    val mealSearchWord = mealVM.searchWord.collectAsState().value
    val mealViewSetting = generalVM.mealViewSetting.collectAsState().value

    val numOfMeals = generalVM.numOfMeals.collectAsState().value
    val groceryCounter = generalVM.groceryBadgeCount.collectAsState().value
    val darkMode = generalVM.darkModeSetting ?: generalVM.setDarkModeSetting(isSystemInDarkTheme())

    Aisle9bTheme(darkTheme = darkMode) {
        Scaffold(
            topBar = {
                when (topBarState) {
                    TopBarOptions.Back ->
                        BackTopAppBar(source = source) {
                            navController.popBackStack()
                            generalVM.setTopBarOption(TopBarOptions.Default)
                        }

                    TopBarOptions.Default ->
                        DefaultTopAppBar(
                            navigate = { navController.navigate(GroceryScreens.SettingsScreen.name) },
                            source = source
                        )

                    TopBarOptions.MealList -> {
                        MealTopAppBar(
                            searchWord = mealSearchWord,
                            viewListOption = mealViewSetting,
                            onSearchChange = { mealVM.setSearchWord(it) },
                            onCancelClick = { mealVM.setSearchWord("") },
                            setListOptions = {
                                generalVM.saveMealViewSettings(it)
                                generalVM.saveMealViewSettings(it)
                            },
                            navigate = { navController.navigate(GroceryScreens.SettingsScreen.name) }
                        )
                    }

                    TopBarOptions.CustomLists -> {
                        CustomListTopAppBar(
                            searchWord = customListSearchWord,
                            onSearchChange = { customListVM.setSearchWord(it) },
                            onCancelClick = { customListVM.setSearchWord("") },
                            navigate = { navController.navigate(GroceryScreens.SettingsScreen.name) })
                    }
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
                        SaveFAB { generalVM.saveAPIMealonFABClick() }

                    else -> {}

                }
            },
            bottomBar = {
                BottomNavigationBar9(
                    mealsName = "Meals (${numOfMeals})",
                    navController = navController,
                    badgeCount = groceryCounter,
                    onItemClick = { navController.navigate(it.route) })
            })
        { paddingValues ->
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Aisle9Navigation(
                    modifier = Modifier.padding(paddingValues),
                    navController = navController,
                    generalVM = generalVM,
                    mealVM = mealVM,
                    customListVM = customListVM
                )
            }
        }
    }
}


enum class TopBarOptions {
    MealList,
    CustomLists,
    Default,
    Back;
}
