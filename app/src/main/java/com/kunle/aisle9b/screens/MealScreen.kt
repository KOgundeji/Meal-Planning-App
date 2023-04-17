package com.kunle.aisle9b.screens

import android.util.Log
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kunle.aisle9b.models.Meal
import com.kunle.aisle9b.navigation.GroceryScreens
import com.kunle.aisle9b.templates.AddMealDialog9
import com.kunle.aisle9b.templates.MealItem9
import com.kunle.aisle9b.ui.theme.BaseOrange

@Composable
fun MealScreen(
    shoppingViewModel: ShoppingViewModel,
    modifier: Modifier = Modifier,
    navController: NavController,
    screenHeader: (String) -> Unit
) {
    val mealHeader = GroceryScreens.headerTitle(GroceryScreens.MealScreen)
    screenHeader(mealHeader)

    var mealDeleteEnabled by remember { mutableStateOf(false) }
    var showAddMealDialog by remember { mutableStateOf(false) }
    val list = shoppingViewModel.tempIngredientList

    if (showAddMealDialog) {
        AddMealDialog9(meal = Meal(name = ""),
            shoppingViewModel = shoppingViewModel,
            setShowAddMealDialog = { showAddMealDialog = it })
    }

    Column(modifier = modifier.fillMaxSize()) {
        if (!mealDeleteEnabled) {
            AddDeleteBar(onAddClick = { showAddMealDialog = it },
                mealDeleteEnabled = { mealDeleteEnabled = it })
        } else {
            SubDeleteBar(shoppingViewModel = shoppingViewModel,
                mealDeleteEnabled = { mealDeleteEnabled = it },
                onDeleteClick = {
                    it.forEach { meal ->
                        shoppingViewModel.deleteMeal(meal)
                        shoppingViewModel.deleteSpecificMealIngredients(meal.mealId)
                    }
                    mealDeleteEnabled = false
                })
        }
        MealListContent(
            mealDeleteEnabled = mealDeleteEnabled,
            shoppingViewModel = shoppingViewModel
        )
    }
}

@Composable
fun AddDeleteBar(
    onAddClick: (Boolean) -> Unit,
    mealDeleteEnabled: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(vertical = 15.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Row(
            modifier = Modifier.clickable { onAddClick(true) },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Filled.AddCircle,
                contentDescription = "Add button",
                modifier = Modifier.size(48.dp),
                tint = BaseOrange
            )
            Text(
                text = "Add",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
            )
        }
        Row(
            modifier = Modifier.clickable { mealDeleteEnabled(true) },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Delete button",
                modifier = Modifier.size(48.dp),
                tint = BaseOrange
            )
            Text(
                text = "Delete",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
fun SubDeleteBar(
    shoppingViewModel: ShoppingViewModel,
    mealDeleteEnabled: (Boolean) -> Unit,
    onDeleteClick: (List<Meal>) -> Unit,
) {

    Row(
        modifier = Modifier
            .padding(vertical = 15.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Row(
            modifier = Modifier.clickable { onDeleteClick(shoppingViewModel.mealDeleteList) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Delete button",
                modifier = Modifier.size(48.dp),
                tint = BaseOrange
            )
            Text(
                text = "Delete",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Row(
            modifier = Modifier.clickable { mealDeleteEnabled(false) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Back button",
                modifier = Modifier.size(48.dp),
                tint = BaseOrange
            )
            Text(
                text = "Go Back",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun MealListContent(
    mealDeleteEnabled: Boolean,
    shoppingViewModel: ShoppingViewModel
) {
    val mealList = shoppingViewModel.mealList.collectAsState().value

    Column {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            items(items = mealList) {
                MealItem9(
                    meal = it,
                    deleteEnabled = mealDeleteEnabled,
                    shoppingViewModel = shoppingViewModel
                )
            }
        }
    }
}


