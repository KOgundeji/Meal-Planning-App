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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.kunle.aisle9b.models.Food
import com.kunle.aisle9b.screens.ShoppingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditFoodDialog9(
    food: Food,
    shoppingViewModel: ShoppingViewModel,
    setShowSelfDialog: (Boolean) -> Unit,
    setFood: (Food) -> Unit
) {

    val ingredientName = remember {
        mutableStateOf(food.name)
    }
    val ingredientQuantity = remember {
        mutableStateOf(food.quantity)
    }
    val ingredientCategory = remember {
        mutableStateOf(food.category)
    }

    Dialog(onDismissRequest = { setShowSelfDialog(false) }) {
        Surface(shape = RoundedCornerShape(16.dp), color = Color.DarkGray) {
            Box(contentAlignment = Alignment.Center) {
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
                            tint = Color.White,
                            modifier = Modifier
                                .width(30.dp)
                                .height(30.dp)
                                .clickable { setShowSelfDialog(false) }
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    TextField(
                        value = ingredientName.value,
                        onValueChange = { ingredientName.value = it },
                        label = { Text(text = "Ingredient") },
                        placeholder = { Text(text = "Type food name") },
                        colors = TextFieldDefaults.textFieldColors(containerColor = Color.Gray),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                    )
                    TextField(
                        value = ingredientQuantity.value,
                        onValueChange = { ingredientQuantity.value = it },
                        label = { Text(text = "How much/How many?") },
                        placeholder = { Text(text = "Type quantity") },
                        colors = TextFieldDefaults.textFieldColors(containerColor = Color.Gray),
                    )
                    TextField(
                        value = ingredientCategory.value,
                        onValueChange = {
                            if (it.all { char ->
                                    char.isLetter() || char.isWhitespace()
                                }) ingredientCategory.value = it
                        },
                        label = { Text(text = "Select Category") },
                        placeholder = { Text(text = "Select Category") },
                        colors = TextFieldDefaults.textFieldColors(containerColor = Color.Gray),
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Box() {
                        Button(
                            onClick = {
                                val newFood = Food(
                                    foodId = food.foodId,
                                    name = ingredientName.value,
                                    quantity = ingredientQuantity.value,
                                    category = ingredientCategory.value,
                                    isInGroceryList = food.isInGroceryList
                                )
                                setFood(newFood)
                                Log.d("Test", "tempIngredientList")
                                setShowSelfDialog(false)
                            },
                            shape = RoundedCornerShape(50.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text(text = "Save")
                        }
                    }
                }
            }
        }
    }
}