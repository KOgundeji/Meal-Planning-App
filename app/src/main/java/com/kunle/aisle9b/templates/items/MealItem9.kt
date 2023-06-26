package com.kunle.aisle9b.templates.items

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kunle.aisle9b.R
import com.kunle.aisle9b.models.Food
import com.kunle.aisle9b.models.Meal
import com.kunle.aisle9b.navigation.GroceryScreens
import com.kunle.aisle9b.screens.GeneralVM
import com.kunle.aisle9b.screens.meals.MealButtonBar
import com.kunle.aisle9b.screens.meals.MealVM

@Composable
fun MealItem9(
    meal: Meal,
    primaryButtonBarAction: MealButtonBar,
    shoppingVM: GeneralVM,
    mealVM: MealVM,
    navController: NavController,
    transferList: MutableList<List<Food>>
) {
    var isChecked by remember { mutableStateOf(false) }

    val mwiList = mealVM.mealsWithIngredients.collectAsState().value
    val mwi = mwiList.find { MWI ->
        MWI.meal.mealId == meal.mealId
    }

    val listedIngredients: String =
        if (mwi?.ingredients?.isNotEmpty() == true) {
            mwi.ingredients.joinToString { it.name }
        } else if (mwi?.meal?.apiID != null) {
            "Sourced from Spoonacular API"
        } else {
            ""
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
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 10.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(.9f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                when (primaryButtonBarAction) {
                    MealButtonBar.Delete -> {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = {
                                isChecked = !isChecked
                                if (isChecked) {
                                    shoppingVM.mealDeleteList.add(meal)
                                } else {
                                    shoppingVM.mealDeleteList.remove(meal)
                                }
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = MaterialTheme.colorScheme.secondaryContainer,
                                uncheckedColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                checkmarkColor = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    MealButtonBar.Transfer -> {
                        if (meal.apiID < 0) {
                            Icon(
                                modifier = Modifier
                                    .clickable {
                                        isChecked = !isChecked
                                        if (isChecked) {
                                            transferList.add(mwi!!.ingredients)
                                        } else {
                                            transferList.remove(mwi!!.ingredients)
                                        }
                                    }
                                    .size(32.dp)
                                    .border(
                                        border = BorderStroke(
                                            1.dp,
                                            color = MaterialTheme.colorScheme.tertiary
                                        ),
                                        shape = CircleShape
                                    ),
                                imageVector = Icons.Filled.ArrowCircleLeft,
                                contentDescription = "Transfer button",
                                tint = if (!isChecked) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                    }
                    else -> {
                        Image(
                            painter = painterResource(id = R.drawable.food),
                            contentDescription = "food",
                            modifier = Modifier.size(30.dp),
                            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primary)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 3.dp)
                        .clickable {
                            if (primaryButtonBarAction == MealButtonBar.Default) {
                                if (meal.apiID > 0) {
                                    val recipeId = meal.apiID
                                    navController.navigate(GroceryScreens.RecipeDetailsScreen.name + "/${recipeId}")
                                } else {
                                    val index = mwiList.indexOf(mwi)
                                    navController.navigate(
                                        GroceryScreens.MealDetailsScreen.name + "/${index}"
                                    )
                                }
                            }
                        },
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = meal.name,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = listedIngredients,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}