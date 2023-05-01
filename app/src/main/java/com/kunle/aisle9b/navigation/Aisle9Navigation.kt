package com.kunle.aisle9b.navigation


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.DinnerDining
import androidx.compose.material.icons.filled.PlaylistAdd
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kunle.aisle9b.TopBarOptions
import com.kunle.aisle9b.data.addFakeToDatabase
import com.kunle.aisle9b.data.sampleFoodData
import com.kunle.aisle9b.screens.*
import com.kunle.aisle9b.screens.AddMealScreen
import com.kunle.aisle9b.screens.AddPreMadeListScreen
import com.kunle.aisle9b.screens.appSettings.SettingsScreen
import com.kunle.aisle9b.screens.appSettings.SettingsVM
import com.kunle.aisle9b.screens.customLists.CustomListVM
import com.kunle.aisle9b.screens.customLists.CustomListScreen
import com.kunle.aisle9b.screens.groceries.GroceryVM
import com.kunle.aisle9b.screens.groceries.GroceryScreen
import com.kunle.aisle9b.screens.meals.MealScreen
import com.kunle.aisle9b.screens.meals.MealVM
import com.kunle.aisle9b.screens.recipes.RecipeScreen
import com.kunle.aisle9b.screens.recipes.RecipesVM
import com.kunle.aisle9b.ui.theme.*

@Composable
fun Aisle9Navigation(
    modifier: Modifier = Modifier,
    sharedVM: SharedVM,
    navController: NavHostController,
    source: (GroceryScreens) -> Unit,
    topBar: (TopBarOptions) -> Unit
) {
    val settingsVM = viewModel<SettingsVM>()
    val recipeVM = viewModel<RecipesVM>()
    val groceryVM = viewModel<GroceryVM>()
    val customListVM = viewModel<CustomListVM>()
    val mealVM = viewModel<MealVM>()

//    addFakeToDatabase(
//        sharedVM = sharedVM,
//        mealVM = mealVM,
//        customListVM = customListVM
//    )
    NavHost(
        navController = navController,
        startDestination = GroceryScreens.GroceryListScreen.name
    ) {
        composable(route = GroceryScreens.GroceryListScreen.name) {
            GroceryScreen(
                modifier = modifier,
                shoppingVM = sharedVM,
                groceryVM = groceryVM,
                navController = navController,
                source = source,
                topBar = topBar
            )
        }
        composable(route = GroceryScreens.MealScreen.name) {
            MealScreen(
                modifier = modifier,
                shoppingVM = sharedVM,
                mealVM = mealVM,
                source = source,
                topBar = topBar
            )
        }
        composable(route = GroceryScreens.SettingsScreen.name) {
            SettingsScreen(
                modifier = modifier,
                shoppingVM = sharedVM,
                settingsVM = settingsVM,
                source = source,
                topBar = topBar
            )
        }
        composable(route = GroceryScreens.RecipeScreen.name) {
            RecipeScreen(
                modifier = modifier,
                shoppingVM = sharedVM,
                recipesVM = recipeVM,
                source = source,
                topBar = topBar
            )
        }
        composable(route = GroceryScreens.CustomListScreen.name) {
            CustomListScreen(
                shoppingVM = sharedVM,
                customListVM = customListVM,
                modifier = modifier,
                source = source,
                topBar = topBar
            )
        }
        composable(route = GroceryScreens.AddMealsScreen.name) {
            AddMealScreen(
                modifier = modifier,
                shoppingVM = sharedVM,
                mealVM = mealVM,
                navController = navController,
                source = source,
                topBar = topBar
            )
        }
        composable(route = GroceryScreens.AddCustomListScreen.name) {
            AddPreMadeListScreen(
                modifier = modifier,
                shoppingVM = sharedVM,
                customListVM = customListVM,
                navController = navController,
                source = source,
                topBar = topBar
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationBar9(
    mealsName: String,
    navController: NavController,
    badgeCount: Int,
    onItemClick: (BottomNavItem) -> Unit
) {
    val backStackEntry = navController.currentBackStackEntryAsState()

    screenList[2].name = mealsName

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        tonalElevation = 5.dp,
        modifier = Modifier.border(border = BorderStroke(width = Dp.Hairline, Color.LightGray))
    ) {
        screenList.forEach { item ->
            val selected = item.route == backStackEntry.value?.destination?.route
            //its checking if the current navController route is the same as the selected route. If it is, highlight the item
            NavigationBarItem(
                selected = selected,
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                    selectedIconColor = MaterialTheme.colorScheme.primary
                ),
                onClick = { onItemClick(item) },
                icon = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        if (item.name == GroceryScreens.headerTitle(GroceryScreens.GroceryListScreen)) {
                            BadgedBox(badge = {
                                Badge(containerColor = MaterialTheme.colorScheme.primary) {
                                    Text(
                                        text = badgeCount.toString(),
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                            }) {
                                Icon(imageVector = item.icon, contentDescription = item.name)
                            }
                        } else {
                            Icon(imageVector = item.icon, contentDescription = item.name)
                        }
                        Text(text = item.name, textAlign = TextAlign.Center, fontSize = 11.sp)
                    }
                })
        }
    }
}

val screenList = listOf(
    BottomNavItem(
        name = GroceryScreens.headerTitle(GroceryScreens.GroceryListScreen),
        route = GroceryScreens.GroceryListScreen.name,
        icon = Icons.Filled.Checklist
    ),
    BottomNavItem(
        name = GroceryScreens.headerTitle(GroceryScreens.CustomListScreen),
        route = GroceryScreens.CustomListScreen.name,
        icon = Icons.Filled.PlaylistAdd
    ),
    BottomNavItem(
        name = "",
        route = GroceryScreens.MealScreen.name,
        icon = Icons.Filled.DinnerDining
    )
)