package com.kunle.aisle9b.screens.meals

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kunle.aisle9b.models.Food
import com.kunle.aisle9b.screens.GeneralVM
import com.kunle.aisle9b.templates.items.MealItem9
import com.kunle.aisle9b.templates.items.VisualMealItem
import com.kunle.aisle9b.util.ReconciliationDialog

@Composable
fun MealScreen(
    modifier: Modifier = Modifier,
    generalVM: GeneralVM = hiltViewModel(),
    mealVM: MealVM = hiltViewModel(),
    navToDetailsScreen: (Long) -> Unit,
    navToViewDetails: (Long) -> Unit,
    navToRecipeDetails: (Int) -> Unit,
) {
    var transferFoodsToGroceryList by remember { mutableStateOf(false) }
    var listsToAddToGroceryList by remember { mutableStateOf(emptyList<Food>()) }

    val context = LocalContext.current

    val filteredMealLists = mealVM.filteredMealList.collectAsState().value
    val mealViewSetting = generalVM.mealViewSetting.collectAsState().value
    val listSpacing = if (mealViewSetting == MealListOptions.Images) {
        30.dp
    } else {
        5.dp
    }

    if (transferFoodsToGroceryList) {
        val foodsForReconciliation =
            generalVM.filterForReconciliation(
                listToAdd = listsToAddToGroceryList
            )

        if (foodsForReconciliation.isNotEmpty()) {
            ReconciliationDialog(
                items = foodsForReconciliation,
                viewModel = mealVM
            ) {
                transferFoodsToGroceryList = false
            }
        } else {
            transferFoodsToGroceryList = false
        }

        Toast.makeText(context, "Groceries added to Grocery List", Toast.LENGTH_SHORT).show()
    }
    LazyColumn(
        modifier = modifier.padding(top = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(listSpacing)
    ) {
        items(items = filteredMealLists) { meal ->
            when (mealViewSetting) {
                MealListOptions.List, null ->
                    MealItem9(
                        meal = meal,
                        mealVM = mealVM,
                        transferMeal = {
                            val ingredientList = mealVM.findMWI(meal.mealId)?.ingredients
                            if (ingredientList != null) {
                                listsToAddToGroceryList = ingredientList
                                transferFoodsToGroceryList = true
                            }
                        },
                        editMeal = { navToDetailsScreen(meal.mealId) },
                        deleteMeal = {
                            mealVM.deleteMeal(meal)
                            mealVM.deleteSpecificMealWithIngredients(meal.mealId)
                            Toast.makeText(
                                context,
                                "${meal.name} deleted from Meals",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        navToRecipeDetails = { navToRecipeDetails(meal.apiID) },
                        navToViewDetails = { navToViewDetails(meal.mealId) })

                MealListOptions.Images ->
                    VisualMealItem(
                        meal = meal,
                        transferMeal = {
                            val ingredientList = mealVM.findMWI(meal.mealId)?.ingredients
                            if (ingredientList != null) {
                                listsToAddToGroceryList = ingredientList
                                transferFoodsToGroceryList = true
                            }
                        },
                        editMeal = { navToDetailsScreen(meal.mealId) },
                        deleteMeal = {
                            mealVM.deleteMeal(meal)
                            mealVM.deleteSpecificMealWithIngredients(meal.mealId)
                            Toast.makeText(
                                context,
                                "${meal.name} deleted from Meals",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        navToRecipeDetails = { navToRecipeDetails(meal.apiID) },
                        navToViewDetails = { navToViewDetails(meal.mealId) }
                    )
            }
        }
    }
}

enum class MealListOptions {
    List,
    Images
}



