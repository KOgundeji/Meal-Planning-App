package com.kunle.aisle9b.util

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.KeyboardDoubleArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.kunle.aisle9b.models.Food
import com.kunle.aisle9b.screens.ShoppingViewModel
import com.kunle.aisle9b.templates.CategoryDropDownMenu
import com.kunle.aisle9b.templates.ListItem9

@Composable
fun ReconciliationDialog(
    items: Map<String, List<Food>>,
    shoppingViewModel: ShoppingViewModel,
    resetListLibraryToDefault: () -> Unit,
    dialogOpen: (Boolean) -> Unit
) {
    val keyList = items.keys.toList()
    val totalDialogsNeeded = items.size

    var currentDialogIndex by remember { mutableStateOf(0) }

    if (items[keyList[currentDialogIndex]] != null) {
        val list = items[keyList[currentDialogIndex]]
        val numOfFoodsToReconcile = list!!.size
        Dialog(onDismissRequest = { dialogOpen(false) }) {
            Surface(color = Color.DarkGray) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "(${currentDialogIndex + 1}/$totalDialogsNeeded)")
                    Text(
                        text = "Some ingredients appear in multiple lists",
                        textAlign = TextAlign.Center
                    )
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .weight(weight = 1f, fill = false)
                    ) {
                        ListItem9(
                            food = list[0],
                            shoppingViewModel = shoppingViewModel,
                            editPencilShown = false,
                            checkBoxShown = false
                        )
                        repeat(times = numOfFoodsToReconcile - 1) { currentNum ->
                            Spacer(modifier = Modifier.height(10.dp))
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.AddBox,
                                    contentDescription = "Add symbol"
                                )
                                ListItem9(
                                    food = list[currentNum + 1],
                                    shoppingViewModel = shoppingViewModel,
                                    editPencilShown = false,
                                    checkBoxShown = false
                                )
                            }
                        }
                    }
                    Icon(
                        imageVector = Icons.Filled.KeyboardDoubleArrowDown,
                        contentDescription = null
                    )
                    ReplacementFoodSection(
                        name = keyList[currentDialogIndex],
                        setFood = { shoppingViewModel.insertFood(it) },
                        takeOriginalFoodOutOfGroceryList = {
                            list.forEach {
                                val outOfGrocery = Food(
                                    foodId = it.foodId,
                                    name = it.name,
                                    quantity = it.quantity,
                                    category = it.category,
                                    isInGroceryList = false
                                )
                                shoppingViewModel.updateFood(outOfGrocery)
                            }
                        },
                        onNextClick = {
                            if ((currentDialogIndex + 1) < totalDialogsNeeded) {
                                currentDialogIndex += 1
                            } else {
                                resetListLibraryToDefault()
                                dialogOpen(false)
                            }
                        })
                }
            }
        }
    } else {
        currentDialogIndex += 1
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReplacementFoodSection(
    name: String,
    setFood: (Food) -> Unit,
    takeOriginalFoodOutOfGroceryList: () -> Unit,
    onNextClick: () -> Unit
) {
    var ingredientQuantity by remember { mutableStateOf("") }
    var ingredientCategory by remember { mutableStateOf("Uncategorized") }

    Column(
        modifier = Modifier.padding(bottom = 20.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = name,
            enabled = false,
            onValueChange = { },
            label = { Text(text = "Grocery Item") },
            colors = TextFieldDefaults.textFieldColors(containerColor = Color.Gray)
        )
        TextField(
            value = ingredientQuantity,
            onValueChange = { ingredientQuantity = it },
            label = { Text(text = "How much/How many?") },
            placeholder = { Text(text = "Type quantity") },
            colors = TextFieldDefaults.textFieldColors(containerColor = Color.Gray),
        )
        CategoryDropDownMenu(
            category = ingredientCategory,
            newCategory = { ingredientCategory = it })
        Spacer(modifier = Modifier.height(20.dp))
        Box() {
            Button(
                onClick = {
                    val newFood = Food(
                        name = name,
                        quantity = ingredientQuantity,
                        category = ingredientCategory,
                        isInGroceryList = true
                    )
                    setFood(newFood)
                    ingredientQuantity = ""
                    ingredientCategory = "Uncategorized"
                    
                    takeOriginalFoodOutOfGroceryList()
                    onNextClick()
                },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = "Replace",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
