package com.kunle.aisle9b.screens

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kunle.aisle9b.templates.ListItem9
import com.kunle.aisle9b.ui.theme.BaseOrange
import java.util.*


@Composable
fun EditMealScreen(
    modifier: Modifier,
    navController: NavController,
    shoppingViewModel: ShoppingViewModel,
    MWI_ID: String?
) {
    val convertedMWIUUID = UUID.fromString(MWI_ID)
    val mwi = shoppingViewModel.mealWithIngredientsList.collectAsState().value.first { MWI ->
        MWI.meal.mealId == convertedMWIUUID
    }
    val context = LocalContext.current
    val mealName = remember {
        mutableStateOf(mwi.meal.name)
    }
    val ingredientList = remember {
        mutableStateListOf(mwi.foods)
    }
    Surface(
        modifier = modifier
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
                            TODO("inflate delete ingredient screen")
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
                items(items = ingredientList[0]) {
                    ListItem9(food = it, viewModel = shoppingViewModel, checkBoxEnabled = false)
                }
            }
        }
    }
}