package com.kunle.aisle9b.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kunle.aisle9b.TopBarOptions
import com.kunle.aisle9b.navigation.GroceryScreens
import com.kunle.aisle9b.templates.MealItem9

@Composable
fun MealScreen(
    shoppingVM: ShoppingVM,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    shoppingVM.screenHeader.value = GroceryScreens.headerTitle(GroceryScreens.MealScreen)
    shoppingVM.topBar.value = TopBarOptions.SearchEnabled

    var mealDeleteEnabled by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxSize()) {
        if (!mealDeleteEnabled) {
            AddDeleteBar(navController = navController,
                mealDeleteEnabled = { mealDeleteEnabled = it })
        } else {
            SubDeleteBar(shoppingVM = shoppingVM,
                navController = navController,
                mealDeleteEnabled = { mealDeleteEnabled = it },
                onDeleteClick = {
                    shoppingVM.mealDeleteList.forEach { meal ->
                        shoppingVM.deleteMeal(meal)
                        shoppingVM.deleteSpecificMealIngredients(meal.mealId)
                    }
                    mealDeleteEnabled = false
                })
        }
        MealListContent(
            mealDeleteEnabled = mealDeleteEnabled,
            shoppingVM = shoppingVM
        )
    }
}

@Composable
fun AddDeleteBar(
    navController: NavController,
    mealDeleteEnabled: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(vertical = 15.dp)
            .fillMaxWidth(),
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
            onClick = { mealDeleteEnabled(true) }) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Delete button",
                modifier = Modifier.size(30.dp)
            )
        }
    }
}

@Composable
fun SubDeleteBar(
    shoppingVM: ShoppingVM,
    navController: NavController,
    mealDeleteEnabled: (Boolean) -> Unit,
    onDeleteClick: () -> Unit,
) {

    Row(
        modifier = Modifier
            .padding(vertical = 15.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            modifier = Modifier.width(75.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            ),
            onClick = { mealDeleteEnabled(false) }) {
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
fun MealListContent(
    mealDeleteEnabled: Boolean,
    shoppingVM: ShoppingVM
) {
    val mealList = shoppingVM.mealList.collectAsState().value

    Column {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            items(items = mealList) {
                MealItem9(
                    meal = it,
                    deleteEnabled = mealDeleteEnabled,
                    shoppingVM = shoppingVM
                )
            }
        }
    }
}


