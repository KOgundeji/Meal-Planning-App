package com.kunle.aisle9b.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kunle.aisle9b.models.Meal
import com.kunle.aisle9b.navigation.GroceryScreens
import com.kunle.aisle9b.templates.AddMealDialog9
import com.kunle.aisle9b.templates.MealItem9
import com.kunle.aisle9b.ui.theme.BaseOrange

@Composable
fun MealScreen(
    shoppingViewModel: ShoppingViewModel,
    modifier: Modifier = Modifier,
    screenHeader: (String) -> Unit
) {
    val mealHeader = GroceryScreens.fullName(GroceryScreens.MealScreen)
    screenHeader(mealHeader)

    val mealDeleteEnabled = remember { mutableStateOf(false) }
    val showAddMealDialog = remember { mutableStateOf(false) }
    val list = shoppingViewModel.tempIngredientList

    list.forEach {
        Log.d("Test", "MealScreen: $it")
    }

    if (showAddMealDialog.value) {
        AddMealDialog9(
            meal = Meal(name = ""),
            shoppingViewModel = shoppingViewModel,
            setShowAddMealDialog = { showAddMealDialog.value = it })
    }

    Surface(modifier = modifier.fillMaxSize()) {
        Column {
            if (!mealDeleteEnabled.value) {
                AddDeleteBar(
                    onAddClick = { showAddMealDialog.value = it },
                    mealDeleteEnabled = { mealDeleteEnabled.value = it }
                )
            } else {
                SubDeleteBar(
                    shoppingViewModel = shoppingViewModel,
                    mealDeleteEnabled = { mealDeleteEnabled.value = it },
                    onDeleteClick = {
                        it.forEach { meal ->
                            shoppingViewModel.deleteMeal(meal)
                            shoppingViewModel.deleteSpecificMealIngredients(meal.mealId)
                        }
                        mealDeleteEnabled.value = false
                    })
            }
            MealListContent(
                deleteEnabled = mealDeleteEnabled,
                shoppingViewModel = shoppingViewModel
            )
        }
    }
}

val fakeMealList: List<Meal> = listOf(
    Meal(name = "Pumpkin Pie"),
    Meal(name = "Risotto"),
    Meal(name = "Meat Lasagna")
)

@Composable
fun AddDeleteBar(
    onAddClick: (Boolean) -> Unit,
    mealDeleteEnabled: (Boolean) -> Unit
) {
    Surface(
        modifier = Modifier
            .padding(top = 20.dp, bottom = 20.dp)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Filled.AddCircle,
                contentDescription = "Add button",
                modifier = Modifier
                    .size(48.dp)
                    .clickable { onAddClick(true) },
                tint = BaseOrange
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = "Add",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 4.dp)
            )
            Spacer(modifier = Modifier.width(35.dp))
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Delete button",
                modifier = Modifier
                    .size(48.dp)
                    .clickable { mealDeleteEnabled(true) },
                tint = BaseOrange
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = "Delete",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 4.dp)
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
    Surface(
        modifier = Modifier
            .padding(top = 20.dp, bottom = 20.dp)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Filled.AddCircle,
                contentDescription = "Delete button",
                modifier = Modifier
                    .size(48.dp)
                    .clickable { onDeleteClick(shoppingViewModel.mealDeleteList) },
                tint = BaseOrange
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = "Delete",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 4.dp)
            )
            Spacer(modifier = Modifier.width(35.dp))
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Back button",
                modifier = Modifier
                    .size(48.dp)
                    .clickable { mealDeleteEnabled(false) },
                tint = BaseOrange
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = "Go Back",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}

@Composable
fun MealListContent(
    deleteEnabled: MutableState<Boolean>,
    shoppingViewModel: ShoppingViewModel
) {
    val mealList = shoppingViewModel.mealList.collectAsState().value
    val mealListCount = mealList.size
    shoppingViewModel.screenList[1].name = "Meals ($mealListCount)"

    Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp)) {
        LazyColumn {
            items(items = mealList) {
                MealItem9(
                    meal = it,
                    deleteEnabled = deleteEnabled.value,
                    shoppingViewModel = shoppingViewModel
                )
            }
        }
    }
}

@Preview
@Composable
fun MealPreview() {
//    MealScreen()
}


