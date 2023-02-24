package com.kunle.aisle9b.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kunle.aisle9b.models.Meal
import com.kunle.aisle9b.templates.MealItem9
import com.kunle.aisle9b.ui.theme.BaseOrange
import com.kunle.aisle9b.ui.theme.OrangeShade

@Composable
fun MealScreen() {
    Scaffold(topBar = {
        TopAppBar(
            backgroundColor = BaseOrange,
            elevation = 5.dp,
        ) {
            Row(horizontalArrangement = Arrangement.Start) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back arrow",
                    modifier = Modifier
                        .size(36.dp)
                        .align(Alignment.CenterVertically),
                )
                Text(
                    text = "Meals",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.padding(start = 10.dp)
                )
            }
        }
    }) {
        Surface(modifier = Modifier.padding(it)) {
            Column {
                AddDeleteBar()
                MealListContent()
            }

        }
    }

}

val fakeMealList: List<Meal> = listOf(
    Meal(mealId = 1, name = "Pumpkin Pie"),
    Meal(mealId = 2, name = "Risotto"),
    Meal(mealId = 3, name = "Meat Lasagna")
)

@Composable
fun AddDeleteBar() {
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
                modifier = Modifier.size(48.dp),
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
                modifier = Modifier.size(48.dp),
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
fun MealListContent() {
    Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp)) {
        LazyColumn {
            items(items = fakeMealList) {
                MealItem9(meal = it)
            }
        }
    }
}

@Preview
@Composable
fun MealPreview() {
    MealScreen()
}


