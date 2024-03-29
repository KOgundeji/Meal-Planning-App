package com.kunle.aisle9b.navigation


import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.DinnerDining
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlaylistAdd
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
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
import com.kunle.aisle9b.screens.meals.EditMealDetailsScreen
import com.kunle.aisle9b.screens.meals.MealScreen
import com.kunle.aisle9b.screens.meals.MealVM
import com.kunle.aisle9b.screens.meals.ViewMealDetailsScreen
import com.kunle.aisle9b.screens.planning.PlanningScreen
import com.kunle.aisle9b.screens.planning.PlanningVM
import com.kunle.aisle9b.screens.recipes.RecipeDetailsScreen
import com.kunle.aisle9b.screens.recipes.RecipeScreenGate
import com.kunle.aisle9b.screens.recipes.RecipesVM
import com.kunle.aisle9b.screens.splash.SplashScreen

@Composable
fun Aisle9Navigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    generalVM: GeneralVM,
    mealVM: MealVM,
    customListVM: CustomListVM
) {
    val groceryVM = hiltViewModel<GroceryVM>()
    val recipesVM = hiltViewModel<RecipesVM>()
    val planningVM = hiltViewModel<PlanningVM>()

    NavHost(
        navController = navController,
        startDestination = GroceryScreens.GroceryListScreen.name,
        modifier = modifier
    ) {
        composable(route = GroceryScreens.SplashScreen.name) {
            SplashScreen()
        }
        composable(route = GroceryScreens.GroceryListScreen.name) {
            generalVM.setTopBarOption(TopBarOptions.Default)
            generalVM.setClickSource(GroceryScreens.GroceryListScreen)

            GroceryScreen(
                groceryVM = groceryVM,
                generalVM = generalVM,
                navToCustomLists = { navController.navigate(GroceryScreens.CustomListScreen.name) },
                navToMealScreen = { navController.navigate(GroceryScreens.MealScreen.name) }
            )
        }
        composable(route = GroceryScreens.Planning.name) {
            generalVM.setTopBarOption(TopBarOptions.Default)
            generalVM.setClickSource(GroceryScreens.Planning)

            PlanningScreen(
                viewModel = mealVM
            )
        }
        composable(route = GroceryScreens.CustomListScreen.name) {
            generalVM.setTopBarOption(TopBarOptions.CustomLists)
            generalVM.setClickSource(GroceryScreens.CustomListScreen)

            CustomListScreen(
                generalVM = generalVM,
                customListVM = customListVM,
                navToListDetails = {
                    navController.navigate(
                        GroceryScreens.AddNewCustomListScreen.name + "/${it}"
                    )
                }
            )
        }
        composable(
            route = GroceryScreens.MealScreen.name,
//            enterTransition = {
//            slideIntoContainer(
//                AnimatedContentTransitionScope.SlideDirection.Start,
//                animationSpec = tween(700)
//            )
//        }, exitTransition = {
//            slideOutOfContainer(
//                AnimatedContentTransitionScope.SlideDirection.Down,
//                animationSpec = tween(700)
//            )
//        }
        ) {
            generalVM.setTopBarOption(TopBarOptions.MealList)
            generalVM.setClickSource(GroceryScreens.MealScreen)

            MealScreen(
                mealVM = mealVM,
                generalVM = generalVM,
                navToEditDetails = {
                    navController.navigate(
                        GroceryScreens.MealEditDetailsScreen.name + "/${it}"
                    )
                },
                navToViewDetails = {
                    navController.navigate(
                        GroceryScreens.ViewMealDetailsScreen.name + "/${it}"
                    )
                },
                navToRecipeDetails = { navController.navigate(GroceryScreens.RecipeDetailsScreen.name + "/${it}") }
            )
        }
        composable(route = GroceryScreens.SettingsScreen.name) {
            generalVM.setTopBarOption(TopBarOptions.Default)
            generalVM.setClickSource(GroceryScreens.SettingsScreen)

            SettingsScreen(generalVM = generalVM)
        }
        composable(route = GroceryScreens.RecipeScreen.name) {
            generalVM.setTopBarOption(TopBarOptions.Default)
            generalVM.setClickSource(GroceryScreens.RecipeScreen)

            RecipeScreenGate(
                recipesVM = recipesVM,
                navToRecipeDetails = { navController.navigate(GroceryScreens.RecipeDetailsScreen.name + "/${it}") },
            )
        }
        composable(route = GroceryScreens.AddNewMealScreen.name) {
            generalVM.setTopBarOption(TopBarOptions.Back)
            generalVM.setClickSource(GroceryScreens.AddNewMealScreen)

            AddMealScreenGate(
                mealVM = mealVM
            )
        }
        composable(
            route = GroceryScreens.AddNewCustomListScreen.name + "/{listIndex}",
            arguments = listOf(
                navArgument(name = "listIndex") {
                    type = NavType.LongType
                })
        ) { backStackEntry ->
            generalVM.setTopBarOption(TopBarOptions.Back)
            generalVM.setClickSource(GroceryScreens.AddNewCustomListScreen)

            PreMadeListScreenGate(
                listId = backStackEntry.arguments?.getLong("listIndex"),
                customListVM = customListVM,
                saveAndExitScreen = { navController.navigate(GroceryScreens.CustomListScreen.name) }
            )
        }
        composable(
            route = GroceryScreens.RecipeDetailsScreen.name + "/{recipeIndex}",
            arguments = listOf(
                navArgument(name = "recipeIndex") {
                    type = NavType.IntType
                })
        ) { backStackEntry ->
            generalVM.setTopBarOption(TopBarOptions.Back)
            generalVM.setClickSource(GroceryScreens.RecipeDetailsScreen)

            RecipeDetailsScreen(
                recipeId = backStackEntry.arguments?.getInt("recipeIndex"),
                recipesVM = recipesVM,
                generalVM = generalVM
            )
        }
        composable(
            route = GroceryScreens.MealEditDetailsScreen.name + "/{mealIndex}",
            arguments = listOf(
                navArgument(name = "mealIndex") {
                    type = NavType.LongType
                })
        ) { backStackEntry ->
            generalVM.setTopBarOption(TopBarOptions.Back)
            generalVM.setClickSource(GroceryScreens.MealEditDetailsScreen)

            EditMealDetailsScreen(
                mealId = backStackEntry.arguments?.getLong("mealIndex"),
                mealVM = mealVM
            )
        }
        composable(
            route = GroceryScreens.ViewMealDetailsScreen.name + "/{mealIndex}",
            arguments = listOf(
                navArgument(name = "mealIndex") {
                    type = NavType.LongType
                })
        ) { backStackEntry ->
            generalVM.setTopBarOption(TopBarOptions.Back)
            generalVM.setClickSource(GroceryScreens.ViewMealDetailsScreen)

            ViewMealDetailsScreen(
                mealId = backStackEntry.arguments?.getLong("mealIndex"),
                mealVM = mealVM
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
//        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        tonalElevation = 0.dp,
    ) {
        screenList.forEach { item ->
            val selected = item.route == backStackEntry.value?.destination?.route
            //its checking if the current navController route is the same as the selected route. If it is, highlight the item
            NavigationBarItem(
                selected = selected,
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    selectedIconColor = MaterialTheme.colorScheme.primary
                ),
                onClick = { onItemClick(item) },
                icon = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        if (item.name == "Groceries") {
                            BadgedBox(badge = {
                                Badge(containerColor = MaterialTheme.colorScheme.primary) {
                                    Text(
                                        text = badgeCount.toString(),
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                            }) {
                                Icon(imageVector = item.icon, contentDescription = item.name, modifier = Modifier.size(30.dp))
                            }
                        } else {
                            Icon(imageVector = item.icon, contentDescription = item.name, modifier = Modifier.size(30.dp))
                        }
                        Text(
                            text = item.name,
                            textAlign = TextAlign.Center,
                            fontSize = 11.sp,
                            fontWeight = if (selected) {
                                FontWeight.ExtraBold
                            } else {
                                null
                            }
                        )
                    }
                })
        }
    }
}

val screenList = listOf(
    BottomNavItem(
        name = "Groceries",
        route = GroceryScreens.GroceryListScreen.name,
        icon = Icons.Filled.Checklist
    ),
    BottomNavItem(
        name = "Lists",
        route = GroceryScreens.CustomListScreen.name,
        icon = Icons.Filled.PlaylistAdd
    ),
    BottomNavItem(
        name = "",
        route = GroceryScreens.MealScreen.name,
        icon = Icons.Filled.DinnerDining
    ),
    BottomNavItem(
        name = "Recipes",
        route = GroceryScreens.RecipeScreen.name,
        icon = Icons.Filled.MenuBook
    )
)