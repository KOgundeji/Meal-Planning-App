package com.kunle.aisle9b.templates.items

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kunle.aisle9b.models.Food
import com.kunle.aisle9b.screens.SharedVM
import com.kunle.aisle9b.templates.dialogs.EditFoodDialog9

@Composable
fun ListItem9(
    modifier: Modifier = Modifier,
    food: Food,
    sharedVM: SharedVM,
    onEditFood: (Food, Food) -> Unit,
    checkBoxShown: Boolean = true,
    editPencilShown: Boolean = true
) {
    var isChecked by remember { mutableStateOf(false) }
    var showEditFoodDialog by remember { mutableStateOf(false) }

    if (showEditFoodDialog) {
        EditFoodDialog9(
            oldFood = food,
            closeDialog = { showEditFoodDialog = false },
            setFood = { oldFood, updatedFood ->
                onEditFood(oldFood, updatedFood)
            })
    }
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp),
        shape = RoundedCornerShape(corner = CornerSize(6.dp)),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = modifier.padding(3.dp)
        ) {
            if (checkBoxShown) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = {
                        isChecked = true
                        food.isInGroceryList = false
                        sharedVM.upsertFood(food)
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.secondaryContainer,
                        uncheckedColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        checkmarkColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.size(36.dp)
                )
            }
            Text(text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append(food.name)
                }
                withStyle(
                    style = SpanStyle(
                        color = Color.Gray,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append(" (${food.quantity})")
                }
            }, modifier = Modifier.weight(1f))
            if (editPencilShown) {
                Icon(
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            showEditFoodDialog = true
                        },
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Edit Icon",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.width(5.dp))
            }
        }
    }
}
