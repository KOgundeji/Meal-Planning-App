package com.kunle.aisle9b.navigation


import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
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
import com.kunle.aisle9b.screens.ListScreen
import com.kunle.aisle9b.screens.MealScreen
import com.kunle.aisle9b.screens.SettingsScreen
import com.kunle.aisle9b.ui.theme.BaseOrange

@Composable
fun Aisle9Navigation(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = GroceryScreens.MealScreen.name) {
        composable(route = GroceryScreens.GroceryScreen.name) { ListScreen(modifier) }
        composable(route = GroceryScreens.MealScreen.name) { MealScreen(modifier) }
        composable(route = GroceryScreens.SettingsScreen.name) { SettingsScreen(modifier) }
    }
}

@Composable
fun BottomNavigationBar9(
    items: List<BottomNavItem>,
    navController: NavController,
    modifier: Modifier = Modifier,
    onItemClick: (BottomNavItem) -> Unit
) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    BottomNavigation(
        modifier = modifier,
        backgroundColor = Color.DarkGray,
        elevation = 5.dp
    ) {
        items.forEach { item ->
            val selected = item.route == backStackEntry.value?.destination?.route
            //its checking if the current navController route is the same as the selected route. If it is, highlight the item
            BottomNavigationItem(
                selected = selected,
                selectedContentColor = BaseOrange,
                unselectedContentColor = Color.Gray,
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
                        if (selected) {
                            Text(text = item.name, textAlign = TextAlign.Center, fontSize = 10.sp)
                        }
                    }
                })
        }
    }
}