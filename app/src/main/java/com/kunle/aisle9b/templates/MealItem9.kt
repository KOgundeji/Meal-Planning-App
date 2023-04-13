package com.kunle.aisle9b.templates

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kunle.aisle9b.models.Meal
import com.kunle.aisle9b.screens.ShoppingViewModel
import com.kunle.aisle9b.ui.theme.BaseOrange
import com.kunle.aisle9b.ui.theme.OrangeTintLight

@Composable
fun MealItem9(meal: Meal, deleteEnabled: Boolean, shoppingViewModel: ShoppingViewModel) {
    var isChecked by remember { mutableStateOf(false) }

    var showEditMealDialog by remember { mutableStateOf(false) }

    if (showEditMealDialog) {
        EditMealDialog9(
            meal = meal,
            shoppingViewModel = shoppingViewModel,
            setShowDialog = { showEditMealDialog = it })
    }


    Card(
        modifier = Modifier
            .padding(4.dp)
            .height(60.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(corner = CornerSize(6.dp))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
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
                enabled = deleteEnabled,
                colors = CheckboxDefaults.colors(
                    checkedColor = OrangeTintLight,
                    uncheckedColor = OrangeTintLight,
                    checkmarkColor = Color.Black
                ),
                modifier = Modifier.size(36.dp)
            )
            Text(text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = BaseOrange, fontSize = 22.sp)) {
                    append(meal.name)
                }
            }, modifier = Modifier.weight(1f))
            Icon(
                modifier = Modifier.size(36.dp)
                    .clickable { showEditMealDialog = true },
                imageVector = Icons.Filled.Edit,
                contentDescription = "Edit Icon",
            )
            Spacer(modifier = Modifier.width(5.dp))


        }
    }
}


//@Composable
//@Preview
//fun MealPreview() {
//    MealItem9(Meal(name = "Delicious Example"))
//}