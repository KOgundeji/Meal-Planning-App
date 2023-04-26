package com.kunle.aisle9b.templates

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kunle.aisle9b.R
import com.kunle.aisle9b.models.Food
import com.kunle.aisle9b.models.Meal
import com.kunle.aisle9b.screens.MealButtonBar
import com.kunle.aisle9b.screens.ShoppingVM

@Composable
fun MealItem9(
    meal: Meal,
    primaryButtonBarAction: MealButtonBar,
    shoppingVM: ShoppingVM,
    transferList: MutableList<List<Food>>
) {
    var isChecked by remember { mutableStateOf(false) }
    var showEditMealDialog by remember { mutableStateOf(false) }
    val mwi = shoppingVM.mealWithIngredientsList.collectAsState().value.find { MWI ->
        MWI.meal.mealId == meal.mealId
    }
    val listedIngredients: String = mwi?.foods
        ?.joinToString(separator = ", ") { it.name } ?: "" //its the default separator, but wanted to include anyway

    if (showEditMealDialog) {
        ModifyIngredientsDialog9(
            id = meal.mealId,
            source = EditSource.Meal,
            shoppingVM = shoppingVM,
            setShowDialog = { showEditMealDialog = false })
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
            horizontalArrangement = Arrangement.SpaceBetween,
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
                        Icon(
                            modifier = Modifier
                                .clickable {
                                    isChecked = !isChecked
                                    if (isChecked) {
                                        transferList.add(mwi!!.foods)
                                    } else {
                                        transferList.remove(mwi!!.foods)
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
                    modifier = Modifier.padding(horizontal = 3.dp),
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
            Icon(
                modifier = Modifier
                    .size(36.dp)
                    .clickable { showEditMealDialog = true },
                imageVector = Icons.Filled.Edit,
                contentDescription = "Edit Icon",
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}