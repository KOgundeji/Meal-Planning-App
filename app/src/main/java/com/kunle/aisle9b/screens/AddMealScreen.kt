package com.kunle.aisle9b.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.kunle.aisle9b.models.Food
import com.kunle.aisle9b.templates.ListItem9
import com.kunle.aisle9b.ui.theme.BaseOrange

@Composable
fun AddMealScreen() {
    val context = LocalContext.current
    val mealName = remember {
        mutableStateOf("")
    }
    val ingredientList = emptyList<Food>()
    Surface(
        modifier = Modifier
            .height(400.dp)
            .width(250.dp),
        color = Color.Gray
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            TextField(
                modifier = Modifier.padding(vertical = 6.dp),
                value = mealName.value,
                placeholder = { Text(text = "Enter name of meal") },
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
                onValueChange = {
                    if (it.all { char ->
                            char.isLetter() || char.isWhitespace()
                        }) mealName.value = it
                }
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                Icon(
                    imageVector = Icons.Filled.AddCircle,
                    contentDescription = "Add button",
                    tint = BaseOrange,
                    modifier = Modifier
                        .size(48.dp)
                        .weight(.25f)
                        .clickable {
                            TODO("inflate add ingredient screen")
                        }
                )
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete button",
                    tint = BaseOrange,
                    modifier = Modifier
                        .size(48.dp)
                        .weight(.25f)
                        .clickable {
                            TODO("inflate add ingredient screen")
                        }
                )
                Icon(
                    imageVector = Icons.Filled.Create,
                    contentDescription = "Save button",
                    tint = BaseOrange,
                    modifier = Modifier
                        .size(48.dp)
                        .weight(.25f)
                        .clickable {
                            if (mealName.value.isNotEmpty()) {

                            }
                        }
                )
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back Arrow",
                    tint = BaseOrange,
                    modifier = Modifier
                        .size(48.dp)
                        .weight(.25f)
                        .clickable {
                            TODO("deflate screen")
                        }
                )
            }
            LazyColumn {
                items(items = ingredientList) {
                    ListItem9(food = it, checkBoxEnabled = false) { food ->

                    }
                }
            }
        }
    }
}