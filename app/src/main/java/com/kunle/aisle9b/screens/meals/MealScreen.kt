package com.kunle.aisle9b.screens.meals

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.kunle.aisle9b.templates.CustomSearchBar9
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

    val searchWord = mealVM.searchWord.collectAsState().value
    val filteredMealLists = mealVM.filteredMealList.collectAsState().value
    val viewListOption = mealVM.viewListOption.collectAsState().value
    val listSpacing = if (viewListOption == MealListOptions.List) {
        5.dp
    } else {
        30.dp
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

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomSearchBar9(
                modifier = Modifier.fillMaxWidth(.75f),
                text = searchWord,
                onValueChange = { string ->
                    mealVM.setSearchWord(string)
                },
                label = "Search in Meals",
                trailingIcon = {
                    if (searchWord.isNotEmpty()) {
                        IconButton(onClick = {
                            mealVM.setSearchWord("")
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Cancel,
                                contentDescription = "Cancel button",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                },
            )
            IconButton(onClick = { mealVM.setViewMealListOption(MealListOptions.List) }) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = Icons.Filled.FormatListBulleted,
                    contentDescription = "List Option",
                    tint = if (viewListOption == MealListOptions.List) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onBackground
                    }
                )
            }
            IconButton(onClick = { mealVM.setViewMealListOption(MealListOptions.Images) }) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = Icons.Filled.Image,
                    contentDescription = "Image Option",
                    tint = if (viewListOption == MealListOptions.Images) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onBackground
                    }
                )
            }
        }

        LazyColumn(
            modifier = Modifier.padding(top = 10.dp),
            verticalArrangement = Arrangement.spacedBy(listSpacing)
        ) {
            items(items = filteredMealLists) { meal ->
                when (viewListOption) {
                    MealListOptions.List ->
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
}

enum class MealListOptions() {
    List,
    Images
}



