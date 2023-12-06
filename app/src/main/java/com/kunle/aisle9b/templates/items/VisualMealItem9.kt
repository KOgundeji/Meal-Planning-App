package com.kunle.aisle9b.templates.items

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.kunle.aisle9b.models.Meal
import com.kunle.aisle9b.util.ActionDropdown
import com.kunle.aisle9b.util.DropActions

@Preview
@Composable
fun VisualMealItem(
    meal: Meal = Meal.createBlank(),
    transferMeal: () -> Unit = {},
    editMeal: () -> Unit = {},
    deleteMeal: () -> Unit = {},
    navToRecipeDetails: () -> Unit = {},
    navToViewDetails: () -> Unit = {}
) {
    val bottomFade = Brush.verticalGradient(
        0F to Color.Transparent,
        .3F to Color.Black.copy(alpha = .5F),
        .5F to Color.Black.copy(alpha = 0.7F),
        1F to Color.Black.copy(alpha = 0.9F)
    )

    var dropdown by remember { mutableStateOf(false) }

    if (dropdown) {
        ActionDropdown(expanded = { dropdown = it }) { dropActions ->
            dropdown = when (dropActions) {
                DropActions.Edit -> {
                    if (meal.apiID <= 0) {
                        editMeal()
                    }
                    false
                }

                DropActions.Transfer -> {
                    transferMeal()
                    false
                }

                DropActions.Delete -> {
                    deleteMeal()
                    false
                }

                else -> {
                    false
                }
            }
        }
    }


    Card(
        modifier = Modifier
            .clickable {
                if (meal.apiID > 0) {
                    navToRecipeDetails()
                } else {
                    navToViewDetails()
                }
            }
            .fillMaxWidth(0.97f),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            AsyncImage(
                modifier = Modifier.fillMaxWidth(),
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
            IconButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .fillMaxWidth(),
                onClick = { dropdown = true }) {
                Icons.Filled.ListAlt
            }
            Text(
                text = meal.name,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .background(bottomFade)
                    .padding(start = 16.dp, top = 8.dp, bottom = 16.dp, end = 16.dp),
                fontWeight = FontWeight.ExtraBold,
                fontSize = 26.sp,
                lineHeight = 30.sp,
                color = Color.White
            )
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