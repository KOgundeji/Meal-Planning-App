package com.kunle.aisle9b.templates

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kunle.aisle9b.models.Meal
import com.kunle.aisle9b.screens.ShoppingViewModel
import com.kunle.aisle9b.ui.theme.DM_DarkishGray
import com.kunle.aisle9b.ui.theme.DM_LightGray

@Composable
fun MealItem9(
    meal: Meal,
    deleteEnabled: Boolean,
    shoppingViewModel: ShoppingViewModel,
) {
    val darkMode = shoppingViewModel.darkModeSetting.value
    var isChecked by remember { mutableStateOf(false) }
    var showEditMealDialog by remember { mutableStateOf(false) }
    val mwi = shoppingViewModel.mealWithIngredientsList.collectAsState().value.first() { MWI ->
        MWI.meal.mealId == meal.mealId
    }
    val listedIngredients: String = mwi.foods
        .joinToString(separator = ", ") { it.name } //its the default separator, but wanted to include anyway

    if (showEditMealDialog) {
        EditMealDialog9(
            meal = meal,
            shoppingViewModel = shoppingViewModel,
            setShowDialog = { showEditMealDialog = it })
    }

    Card(
        modifier = Modifier
            .padding(horizontal = 6.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        shape = RoundedCornerShape(corner = CornerSize(6.dp))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 16.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(.9f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (deleteEnabled) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = {
                            isChecked = !isChecked
                            if (isChecked) {
                                shoppingViewModel.mealDeleteList.add(meal)
                            } else {
                                shoppingViewModel.mealDeleteList.remove(meal)
                            }
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.background,
                            uncheckedColor = MaterialTheme.colorScheme.outline,
                            checkmarkColor = MaterialTheme.colorScheme.outline
                        ),
                        modifier = Modifier.size(36.dp)
                    )
                }
                Column(
                    modifier = Modifier.padding(horizontal = 3.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = meal.name,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = listedIngredients,
                        color = if (darkMode) DM_LightGray else DM_DarkishGray,
                        fontSize = 18.sp,
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
                tint = MaterialTheme.colorScheme.outline
            )
        }
    }
}


//@Composable
//@Preview
//fun MealPreview() {
//    MealItem9(Meal(name = "Delicious Example"))
//}