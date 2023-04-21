package com.kunle.aisle9b.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kunle.aisle9b.TopBarOptions
import com.kunle.aisle9b.navigation.GroceryScreens


@Composable
fun RecipeScreen(
    modifier: Modifier = Modifier,
    shoppingVM: ShoppingVM
) {
    shoppingVM.screenHeader.value = GroceryScreens.headerTitle(GroceryScreens.RecipeScreen)
    shoppingVM.topBar.value = TopBarOptions.BackButton

}