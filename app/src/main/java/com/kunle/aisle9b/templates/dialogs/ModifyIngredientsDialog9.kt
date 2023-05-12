package com.kunle.aisle9b.templates.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kunle.aisle9b.models.Food
import com.kunle.aisle9b.models.ListFoodMap
import com.kunle.aisle9b.models.MealFoodMap
import com.kunle.aisle9b.screens.SharedVM
import com.kunle.aisle9b.screens.customLists.CustomListVM
import com.kunle.aisle9b.screens.meals.MealVM
import com.kunle.aisle9b.templates.EditFoodDialog9
import com.kunle.aisle9b.templates.ListItem9
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifyIngredientsDialog9(
    id: UUID,
    source: EditSource,
    shoppingVM: SharedVM,
    customListVM: CustomListVM = viewModel(),
    mealVM: MealVM = viewModel(),
    setShowDialog: () -> Unit
) {
    val food = if (source == EditSource.CustomList) {
        customListVM.groceriesOfCustomLists.collectAsState().value.first { it.list.listId == id }.groceries
    } else {
        mealVM.mealsWithIngredients.collectAsState().value.first { it.meal.mealId == id }.foods
    }

    val sourceName = if (source == EditSource.CustomList) {
        customListVM.groceriesOfCustomLists.collectAsState().value.first { it.list.listId == id }.list.name
    } else {
        mealVM.mealsWithIngredients.collectAsState().value.first { it.meal.mealId == id }.meal.name
    }

    val interactionSource = remember { MutableInteractionSource() }
    val foodList by remember { mutableStateOf(food) }
    var name by remember { mutableStateOf(sourceName) }
    var showFoodDialog by remember { mutableStateOf(false) }

    if (showFoodDialog) {
        EditFoodDialog9(
            food = Food(name = "", quantity = "", isInGroceryList = false),
            closeDialog = { showFoodDialog = false },
            setFood = {
                shoppingVM.insertFood(it)
                if (source == EditSource.CustomList) {
                    customListVM.insertPair(ListFoodMap(listId = id, foodId = it.foodId))
                } else {
                    mealVM.insertPair(MealFoodMap(mealId = id, foodId = it.foodId))
                }
            })
    }

    Dialog(onDismissRequest = { setShowDialog() }) {
        Surface(shape = RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.background) {
            Column(
                modifier = Modifier.padding(20.dp),
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
                BasicTextField(
                    modifier = Modifier
                        .height(45.dp)
                        .fillMaxWidth(),
                    value = name,
                    textStyle = TextStyle(fontSize = 16.sp),
                    singleLine = true,
                    onValueChange = { name = it },
                    interactionSource = interactionSource
                ) {
                    TextFieldDefaults.TextFieldDecorationBox(
                        value = name,
                        innerTextField = it,
                        enabled = true,
                        singleLine = true,
                        shape = RoundedCornerShape(3.dp),
                        label = { Text(text = "Meal Name") },
                        visualTransformation = VisualTransformation.None,
                        interactionSource = interactionSource,
                        contentPadding = PaddingValues(horizontal = 15.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            textColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { showFoodDialog = true },
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
                            shoppingVM = shoppingVM,
                            checkBoxShown = false
                        )
                    }
                }
            }
        }
    }
}

enum class EditSource {
    Meal,
    CustomList;
}