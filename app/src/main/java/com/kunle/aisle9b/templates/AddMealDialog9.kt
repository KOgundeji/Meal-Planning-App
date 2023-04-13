package com.kunle.aisle9b.templates

import android.util.Log
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.kunle.aisle9b.models.Food
import com.kunle.aisle9b.models.Meal
import com.kunle.aisle9b.models.MealFoodMap
import com.kunle.aisle9b.screens.ShoppingViewModel
import com.kunle.aisle9b.ui.theme.BaseOrange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMealDialog9(
    meal: Meal,
    shoppingViewModel: ShoppingViewModel,
    setShowAddMealDialog: (Boolean) -> Unit
) {

    shoppingViewModel.tempIngredientList.forEach {
        Log.d("Test", "AddMealDialog: $it")
    }

    val mealName = remember {
        mutableStateOf("")
    }
    val showEditFoodDialog = remember {
        mutableStateOf(false)
    }


    if (showEditFoodDialog.value) {
        EditFoodDialog9(
            food = Food(name = "", quantity = "", isInGroceryList = false),
            shoppingViewModel = shoppingViewModel,
            setShowSelfDialog = { showEditFoodDialog.value = it },
            setFood = { shoppingViewModel.addIngredient(it) },
        )
    }

    Dialog(onDismissRequest = { setShowAddMealDialog(false) }) {
        Surface(shape = RoundedCornerShape(16.dp), color = Color.DarkGray) {
            Box(contentAlignment = Alignment.Center) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Close button",
                            tint = Color.White,
                            modifier = Modifier
                                .width(30.dp)
                                .height(30.dp)
                                .clickable { setShowAddMealDialog(false) }
                        )
                    }
                    TextField(
                        value = mealName.value,
                        onValueChange = { mealName.value = it },
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
                                    showEditFoodDialog.value = true
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
                                            meal = Meal(mealId = meal.mealId, name = mealName.value)
                                        )
                                        shoppingViewModel.insertPair(
                                            MealFoodMap(mealId = meal.mealId, foodId = it.foodId)
                                        )
                                    }
                                    shoppingViewModel.tempIngredientList.clear()
                                    setShowAddMealDialog(false)
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
            }
        }
    }
}