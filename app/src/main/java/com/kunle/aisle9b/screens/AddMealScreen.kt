package com.kunle.aisle9b.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kunle.aisle9b.models.Food
import com.kunle.aisle9b.models.Meal
import com.kunle.aisle9b.models.MealFoodMap
import com.kunle.aisle9b.navigation.GroceryScreens
import com.kunle.aisle9b.templates.EditFoodDialog9
import com.kunle.aisle9b.templates.ListItem9
import com.kunle.aisle9b.ui.theme.BaseOrange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMealScreen(
    modifier: Modifier = Modifier,
    shoppingViewModel: ShoppingViewModel,
    navController: NavController,
    screenHeader: (String) -> Unit
) {
    screenHeader(GroceryScreens.headerTitle(GroceryScreens.AddMealsScreen))

    val meal = Meal(name = "")
    var mealName by remember { mutableStateOf("") }
    var showEditFoodDialog by remember { mutableStateOf(false) }
//    val tempIngredientList = remember { mutableStateListOf<Food>() }

    if (showEditFoodDialog) {
        EditFoodDialog9(
            food = Food(name = "", quantity = "", isInGroceryList = false),
            setShowSelfDialog = { showEditFoodDialog = it },
            setFood = { shoppingViewModel.tempIngredientList.add(it) },
        )
    }

//    Dialog(onDismissRequest = { setShowAddMealDialog(false) }) {
    Column(
        modifier = modifier
            .padding(20.dp)
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(16.dp),
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        TextField(
            value = mealName,
            onValueChange = { mealName = it },
            placeholder = { Text(text = "Type meal name") },
            colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )

        Spacer(modifier = Modifier.height(15.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Filled.AddCircle,
                contentDescription = "Add button",
                tint = BaseOrange,
                modifier = Modifier
                    .size(48.dp)
                    .clickable {
                        showEditFoodDialog = true
                    }
            )
            Spacer(modifier = Modifier.width(40.dp))
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Delete button",
                tint = BaseOrange,
                modifier = Modifier
                    .size(48.dp)
                    .clickable {

                    }
            )
            Spacer(modifier = Modifier.width(40.dp))
            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = "Save button",
                tint = BaseOrange,
                modifier = Modifier
                    .size(48.dp)
                    .clickable {
                        shoppingViewModel.tempIngredientList.forEach {
                            shoppingViewModel.insertFood(it)
                            shoppingViewModel.insertMeal(
                                meal = Meal(mealId = meal.mealId, name = mealName)
                            )
                            shoppingViewModel.insertPair(
                                MealFoodMap(mealId = meal.mealId, foodId = it.foodId)
                            )
                        }
                        shoppingViewModel.tempIngredientList.clear()
                        navController.navigate(GroceryScreens.MealScreen.name)
                    }
            )

        }
        Spacer(modifier = Modifier.height(10.dp))
        LazyColumn {
            items(items = shoppingViewModel.tempIngredientList) {
                Log.d("Test", "lazyColumn: activated: $it")
                ListItem9(
                    food = it,
                    shoppingViewModel = shoppingViewModel,
                    checkBoxShown = false,
                    onEditClickNewFood = true
                )
            }
        }
    }
//    }
}