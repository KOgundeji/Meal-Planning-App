package com.kunle.aisle9b.templates

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kunle.aisle9b.models.Food
import com.kunle.aisle9b.screens.ShoppingViewModel
import com.kunle.aisle9b.ui.theme.BaseOrange
import com.kunle.aisle9b.ui.theme.OrangeTintDark
import com.kunle.aisle9b.ui.theme.OrangeTintLight

@Composable
fun ListItem9(
    food: Food,
    shoppingViewModel: ShoppingViewModel,
    checkBoxEnabled: Boolean = true,
    onEditClickNew: Boolean = false
) {
    //move to ViewModel later
    val isChecked = remember {
        mutableStateOf(false)
    }

    val showEditFoodDialog = remember {
        mutableStateOf(false)
    }

    if (showEditFoodDialog.value) {
        EditFoodDialog9(
            food = food,
            shoppingViewModel = shoppingViewModel,
            setShowSelfDialog = { showEditFoodDialog.value = it },
            setFood = { if (!onEditClickNew) {
                shoppingViewModel.updateFood(it)
            } else {
                shoppingViewModel.tempIngredientList.remove(food)
                shoppingViewModel.tempIngredientList.add(it)
            } })
    }

    Card(
        modifier = Modifier
            .padding(4.dp)
            .height(50.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(corner = CornerSize(6.dp))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Checkbox(
                checked = isChecked.value,
                onCheckedChange = {
                    isChecked.value = true
                    food.isInGroceryList = false
                    shoppingViewModel.updateFood(food)
                },
                enabled = checkBoxEnabled,
                colors = CheckboxDefaults.colors(
                    checkedColor = OrangeTintLight,
                    uncheckedColor = OrangeTintLight,
                    checkmarkColor = Color.Black
                ),
                modifier = Modifier.size(36.dp)
            )
            Text(text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = BaseOrange, fontSize = 22.sp)) {
                    append(food.name)
                }
                withStyle(style = SpanStyle(color = OrangeTintDark, fontSize = 16.sp)) {
                    append(" (${food.quantity})")
                }
            }, modifier = Modifier.weight(1f))
            Icon(
                modifier = Modifier
                    .size(36.dp)
                    .clickable {
                        showEditFoodDialog.value = true
                    },
                imageVector = Icons.Filled.Edit,
                contentDescription = "Edit Icon",
            )
            Spacer(modifier = Modifier.width(5.dp))
        }
    }
}



//@Composable
//@Preview
//fun Preview() {
//    ListItem9(Food(name = "Hot Dog", quantity = "3", category =  "NA", isInGroceryList = true))
//}