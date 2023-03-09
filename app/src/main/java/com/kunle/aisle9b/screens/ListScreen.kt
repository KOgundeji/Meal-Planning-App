package com.kunle.aisle9b.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kunle.aisle9b.data.sampleFoodData
import com.kunle.aisle9b.models.Food
import com.kunle.aisle9b.templates.ListItem9
import com.kunle.aisle9b.ui.theme.BaseOrange

@Composable
fun ListScreen(shoppingViewModel: ShoppingViewModel, modifier: Modifier = Modifier) {
    Surface(modifier = modifier.fillMaxSize(1f)) {
        Column {
            GroceryInputTextField(
                modifier = Modifier.padding(top = 10.dp),
                onAddGrocery = { shoppingViewModel.insertFood(it) }
            )
            Spacer(modifier = Modifier.height(10.dp))
            Card(
                modifier = Modifier
                    .padding(top = 6.dp, bottom = 4.dp)
                    .height(60.dp)
                    .fillMaxWidth(),
                shape = RectangleShape,
                elevation = 6.dp,
                backgroundColor = BaseOrange,
            ) {
                Text(
                    text = "Grocery List",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Left,
                    color = Color.White
                )
            }
            LazyColumn {
                items(items = sampleFoodData) {
                    ListItem9(food = it) { food ->
                        shoppingViewModel.updateFood(food)
                    }
                }
            }
        }
    }
}

@Composable
fun GroceryInputTextField(
    modifier: Modifier = Modifier,
    onAddGrocery: (Food) -> Unit = {}
) {
    val context = LocalContext.current
    val item = remember {
        mutableStateOf("")
    }
    val quantity = remember {
        mutableStateOf("")
    }
    Card(
        modifier = modifier
            .padding(top = 6.dp, bottom = 4.dp)
            .height(60.dp)
            .fillMaxWidth(),
        shape = RectangleShape,
        elevation = 6.dp,
        backgroundColor = Color.White,
    ) {
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = item.value,
                onValueChange = {
                    if (it.all { char ->
                            char.isLetter() || char.isWhitespace()
                        }) item.value = it
                },
                modifier = Modifier.weight(.6f),
                placeholder = { Text(text = "Add item/meal") },
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
            )
            Spacer(modifier = Modifier.width(5.dp))
            TextField(
                value = quantity.value,
                onValueChange = {
                    if (it.all { char ->
                            char.isLetter() || char.isWhitespace()
                        }) quantity.value = it
                },
                modifier = Modifier.weight(.4f),
                placeholder = { Text(text = "How Many?") },
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
            )
            Spacer(modifier = Modifier.width(5.dp))
            Icon(
                imageVector = Icons.Filled.AddCircle,
                contentDescription = "Add button",
                tint = BaseOrange,
                modifier = Modifier
                    .size(48.dp)
                    .clickable {
                        if (item.value.isNotEmpty()) {
                            onAddGrocery(
                                Food(
                                    name = item.value,
                                    quantity = quantity.value,
                                    isInGroceryList = true
                                )
                            )
                            Toast.makeText(context, "${item.value} added", Toast.LENGTH_SHORT).show()
                            item.value = ""
                            quantity.value = ""
                        }
                    }
            )
            Spacer(modifier = Modifier.width(5.dp))
        }
    }
}

@Preview
@Composable
fun ListPreview() {
//    ListScreen()
}

