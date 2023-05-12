package com.kunle.aisle9b.templates

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.kunle.aisle9b.models.Food
import com.kunle.aisle9b.util.CategoryDropDownMenu

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditFoodDialog9(
    food: Food,
    closeDialog: () -> Unit,
    setFood: (Food) -> Unit
) {
    var ingredientName by remember { mutableStateOf(food.name) }
    var ingredientQuantity by remember { mutableStateOf(food.quantity) }
    var ingredientCategory by remember { mutableStateOf(food.category) }

    Dialog(onDismissRequest = { closeDialog() }) {
        Surface(shape = RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.background) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Modify",
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close button",
                        modifier = Modifier
                            .width(30.dp)
                            .height(30.dp)
                            .clickable { closeDialog() }
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                TextField(
                    value = ingredientName,
                    onValueChange = { ingredientName = it },
                    label = { Text(text = "Ingredient") },
                    placeholder = { Text(text = "Type food name") },
                    colors = TextFieldDefaults.textFieldColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    shape = RectangleShape
                )
                TextField(
                    value = ingredientQuantity,
                    onValueChange = { ingredientQuantity = it },
                    label = { Text(text = "How much/How many?") },
                    placeholder = { Text(text = "Type quantity") },
                    colors = TextFieldDefaults.textFieldColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    shape = RectangleShape
                )
                CategoryDropDownMenu(
                    category = ingredientCategory,
                    newCategory = { ingredientCategory = it })
                Spacer(modifier = Modifier.height(20.dp))
                Box() {
                    Button(
                        onClick = {
                            val newFood = Food(
                                foodId = food.foodId,
                                name = ingredientName,
                                quantity = ingredientQuantity,
                                category = ingredientCategory,
                                isInGroceryList = food.isInGroceryList
                            )
                            setFood(newFood)
                            closeDialog()
                        },
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(text = "Save", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }

        }
    }
}

