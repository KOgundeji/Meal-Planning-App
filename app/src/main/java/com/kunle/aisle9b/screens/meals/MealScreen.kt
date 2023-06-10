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
import androidx.navigation.NavController
import com.kunle.aisle9b.TopBarOptions
import com.kunle.aisle9b.models.Food
import com.kunle.aisle9b.navigation.GroceryScreens
import com.kunle.aisle9b.screens.SharedVM
import com.kunle.aisle9b.templates.CustomSearchBar9
import com.kunle.aisle9b.templates.items.MealItem9
import com.kunle.aisle9b.util.*

@Composable
fun MealScreen(
    modifier: Modifier = Modifier,
    shoppingVM: SharedVM,
    mealVM: MealVM,
    navController: NavController,
    topBar: (TopBarOptions) -> Unit,
    source: (GroceryScreens) -> Unit
) {
    source(GroceryScreens.MealScreen)

    val primaryButtonBar = shoppingVM.mealButtonBar.value
    var transferFoodsToGroceryList by remember { mutableStateOf(false) }

    val listsToAddToGroceryList = remember { mutableStateListOf(shoppingVM.groceryList.value) }
    var searchWord by remember { mutableStateOf("") }
    val context = LocalContext.current

    val mealList = mealVM.mealsList.collectAsState().value
    var filteredMealLists by remember { mutableStateOf(mealList) }

    if (transferFoodsToGroceryList) {
        val foodsForReconciliation = filterForReconciliation(
            lists = listsToAddToGroceryList,
            shoppingVM = shoppingVM
        )
        if (foodsForReconciliation.isNotEmpty()) {
            ReconciliationDialog(
                items = foodsForReconciliation,
                shoppingVM = shoppingVM,
                resetButtonBarToDefault = {
                    topBar(TopBarOptions.Default)
                    shoppingVM.mealButtonBar.value = MealButtonBar.Default
                }
            ) {
                transferFoodsToGroceryList = false
            }
        } else {
            topBar(TopBarOptions.Default)
            shoppingVM.mealButtonBar.value = MealButtonBar.Default
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
                    topAppBar = topBar,
                    onBackClick = {
                        topBar(TopBarOptions.Default)
                        shoppingVM.mealButtonBar.value = MealButtonBar.Default
                    },
                    onDeleteClick = {
                        shoppingVM.mealDeleteList.forEach { meal ->
                            mealVM.deleteMeal(meal)
                            mealVM.deleteSpecificMealIngredients(meal.mealId)
                        }
                        shoppingVM.mealButtonBar.value = MealButtonBar.Default
                    })
            }
            MealButtonBar.Transfer -> {
                AddMealToGroceryList_ButtonBar(
                    transferList = listsToAddToGroceryList,
                    topAppBar = topBar,
                    addLists = { transferFoodsToGroceryList = true }
                ) {
                    topBar(TopBarOptions.Default)
                    shoppingVM.mealButtonBar.value = MealButtonBar.Default
                }
            }
        }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            items(items = filteredMealLists) { meal ->
                MealItem9(
                    meal = meal,
                    primaryButtonBarAction = primaryButtonBar,
                    shoppingVM = shoppingVM,
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
    topAppBar: (TopBarOptions) -> Unit,
    onBackClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    topAppBar(TopBarOptions.Back)
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
    topAppBar: (TopBarOptions) -> Unit,
    addLists: () -> Unit,
    onBackClick: () -> Unit
) {
    topAppBar(TopBarOptions.Back)
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


