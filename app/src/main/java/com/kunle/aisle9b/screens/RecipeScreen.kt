package com.kunle.aisle9b.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kunle.aisle9b.navigation.GroceryScreens


@Composable
fun RecipeScreen(
    modifier: Modifier = Modifier,
    shoppingViewModel: ShoppingViewModel,
    screenHeader: (String) -> Unit
) {
   screenHeader(GroceryScreens.fullName(GroceryScreens.RecipeScreen))

}