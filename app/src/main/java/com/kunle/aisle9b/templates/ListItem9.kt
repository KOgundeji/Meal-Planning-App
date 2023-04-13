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
import androidx.compose.ui.text.font.FontWeight
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
    var isChecked by remember { mutableStateOf(false) }
    var showEditFoodDialog by remember { mutableStateOf(false) }

    if (showEditFoodDialog) {
        EditFoodDialog9(
            food = food,
            shoppingViewModel = shoppingViewModel,
            setShowSelfDialog = { showEditFoodDialog = it },
            setFood = {
                if (!onEditClickNew) {
                    shoppingViewModel.updateFood(it)
                } else {
                    shoppingViewModel.tempIngredientList.remove(food)
                    shoppingViewModel.tempIngredientList.add(it)
                }
            })
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp),
        shape = RoundedCornerShape(corner = CornerSize(6.dp)),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.padding(8.dp)
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = {
                    isChecked = true
                    food.isInGroceryList = false
                    shoppingViewModel.updateFood(food)
                },
                enabled = checkBoxEnabled,
                colors = CheckboxDefaults.colors(
                    checkedColor = OrangeTintLight,
                    uncheckedColor = Color.Black,
                    checkmarkColor = Color.Black
                ),
                modifier = Modifier.size(36.dp)
            )
            Text(text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = Color.Black,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append(food.name)
                }
                withStyle(
                    style = SpanStyle(
                        color = Color.Gray,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append(" (${food.quantity})")
                }
            }, modifier = Modifier.weight(1f))
            Icon(
                modifier = Modifier
                    .size(36.dp)
                    .clickable {
                        showEditFoodDialog = true
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