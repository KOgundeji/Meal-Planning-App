package com.kunle.aisle9b.screens.recipes

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import com.kunle.aisle9b.TopBarOptions
import com.kunle.aisle9b.models.apiModels.DataOrException
import com.kunle.aisle9b.models.apiModels.trendingRecipeModels.TrendingRawAPIData
import com.kunle.aisle9b.navigation.GroceryScreens

@Composable
fun RecipeScreen(
    modifier: Modifier = Modifier,
    recipesVM: RecipesVM,
    topBar: (TopBarOptions) -> Unit,
    source: (GroceryScreens) -> Unit
) {
    topBar(TopBarOptions.Default)
    source(GroceryScreens.RecipeScreen)

    val recipeData = produceState<DataOrException<TrendingRawAPIData, Boolean, Exception>>(
        initialValue = DataOrException(loading = true)
    ) {
        value = recipesVM.getTrendingRecipes(vegetarian = false)
    }.value

    if (recipeData.loading == true) {
        CircularProgressIndicator()
    } else if (recipeData.data != null) {
        Text(text = recipeData.data!!.toString())
    }

}

