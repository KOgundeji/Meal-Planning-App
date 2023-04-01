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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kunle.aisle9b.models.Meal
import com.kunle.aisle9b.navigation.GroceryScreens
import com.kunle.aisle9b.templates.MealItem9
import com.kunle.aisle9b.ui.theme.BaseOrange

@Composable
fun MealScreen(
    shoppingViewModel: ShoppingViewModel,
    modifier: Modifier = Modifier,
    navController: NavController
) {

    Surface(modifier = modifier.fillMaxSize()) {
        Column {
            if (!shoppingViewModel.mealDeleteEnabled.value) {
                AddDeleteBar(
                    onAddClick = { navController.navigate(route = GroceryScreens.AddMealScreen.name) },
                    onDeleteClick = { shoppingViewModel.mealDeleteEnabled.value = true }
                )
            } else {
                SubDeleteBar(
                    onDeleteClick = {

                        shoppingViewModel.mealDeleteEnabled.value = false
                    }, //if item is checked, delete
                    onBackClick = { shoppingViewModel.mealDeleteEnabled.value = false })
            }
            MealListContent(
                deleteEnabled = shoppingViewModel.mealDeleteEnabled,
                onCheckBoxChecked = {},
                viewModel = shoppingViewModel
            ) { mealId ->
                Log.d("Screen", "MealScreen: activated")
                navController.navigate(route = GroceryScreens.EditMealScreen.name + "/$mealId")
            }
        }
    }
}

val fakeMealList: List<Meal> = listOf(
    Meal(name = "Pumpkin Pie"),
    Meal(name = "Risotto"),
    Meal(name = "Meat Lasagna")
)

@Composable
fun AddDeleteBar(onAddClick: () -> Unit, onDeleteClick: () -> Unit) {
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
                    .clickable { onAddClick },
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
                    .clickable { onDeleteClick },
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
fun SubDeleteBar(onDeleteClick: () -> Unit, onBackClick: () -> Unit) {
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
                    .clickable { onDeleteClick },
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
                    .clickable { onBackClick },
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
    viewModel: ShoppingViewModel,
    onCheckBoxChecked: (Int) -> Unit,
    onMealEditClick: (String?) -> Unit
) {
    val mealList = viewModel.mealList.collectAsState().value
    Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp)) {
        LazyColumn {
            items(items = mealList) {
                Log.d("Screen", "MealListContent: $it")
                MealItem9(
                    meal = it,
                    deleteEnabled = deleteEnabled.value,
                    onCheckBoxChecked = onCheckBoxChecked
                ) { meal ->
                    onMealEditClick(meal)
                }
            }
        }
    }
}

@Preview
@Composable
fun MealPreview() {
//    MealScreen()
}


