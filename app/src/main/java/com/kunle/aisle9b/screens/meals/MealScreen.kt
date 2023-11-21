package com.kunle.aisle9b.screens.meals

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kunle.aisle9b.models.Food
import com.kunle.aisle9b.navigation.GroceryScreens
import com.kunle.aisle9b.screens.GeneralVM
import com.kunle.aisle9b.templates.CustomSearchBar9
import com.kunle.aisle9b.templates.items.MealItem9
import com.kunle.aisle9b.util.*

@Composable
fun MealScreen(
    modifier: Modifier = Modifier,
    generalVM: GeneralVM = hiltViewModel(),
    mealVM: MealVM = hiltViewModel(),
    navController: NavController
) {
    generalVM.setClickSource(GroceryScreens.MealScreen)

    var transferFoodsToGroceryList by remember { mutableStateOf(false) }

    var listsToAddToGroceryList by remember { mutableStateOf(emptyList<Food>()) }
    var searchWord by remember { mutableStateOf("") }
    val context = LocalContext.current

    val mealList = mealVM.mealList.collectAsState().value
    var filteredMealLists by remember { mutableStateOf(mealList) }

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
            Toast.makeText(context, "Groceries added to Grocery List", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        CustomSearchBar9(
            text = searchWord,
            onValueChange = { string ->
                searchWord = string
                filteredMealLists = mealList.filter { meal ->
                    meal.name.contains(searchWord, ignoreCase = true)
                }
            },
            label = "Search in Meals",
            trailingIcon = {
                if (searchWord.isNotEmpty()) {
                    IconButton(onClick = {
                        searchWord = ""
                        filteredMealLists = mealList
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
        LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            items(items = filteredMealLists) { meal ->
                MealItem9(
                    meal = meal,
                    mealVM = mealVM,
                    navController = navController,
                    deleteMeal = {
                        mealVM.deleteMeal(meal)
                        mealVM.deleteSpecificMealWithIngredients(meal.mealId)
                    },
                    transferMeal = { ingredients ->
                        listsToAddToGroceryList = ingredients
                        transferFoodsToGroceryList = true
                    }
                )
            }
        }
    }
}



