package com.kunle.aisle9b.templates.dialogs.mealDialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.kunle.aisle9b.models.Food
import com.kunle.aisle9b.screens.meals.MealVM
import com.kunle.aisle9b.templates.dialogs.EditFoodDialog9
import com.kunle.aisle9b.templates.items.CustomUpdateTextField9
import com.kunle.aisle9b.templates.items.ListItem9

@Composable
fun IngredientsListDialog9(
    modifier: Modifier = Modifier,
    mealVM: MealVM,
    originalServingSize: String,
    foodList: List<Food>,
    updateFoodList: (Food) -> Unit,
    onSaveServingSizeClick: (String) -> Unit,
    setShowDialog: () -> Unit
) {
    var addFoodDialog by remember { mutableStateOf(false) }
    var newServingSize by remember { mutableStateOf(originalServingSize) }

    if (addFoodDialog) {
        EditFoodDialog9(
            oldFood = Food.createBlank(),
            closeDialog = { addFoodDialog = false },
            setFood = { newFood ->
                updateFoodList(newFood)
            })
    }

    Dialog(onDismissRequest = { setShowDialog() }) {
        Surface(shape = RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.background) {
            Column(
                modifier = modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Modify",
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close button",
                        modifier = Modifier
                            .width(30.dp)
                            .height(30.dp)
                            .clickable { setShowDialog() }
                    )
                }
                CustomUpdateTextField9(
                    text = newServingSize,
                    onValueChange = { newServingSize = it },
                    onSaveClick = { onSaveServingSizeClick(newServingSize) },
                    label = "# of servings recipe makes"
                )
                Text(text = "List of Ingredients", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { addFoodDialog = true },
                        modifier = Modifier.width(75.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    ) {
                        Icon(
                            modifier = Modifier.size(30.dp),
                            imageVector = Icons.Filled.AddCircle,
                            contentDescription = "Add button"
                        )
                    }
                    Button(
                        onClick = { }, //fix this
                        modifier = Modifier.width(75.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete button",
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }

                LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    items(items = foodList) {
                        ListItem9(
                            modifier = Modifier.padding(start = 4.dp),
                            food = it,
                            viewModel = mealVM,
                            checkBoxShown = false,
                            onEditFood = { newFood ->
                                updateFoodList(newFood)
                            }
                        )
                    }
                }
            }
        }
    }
}