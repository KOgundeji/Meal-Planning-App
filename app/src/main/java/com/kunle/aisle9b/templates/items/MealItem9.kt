package com.kunle.aisle9b.templates.items

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kunle.aisle9b.R
import com.kunle.aisle9b.models.Food
import com.kunle.aisle9b.models.Meal
import com.kunle.aisle9b.models.MealWithIngredients
import com.kunle.aisle9b.navigation.GroceryScreens
import com.kunle.aisle9b.screens.meals.MealVM
import com.kunle.aisle9b.util.ActionDropdown
import com.kunle.aisle9b.util.DropActions

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MealItem9(
    meal: Meal,
    mealVM: MealVM,
    navController: NavController,
    deleteMeal: () -> Unit,
    transferMeal: (List<Food>) -> Unit
) {
    var longPress by remember { mutableStateOf(false) }
    val haptics = LocalHapticFeedback.current

    val mwiList = mealVM.mealsWithIngredients.collectAsState().value
    val mwi = mwiList.find { mealWI ->
        mealWI.meal.mealId == meal.mealId
    }

    val listedIngredients: String =
        if (mwi?.ingredients?.isNotEmpty() == true) {
            mwi.ingredients.joinToString { it.name }
        } else if (mwi?.meal?.apiID != null) {
            "Sourced from Spoonacular API"
        } else {
            ""
        }

    if (longPress) {
        ActionDropdown(expanded = { longPress = it }) { dropActions ->
            longPress = when (dropActions) {
                DropActions.Edit -> {
                    moveToMealDetailsScreen(meal, navController, mwiList, mwi)
                    false
                }

                DropActions.Transfer -> {
                    if (mwi?.ingredients?.isNotEmpty() == true) {
                        transferMeal(mwi.ingredients)
                    }
                    false
                }

                DropActions.Delete -> {
                    deleteMeal()
                    false
                }
            }
        }
    }

    Card(
        modifier = Modifier
            .padding(horizontal = 6.dp)
            .fillMaxWidth()
            .combinedClickable(
                enabled = true, onLongClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    longPress = true
                },
                onLongClickLabel = "Action Dropdown"
            ) {},
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
                Image(
                    painter = painterResource(id = R.drawable.food),
                    contentDescription = "food",
                    modifier = Modifier.size(30.dp),
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primary)
                )
                Spacer(modifier = Modifier.width(2.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 3.dp),
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


private fun moveToMealDetailsScreen(
    meal: Meal,
    navController: NavController,
    mwiList: List<MealWithIngredients>,
    mwi: MealWithIngredients?
) {
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