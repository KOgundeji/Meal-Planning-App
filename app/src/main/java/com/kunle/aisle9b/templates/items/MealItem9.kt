package com.kunle.aisle9b.templates.items

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.kunle.aisle9b.models.MealWithIngredients
import com.kunle.aisle9b.util.Constants

@Composable
fun MealItem9(
    mealWithIngredients: MealWithIngredients?,
    showBottomSheet: () -> Unit,
) {
    if (mealWithIngredients != null) {
        val ingredientsList = mealWithIngredients.ingredients
        val meal = mealWithIngredients.meal

        val listedIngredients: String =
            when {
                ingredientsList.isNotEmpty() -> ingredientsList.joinToString { it.name }
                meal.apiID > 0 -> "Sourced from Spoonacular API"
                else -> "No ingredients yet"
            }

        Card(
            modifier = Modifier
                .padding(horizontal = 6.dp)
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
            shape = RoundedCornerShape(corner = CornerSize(6.dp))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(.9f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        modifier = Modifier.size(60.dp).fillMaxHeight(),
                        model =
                        if (meal.apiID > 0) {
                            meal.apiImageURL
                        } else if (meal.apiID < 0 && meal.mealPic != Uri.EMPTY) {
                            meal.mealPic
                        } else {
                            Constants.GENERIC_IMG
                        },
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 10.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = meal.name,
                            color = MaterialTheme.colorScheme.inversePrimary,
                            fontSize = 18.sp,
                            lineHeight = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = listedIngredients,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            fontSize = 14.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                IconButton(onClick = { showBottomSheet() }) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "Additional screen options",
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }
    }
}