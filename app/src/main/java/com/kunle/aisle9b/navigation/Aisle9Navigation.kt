package com.kunle.aisle9b.navigation


import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kunle.aisle9b.screens.*
import com.kunle.aisle9b.ui.theme.BaseOrange

@Composable
fun Aisle9Navigation(
    navController: NavHostController,
    shoppingViewModel: ShoppingViewModel,
    modifier: Modifier = Modifier,
    screenHeader: (String) -> Unit
) {
    NavHost(navController = navController, startDestination = GroceryScreens.SettingsScreen.name) {
        composable(route = GroceryScreens.ListScreen.name) {
            ListScreen(
                shoppingViewModel = shoppingViewModel,
                modifier = modifier,
                screenHeader = screenHeader
            )
        }
        composable(route = GroceryScreens.MealScreen.name) {
            MealScreen(
                shoppingViewModel = shoppingViewModel,
                modifier = modifier,
                screenHeader = screenHeader
            )
        }
        composable(route = GroceryScreens.SettingsScreen.name) {
            SettingsScreen(
                shoppingViewModel = shoppingViewModel,
                modifier = modifier,
                screenHeader = screenHeader
            )
        }
        composable(route = GroceryScreens.RecipeScreen.name) {
            RecipeScreen(
                shoppingViewModel = shoppingViewModel,
                modifier = modifier,
                screenHeader = screenHeader,
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
    modifier: Modifier = Modifier,
    onItemClick: (BottomNavItem) -> Unit
) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    NavigationBar(
        modifier = modifier,
        containerColor = Color.White,
        tonalElevation = 5.dp
    ) {
        items.forEach { item ->
            val selected = item.route == backStackEntry.value?.destination?.route
            //its checking if the current navController route is the same as the selected route. If it is, highlight the item
            NavigationBarItem(
                selected = selected,
                onClick = { onItemClick(item) },
                icon = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        if (item.badgeCount > 0) {
                            BadgedBox(badge = {
                                Badge { Text(text = item.badgeCount.toString()) }
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