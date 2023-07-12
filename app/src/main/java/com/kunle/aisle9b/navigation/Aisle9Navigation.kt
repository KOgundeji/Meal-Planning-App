package com.kunle.aisle9b.navigation


import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.DinnerDining
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlaylistAdd
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kunle.aisle9b.TopBarOptions
import com.kunle.aisle9b.screens.*
import com.kunle.aisle9b.screens.appSettings.SettingsScreen
import com.kunle.aisle9b.screens.customLists.CustomListScreen
import com.kunle.aisle9b.screens.customLists.CustomListVM
import com.kunle.aisle9b.screens.groceries.GroceryScreen
import com.kunle.aisle9b.screens.groceries.GroceryVM
import com.kunle.aisle9b.screens.meals.MealDetailsScreen
import com.kunle.aisle9b.screens.meals.MealScreen
import com.kunle.aisle9b.screens.meals.MealVM
import com.kunle.aisle9b.screens.recipes.RecipeDetailsScreen
import com.kunle.aisle9b.screens.recipes.RecipeScreen
import com.kunle.aisle9b.screens.recipes.RecipesVM
import com.kunle.aisle9b.screens.splash.SplashScreen
import com.kunle.aisle9b.ui.theme.*
import dagger.hilt.android.AndroidEntryPoint

@Composable
fun Aisle9Navigation(
    modifier: Modifier = Modifier,
    generalVM: GeneralVM,
    navController: NavHostController,
    source: (GroceryScreens) -> Unit,
    topBar: (TopBarOptions) -> Unit
) {
    val groceryVM = viewModel<GroceryVM>()
    val recipeVM = viewModel<RecipesVM>()
    val customListVM = viewModel<CustomListVM>()
    val mealVM = viewModel<MealVM>()

    NavHost(
        navController = navController,
        startDestination = GroceryScreens.GroceryListScreen.name
    ) {
        composable(route = GroceryScreens.SplashScreen.name) {
            SplashScreen(
                navController = navController
            )
        }
        composable(route = GroceryScreens.GroceryListScreen.name) {
            GroceryScreen(
                modifier = modifier,
                generalVM = generalVM,
                groceryVM = groceryVM,
                navController = navController,
                source = source,
                topBar = topBar
            )
        }
        composable(route = GroceryScreens.CustomListScreen.name) {
            CustomListScreen(
                generalVM = generalVM,
                customListVM = customListVM,
                modifier = modifier,
                source = source,
                topBar = topBar
            )
        }
        composable(route = GroceryScreens.MealScreen.name) {
            MealScreen(
                modifier = modifier,
                generalVM = generalVM,
                mealVM = mealVM,
                navController = navController,
                source = source,
                topBar = topBar
            )
        }
        composable(route = GroceryScreens.SettingsScreen.name) {
            SettingsScreen(
                modifier = modifier,
                generalVM = generalVM,
                source = source,
                topBar = topBar
            )
        }
        composable(route = GroceryScreens.RecipeScreen.name) {
            RecipeScreen(
                modifier = modifier,
                recipesVM = recipeVM,
                navController = navController,
                source = source,
                topBar = topBar
            )
        }
        composable(route = GroceryScreens.AddMealsScreenTEST.name) {
            AddMealScreenTest(
                modifier = modifier,
                generalVM = generalVM,
                mealVM = mealVM,
                source = source,
                topBar = topBar
            )
        }
        composable(route = GroceryScreens.AddCustomListScreen.name) {
            AddPreMadeListScreen(
                modifier = modifier,
                customListVM = customListVM,
                navController = navController,
                source = source,
                topBar = topBar
            )
        }
        composable(
            route = GroceryScreens.RecipeDetailsScreen.name + "/{recipeIndex}",
            arguments = listOf(
                navArgument(name = "recipeIndex") {
                    type = NavType.IntType
                })
        ) { backStack ->
            RecipeDetailsScreen(
                modifier = modifier,
                recipeId = backStack.arguments?.getInt("recipeIndex"),
                recipesVM = recipeVM,
                generalVM = generalVM,
                source = source,
                topBar = topBar
            )
        }
        composable(
            route = GroceryScreens.MealDetailsScreen.name + "/{mealIndex}",
            arguments = listOf(
                navArgument(name = "mealIndex") {
                    type = NavType.IntType
                })
        ) { backStack ->
            MealDetailsScreen(
                modifier = modifier,
                mealIndex = backStack.arguments?.getInt("mealIndex"),
                mealVM = mealVM,
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