package com.kunle.aisle9b.util

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.kunle.aisle9b.models.Food
import com.kunle.aisle9b.screens.CustomListButtonBar
import com.kunle.aisle9b.screens.MealButtonBar
import com.kunle.aisle9b.screens.ShoppingVM
import com.kunle.aisle9b.templates.ListItem9

@Composable
fun ReconciliationDialog(
    items: Map<String, List<Food>>,
    shoppingVM: ShoppingVM,
    resetButtonBarToDefault: () -> Unit,
    closeDialog: () -> Unit
) {
    val context = LocalContext.current
    val keyList = items.keys.toList()
    val totalDialogsNeeded = items.size

    var currentDialogIndex by remember { mutableStateOf(0) }

    if (keyList.isNotEmpty() && items[keyList[currentDialogIndex]] != null) {
        val list = items[keyList[currentDialogIndex]]
        val numOfFoodsToReconcile = list!!.size
        Dialog(onDismissRequest = { closeDialog() }) {
            Surface(color = Color.DarkGray) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "(${currentDialogIndex + 1}/$totalDialogsNeeded)",
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Some ingredients appear in multiple lists",
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp
                    )
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .weight(weight = 1f, fill = false)
                    ) {
                        ListItem9(
                            modifier = Modifier.height(40.dp),
                            food = list[0],
                            shoppingVM = shoppingVM,
                            editPencilShown = false,
                            checkBoxShown = false
                        )
                        repeat(times = numOfFoodsToReconcile - 1) { currentNum ->
                            Spacer(modifier = Modifier.height(7.dp))
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.AddBox,
                                    contentDescription = "Add symbol"
                                )
                                ListItem9(
                                    modifier = Modifier.height(40.dp),
                                    food = list[currentNum + 1],
                                    shoppingVM = shoppingVM,
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
                        setFood = { shoppingVM.insertFood(it) },
                        takeOriginalFoodOutOfGroceryList = {
                            list.forEach {
                                it.isInGroceryList = false
                                shoppingVM.updateFood(it)
                            }
                        },
                        onNextClick = {
                            if ((currentDialogIndex + 1) < totalDialogsNeeded) {
                                currentDialogIndex += 1
                            } else {
                                resetButtonBarToDefault()
                                closeDialog()
                            }
                            Toast.makeText(context, "$it added to Grocery List", Toast.LENGTH_SHORT).show()
                        })
                }
            }
        }
    } else {
        currentDialogIndex += 1
    }
//    resetButtonBarToDefault()
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReplacementFoodSection(
    name: String,
    setFood: (Food) -> Unit,
    takeOriginalFoodOutOfGroceryList: () -> Unit,
    onNextClick: (String) -> Unit
) {
    var ingredientQuantity by remember { mutableStateOf("") }
    var ingredientCategory by remember { mutableStateOf("") }

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
                    ingredientCategory = ""

                    takeOriginalFoodOutOfGroceryList()
                    onNextClick(name)
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
