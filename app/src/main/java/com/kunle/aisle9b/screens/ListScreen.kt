package com.kunle.aisle9b.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kunle.aisle9b.data.sampleFoodData
import com.kunle.aisle9b.templates.GroceryInputTextField
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
                    ListItem9(food = it)
                }
            }
        }
    }
}

@Preview
@Composable
fun ListPreview() {
//    ListScreen()
}

