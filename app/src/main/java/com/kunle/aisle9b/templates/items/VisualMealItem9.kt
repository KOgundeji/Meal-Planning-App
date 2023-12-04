package com.kunle.aisle9b.templates.items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleLeft
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.kunle.aisle9b.models.Meal

@Preview
@Composable
fun VisualMealItem(
    meal: Meal = Meal.createBlank(),
    screenWidth: Dp = 325.dp,
    transferMeal: () -> Unit = {},
    editMeal: () -> Unit = {},
    deleteMeal: () -> Unit = {},
    navToRecipeDetails: () -> Unit = {},
    navToViewDetails: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .width(screenWidth)
            .height(400.dp)
    ) {
        Surface(
            color = MaterialTheme.colorScheme.tertiaryContainer,
            shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp, vertical = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SelectOption(
                    image = Icons.Filled.ArrowCircleLeft,
                    text = "Transfer",
                    onClick = { transferMeal() })
                SelectOption(
                    image = Icons.Filled.EditNote,
                    text = "Edit Meal",
                    onClick = {
                        if (meal.apiID <= 0) {
                            editMeal()
                        }
                    })
                SelectOption(
                    image = Icons.Filled.Delete,
                    text = "Delete",
                    onClick = { deleteMeal() })
            }
        }
        Card(
            modifier = Modifier.clickable {
                if (meal.apiID > 0) {
                    navToRecipeDetails()
                } else {
                    navToViewDetails()
                }
            },
            shape = RoundedCornerShape(0.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.7f),
                model =
                if (meal.apiID <= 0) {
                    meal.mealPic
                } else {
                    meal.apiImageURL
                },
                contentDescription = null,
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
            )
        }
        Surface(
            shape = RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp),
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Column {
                Text(
                    text = meal.name,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(start = 16.dp, top = 8.dp, bottom = 16.dp, end = 16.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 26.sp,
                    lineHeight = 30.sp
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(text = "Test 1")
                    VerticalDivider()
                    Text(text = "Test 2")
                    VerticalDivider()
                    Text(text = "Test 3")
                }
            }
        }
    }
}

@Composable
private fun VerticalDivider() {
    Divider(
        modifier = Modifier
            .fillMaxHeight(.8f)
            .width(1.dp),
        color = MaterialTheme.colorScheme.onPrimaryContainer
    )
}


@Composable
private fun SelectOption(
    image: ImageVector,
    text: String,
    contentDescription: String? = null,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 3.dp)
            .clickable { onClick() },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = image,
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.onPrimary,
        )
        Text(
            text = text,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onPrimary,
            maxLines = 1
        )
    }
}