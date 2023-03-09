package com.kunle.aisle9b.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kunle.aisle9b.models.Food
import com.kunle.aisle9b.ui.theme.BaseOrange

@Composable
fun EditIngredients(ingredient: Food, onConfirmClick: () -> Food) {
    val ingredientName = remember {
        mutableStateOf(ingredient.name)
    }
    val quantity = remember {
        mutableStateOf(ingredient.quantity)
    }
    val category = remember {
        mutableStateOf(ingredient.category)
    }
    Surface(
        modifier = Modifier
            .height(350.dp)
            .width(250.dp),
        color = Color.Gray
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            TextField(
                value = ingredientName.value,
                onValueChange = {
                    if (it.all { char ->
                            char.isLetter() || char.isWhitespace()
                        }) ingredientName.value = it
                },
                placeholder = { Text(text = "Ingredient") },
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
            )
            TextField(
                value = quantity.value,
                onValueChange = {
                    if (it.all { char ->
                            char.isLetter() || char.isWhitespace()
                        }) quantity.value = it
                },
                placeholder = { Text(text = "How much/How many?") },
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
            )
            TextField(
                value = category.value,
                onValueChange = {
                    if (it.all { char ->
                            char.isLetter() || char.isWhitespace()
                        }) category.value = it
                },
                placeholder = { Text(text = "Select Category") },
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.AddCircle,
                    contentDescription = "Confirm button",
                    modifier = Modifier
                        .size(48.dp)
                        .clickable { onConfirmClick },
                    tint = BaseOrange
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = "Confirm",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 4.dp)
                )
                Spacer(modifier = Modifier.width(35.dp))
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Go back button",
                    modifier = Modifier
                        .size(48.dp)
                        .clickable { TODO("deflate screen") },
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
}