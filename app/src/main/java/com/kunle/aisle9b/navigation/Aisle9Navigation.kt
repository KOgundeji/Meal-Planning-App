package com.kunle.aisle9b.navigation


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.kunle.aisle9b.TopBarOptions
import com.kunle.aisle9b.screens.*
import com.kunle.aisle9b.screens.AddMealScreen
import com.kunle.aisle9b.screens.AddPreMadeListScreen
import com.kunle.aisle9b.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Aisle9Navigation(
    navController: NavHostController,
    shoppingVM: ShoppingVM,
    drawerState: DrawerState,
    modifier: Modifier = Modifier
) {
    NavHost(navController = navController, startDestination = GroceryScreens.ListScreen.name) {
        composable(route = GroceryScreens.ListScreen.name) {
            ListScreen(
                shoppingVM = shoppingVM,
                modifier = modifier,
                navController = navController,
                drawerState = drawerState
            )
        }
        composable(route = GroceryScreens.MealScreen.name) {
            MealScreen(
                shoppingVM = shoppingVM,
                modifier = modifier,
                navController = navController,
                drawerState = drawerState,
            )
        }
        composable(route = GroceryScreens.SettingsScreen.name) {
            SettingsScreen(
                shoppingVM = shoppingVM,
                modifier = modifier,
                navController = navController,
                drawerState = drawerState
            )
        }
        composable(route = GroceryScreens.RecipeScreen.name) {
            RecipeScreen(
                shoppingVM = shoppingVM,
                modifier = modifier,
                navController = navController,
                drawerState = drawerState
            )
        }
        composable(route = GroceryScreens.PremadeListScreen.name) {
            ListLibrary(
                shoppingVM = shoppingVM,
                modifier = modifier,
                navController = navController,
                drawerState = drawerState,
            )
        }
        composable(route = GroceryScreens.AddMealsScreen.name) {
            AddMealScreen(
                shoppingVM = shoppingVM,
                modifier = modifier,
                navController = navController
            )
        }
        composable(route = GroceryScreens.AddCustomListScreen.name) {
            AddPreMadeListScreen(
                shoppingVM = shoppingVM,
                modifier = modifier,
                navController = navController
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationBar9(
    items: List<BottomNavItem>,
    navController: NavController,
    badgeCount: Int,
    onItemClick: (BottomNavItem) -> Unit
) {
    val backStackEntry = navController.currentBackStackEntryAsState()

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        tonalElevation = 5.dp,
        modifier = Modifier.border(border = BorderStroke(width = Dp.Hairline, Color.LightGray))
    ) {
        items.forEach { item ->
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
                        if (item.name == GroceryScreens.headerTitle(GroceryScreens.ListScreen)) {
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