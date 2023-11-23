package com.kunle.aisle9b.navigation


import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.DinnerDining
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlaylistAdd
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kunle.aisle9b.screens.*
import com.kunle.aisle9b.screens.appSettings.SettingsScreen
import com.kunle.aisle9b.screens.customLists.CustomListScreen
import com.kunle.aisle9b.screens.customLists.CustomListVM
import com.kunle.aisle9b.screens.groceries.GroceryScreen
import com.kunle.aisle9b.screens.meals.MealDetailsScreen
import com.kunle.aisle9b.screens.meals.MealScreen
import com.kunle.aisle9b.screens.meals.MealVM
import com.kunle.aisle9b.screens.meals.ViewMealDetailsScreen
import com.kunle.aisle9b.screens.recipes.RecipeDetailsScreen
import com.kunle.aisle9b.screens.recipes.RecipeScreenGate
import com.kunle.aisle9b.screens.recipes.RecipesVM
import com.kunle.aisle9b.screens.splash.SplashScreen

@Composable
fun Aisle9Navigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    generalVM: GeneralVM
) {

    NavHost(
        navController = navController,
        startDestination = GroceryScreens.GroceryListScreen.name,
        modifier = modifier
    ) {

        composable(route = GroceryScreens.SplashScreen.name) {
            SplashScreen(
                navController = navController
            )
        }
        composable(route = GroceryScreens.GroceryListScreen.name) { backStackEntry ->

            GroceryScreen(
                navController = navController,
                generalVM = generalVM
            )
        }
        composable(route = GroceryScreens.CustomListScreen.name) { backStackEntry ->

            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(GroceryScreens.GroceryListScreen.name)
            }
            val customListVM = hiltViewModel<CustomListVM>(parentEntry)

            CustomListScreen(
                customListVM = customListVM,
                generalVM = generalVM
            )
        }
        composable(route = GroceryScreens.MealScreen.name) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(GroceryScreens.GroceryListScreen.name)
            }
            val mealVM = hiltViewModel<MealVM>(parentEntry)
            MealScreen(
                navController = navController,
                mealVM = mealVM,
                generalVM = generalVM
            )
        }
        composable(route = GroceryScreens.SettingsScreen.name) {
            SettingsScreen()
        }
        composable(route = GroceryScreens.RecipeScreen.name) { backStackEntry ->

            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(GroceryScreens.GroceryListScreen.name)
            }
            val recipesVM = hiltViewModel<RecipesVM>(parentEntry)

            RecipeScreenGate(
                navController = navController,
                recipesVM = recipesVM,
                generalVM = generalVM
            )
        }
        composable(route = GroceryScreens.AddNewMealScreen.name) { backStackEntry ->

            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(GroceryScreens.GroceryListScreen.name)
            }
            val mealVM = hiltViewModel<MealVM>(parentEntry)

            AddMealScreenGate(
                mealVM = mealVM,
                generalVM = generalVM
            )
        }
        composable(route = GroceryScreens.AddNewCustomListScreen.name) { backStackEntry ->

            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(GroceryScreens.GroceryListScreen.name)
            }
            val customListVM = hiltViewModel<CustomListVM>(parentEntry)

            AddPreMadeListScreen(
                navController = navController,
                customListVM = customListVM,
                generalVM = generalVM
            )
        }
        composable(
            route = GroceryScreens.RecipeDetailsScreen.name + "/{recipeIndex}",
            arguments = listOf(
                navArgument(name = "recipeIndex") {
                    type = NavType.IntType
                })
        ) { backStackEntry ->

            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(GroceryScreens.GroceryListScreen.name)
            }
            val recipesVM = hiltViewModel<RecipesVM>(parentEntry)

            RecipeDetailsScreen(
                recipeId = backStackEntry.arguments?.getInt("recipeIndex"),
                recipesVM = recipesVM,
                generalVM = generalVM
            )
        }
        composable(
            route = GroceryScreens.MealDetailsScreen.name + "/{mealIndex}",
            arguments = listOf(
                navArgument(name = "mealIndex") {
                    type = NavType.IntType
                })
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(GroceryScreens.GroceryListScreen.name)
            }
            val mealVM = hiltViewModel<MealVM>(parentEntry)

            MealDetailsScreen(
                mealIndex = backStackEntry.arguments?.getInt("mealIndex"),
                mealVM = mealVM,
                generalVM = generalVM
            )
        }
        composable(
            route = GroceryScreens.ViewMealDetailsScreen.name + "/{mealIndex}",
            arguments = listOf(
                navArgument(name = "mealIndex") {
                    type = NavType.IntType
                })
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(GroceryScreens.GroceryListScreen.name)
            }
            val mealVM = hiltViewModel<MealVM>(parentEntry)

            ViewMealDetailsScreen(
                mealIndex = backStackEntry.arguments?.getInt("mealIndex"),
                mealVM = mealVM,
                generalVM = generalVM
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
        tonalElevation = 5.dp
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
        name = "Saved Lists",
        route = GroceryScreens.CustomListScreen.name,
        icon = Icons.Filled.PlaylistAdd
    ),
    BottomNavItem(
        name = "",
        route = GroceryScreens.MealScreen.name,
        icon = Icons.Filled.DinnerDining
    ),
    BottomNavItem(
        name = GroceryScreens.headerTitle(GroceryScreens.RecipeScreen),
        route = GroceryScreens.RecipeScreen.name,
        icon = Icons.Filled.MenuBook
    )
)