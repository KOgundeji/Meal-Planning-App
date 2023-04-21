package com.kunle.aisle9b.screens

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
import com.kunle.aisle9b.templates.MealItem9
import com.kunle.aisle9b.util.ReconciliationDialog
import com.kunle.aisle9b.util.filterForReconciliation

@Composable
fun MealScreen(
    shoppingVM: ShoppingVM,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    shoppingVM.screenHeader.value = GroceryScreens.headerTitle(GroceryScreens.MealScreen)
    shoppingVM.topBar.value = TopBarOptions.SearchEnabled
    shoppingVM.searchSource.value = GroceryScreens.MealScreen.name

    val context = LocalContext.current
    shoppingVM.filteredMeals.value = shoppingVM.mealList.collectAsState().value

    var primaryButtonBar by remember { mutableStateOf(MealButtonBar.Default) }
    var transferFoodsToGroceryList by remember { mutableStateOf(false) }
    val listsToAddToGroceryList = remember { mutableStateListOf(shoppingVM.groceryList.value) }

    if (transferFoodsToGroceryList) {
        val foodsForReconciliation = filterForReconciliation(
            lists = listsToAddToGroceryList,
            shoppingVM = shoppingVM
        )
        ReconciliationDialog(
            items = foodsForReconciliation,
            shoppingVM = shoppingVM,
            resetListLibraryToDefault = { primaryButtonBar = MealButtonBar.Default }
        ) {
            transferFoodsToGroceryList = it
        }
        Toast.makeText(context, "Groceries added to Grocery List", Toast.LENGTH_SHORT).show()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        when (primaryButtonBar) {
            MealButtonBar.Default -> {
                AddDeleteBar(
                    navController = navController,
                    primaryButtonBar = { primaryButtonBar = it })
            }
            MealButtonBar.Delete -> {
                FinalDeleteMeal_ButtonBar(
                    primaryButtonBar = { primaryButtonBar = it },
                    onDeleteClick = {
                        shoppingVM.mealDeleteList.forEach { meal ->
                            shoppingVM.deleteMeal(meal)
                            shoppingVM.deleteSpecificMealIngredients(meal.mealId)
                        }
                        primaryButtonBar = MealButtonBar.Default
                    })
            }
            MealButtonBar.Transfer -> {
                AddMealToGroceryList_ButtonBar(
                    transferList = listsToAddToGroceryList,
                    addLists = { transferFoodsToGroceryList = it }
                ) {
                    primaryButtonBar = MealButtonBar.Default
                }
            }
        }
        Column {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                items(items = shoppingVM.filteredMeals.value) {
                    MealItem9(
                        meal = it,
                        primaryButtonBarAction = primaryButtonBar,
                        shoppingVM = shoppingVM,
                        transferList = listsToAddToGroceryList
                    )
                }
            }
        }
    }
}

@Composable
fun AddDeleteBar(
    navController: NavController,
    primaryButtonBar: (MealButtonBar) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            modifier = Modifier.width(75.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            ),
            onClick = { navController.navigate(GroceryScreens.AddMealsScreen.name) }) {
            Icon(
                imageVector = Icons.Filled.AddCircle,
                contentDescription = "Add button",
                modifier = Modifier.size(30.dp)
            )
        }
        Button(
            modifier = Modifier.width(75.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            ),
            onClick = { primaryButtonBar(MealButtonBar.Delete) }) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Delete button",
                modifier = Modifier.size(30.dp)
            )
        }
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            ),
            onClick = { primaryButtonBar(MealButtonBar.Transfer) }) {
            Icon(
                modifier = Modifier.size(30.dp),
                imageVector = Icons.Filled.DriveFileMoveRtl,
                contentDescription = null
            )
        }
    }
}

@Composable
fun FinalDeleteMeal_ButtonBar(
    primaryButtonBar: (MealButtonBar) -> Unit,
    onDeleteClick: () -> Unit,
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            modifier = Modifier.width(75.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            ),
            onClick = { primaryButtonBar(MealButtonBar.Default) }) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Back button",
                modifier = Modifier.size(30.dp)
            )
        }
        Button(
            modifier = Modifier.width(75.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
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
    addLists: (Boolean) -> Unit,
    primaryButtonBar: (MealButtonBar) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Button(
            onClick = { primaryButtonBar(MealButtonBar.Default) },
            modifier = Modifier.width(75.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
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
                    addLists(true)
                }
            },
            modifier = Modifier.width(75.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
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


