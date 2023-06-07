package com.kunle.aisle9b.templates.dialogs.mealDialogs

import androidx.compose.animation.core.EaseOutQuint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.kunle.aisle9b.models.Food
import com.kunle.aisle9b.screens.SharedVM
import com.kunle.aisle9b.templates.CustomTextField9
import com.kunle.aisle9b.templates.dialogs.EditFoodDialog9
import com.kunle.aisle9b.templates.items.ListItem9

@Composable
fun IngredientsListDialog9(
    modifier: Modifier = Modifier,
    sharedVM: SharedVM,
    foodList: List<Food>,
    originalServingSize: String,
    updateFoodList: (Food, Food, String) -> Unit,
    onSaveServingSizeClick: (String) -> Unit,
    setShowDialog: () -> Unit
) {
    var addFoodDialog by remember { mutableStateOf(false) }
    var newServingSize by remember { mutableStateOf(originalServingSize) }
    var saveClicked by remember { mutableStateOf(false) }
    val fadeInOut by animateFloatAsState(
        targetValue = if (saveClicked) 0f else 1f,
        animationSpec = tween(
            durationMillis = 1000,
            easing = EaseOutQuint
        ),
        finishedListener = { saveClicked = false }
    )

    if (addFoodDialog) {
        EditFoodDialog9(
            oldFood = Food.createBlank(),
            closeDialog = { addFoodDialog = false },
            setFood = { oldFood, newFood ->
                updateFoodList(oldFood, newFood, "Add")
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
                CustomTextField9(
                    modifier = Modifier
                        .height(45.dp)
                        .fillMaxWidth(),
                    text = newServingSize,
                    onValueChange = { newServingSize = it },
                    label = "# of servings recipe makes",
                    singleLine = true,
                    textStyle = TextStyle(fontSize = 16.sp),
                    trailingIcon = {
                        Button(
                            shape = RectangleShape,
                            contentPadding = PaddingValues(7.dp),
                            modifier = Modifier
                                .padding(5.dp)
                                .alpha(1 - fadeInOut),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                            onClick = {
                                if (newServingSize.isNotEmpty()) {
                                    onSaveServingSizeClick(newServingSize)
                                }
                            }) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                        Button(
                            shape = RectangleShape,
                            contentPadding = PaddingValues(7.dp),
                            modifier = Modifier
                                .padding(5.dp)
                                .alpha(fadeInOut),
                            onClick = {
                                if (newServingSize.isNotEmpty()) {
                                    onSaveServingSizeClick(newServingSize)
                                    saveClicked = true
                                }
                            }) {
                            Text(
                                text = "Save",
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
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
                        onClick = { },
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
                            sharedVM = sharedVM,
                            checkBoxShown = false,
                            onEditFood = { oldFood, newFood ->
                                updateFoodList(oldFood, newFood, "Edit")
                            }
                        )
                    }
                }
            }
        }
    }
}