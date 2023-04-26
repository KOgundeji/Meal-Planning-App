package com.kunle.aisle9b.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.kunle.aisle9b.TopBarOptions
import com.kunle.aisle9b.navigation.BottomNavigationBar9
import com.kunle.aisle9b.navigation.GroceryScreens
import com.kunle.aisle9b.util.DefaultTopAppBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeScreen(
    modifier: Modifier = Modifier,
    shoppingVM: ShoppingVM,
    navController: NavController,
    drawerState: DrawerState
) {
    Scaffold(
        topBar = {
            DefaultTopAppBar(
                drawerState = drawerState,
                screenHeader = GroceryScreens.headerTitle(GroceryScreens.RecipeScreen)
            )
        }, bottomBar = {
            BottomNavigationBar9(
                items = shoppingVM.screenList,
                navController = navController,
                badgeCount = shoppingVM.groceryBadgeCount.value,
                onItemClick = {
                    navController.navigate(it.route)
                })
        }) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(it)
        ) {

        }
    }
}

