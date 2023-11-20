package com.kunle.aisle9b.screens.meals

import android.util.Log
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kunle.aisle9b.TopBarOptions
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

    val primaryButtonBar = generalVM.mealButtonBar.value
    var transferFoodsToGroceryList by remember { mutableStateOf(false) }

    val listsToAddToGroceryList = remember { mutableStateListOf(generalVM.groceryList.value) }
    var searchWord by remember { mutableStateOf("") }
    val context = LocalContext.current

    val mealList = mealVM.mealList.collectAsState().value
    Log.d("Test", "mealList size: ${mealList.size}")
    var filteredMealLists by remember { mutableStateOf(mealList) }
    Log.d("Test", "filteredMeal size: ${filteredMealLists.size}")

    if (transferFoodsToGroceryList) {
        val foodsForReconciliation =
            generalVM.filterForReconciliation(
            lists = listsToAddToGroceryList)

        if (foodsForReconciliation.isNotEmpty()) {
            ReconciliationDialog(
                items = foodsForReconciliation,
                viewModel = mealVM,
                resetButtonBarToDefault = {
                    generalVM.setTopBarOption(TopBarOptions.Default)
                    generalVM.mealButtonBar.value = MealButtonBar.Default
                }
            ) {
                transferFoodsToGroceryList = false
            }
        } else {
            generalVM.setTopBarOption(TopBarOptions.Default)
            generalVM.mealButtonBar.value = MealButtonBar.Default
        }
        Toast.makeText(context, "Groceries added to Grocery List", Toast.LENGTH_SHORT).show()
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
        when (primaryButtonBar) {
            MealButtonBar.Default -> {}
            MealButtonBar.Delete -> {
                FinalDeleteMeal_ButtonBar(
                    generalVM = generalVM,
                    onBackClick = {
                        generalVM.setTopBarOption(TopBarOptions.Default)
                        generalVM.mealButtonBar.value = MealButtonBar.Default
                    },
                    onDeleteClick = {
                        generalVM.mealDeleteList.forEach { meal ->
                            mealVM.deleteMeal(meal)
                            mealVM.deleteSpecificMealWithIngredients(meal.mealId)
                        }
                        generalVM.mealButtonBar.value = MealButtonBar.Default
                    })
            }
            MealButtonBar.Transfer -> {
                AddMealToGroceryList_ButtonBar(
                    transferList = listsToAddToGroceryList,
                    generalVM = generalVM,
                    addLists = { transferFoodsToGroceryList = true }
                ) {
                    generalVM.setTopBarOption(TopBarOptions.Default)
                    generalVM.mealButtonBar.value = MealButtonBar.Default
                }
            }
        }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            items(items = filteredMealLists) { meal ->
                Log.d("Test", "Meal Screens items list")
                MealItem9(
                    meal = meal,
                    primaryButtonBarAction = primaryButtonBar,
                    shoppingVM = generalVM,
                    mealVM = mealVM,
                    navController = navController,
                    transferList = listsToAddToGroceryList
                )
            }
        }
    }
}

@Composable
fun FinalDeleteMeal_ButtonBar(
    generalVM: GeneralVM,
    onBackClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    generalVM.setTopBarOption(TopBarOptions.Back)
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            modifier = Modifier.width(75.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
            ),
            onClick = {
                onBackClick()
            }) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Back button",
                modifier = Modifier.size(30.dp)
            )
        }
        Button(
            modifier = Modifier.width(75.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
            ),
            onClick = { onDeleteClick() }) {
            Icon(
                imageVector = Icons.Filled.DeleteForever,
                contentDescription = "Delete button",
                modifier = Modifier.size(30.dp)
            )
        }
    }
}

@Composable
fun AddMealToGroceryList_ButtonBar(
    transferList: MutableList<List<Food>>,
    generalVM: GeneralVM,
    addLists: () -> Unit,
    onBackClick: () -> Unit
) {
    generalVM.setTopBarOption(TopBarOptions.Back)
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Button(
            onClick = {
                onBackClick()
            },
            modifier = Modifier.width(75.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
            )
        ) {
            Icon(
                modifier = Modifier.size(30.dp),
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Back button"
            )
        }
        Button(
            onClick = {
                if (transferList.isNotEmpty()) {
                    addLists()
                }
            },
            modifier = Modifier.width(75.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
            )
        ) {
            Icon(
                modifier = Modifier.size(30.dp),
                imageVector = Icons.Filled.DriveFileMoveRtl,
                contentDescription = "transfer to grocery list button"
            )
        }
    }
}

enum class MealButtonBar {
    Default,
    Delete,
    Transfer;
}


