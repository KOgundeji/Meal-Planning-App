package com.kunle.aisle9b.templates.dialogs

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
import com.kunle.aisle9b.models.GroceryList
import com.kunle.aisle9b.models.GroceryListNameUpdate
import com.kunle.aisle9b.models.ListFoodMap
import com.kunle.aisle9b.screens.customLists.CustomListVM
import com.kunle.aisle9b.templates.CustomTextField9
import com.kunle.aisle9b.templates.items.CustomUpdateTextField9
import com.kunle.aisle9b.templates.items.ListItem9
import kotlinx.coroutines.launch

@Composable
fun ModifyGroceriesDialog9(
    groceryList: GroceryList,
    customListVM: CustomListVM,
    setShowDialog: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val collectedFoodList =
        customListVM.groceriesOfCustomLists.collectAsState().value.first { it.list.listId == groceryList.listId }.groceries

    val foodList by remember { mutableStateOf(collectedFoodList) }
    var name by remember { mutableStateOf(groceryList.listName) } //this doesn't actually go anywhere
    var showFoodDialog by remember { mutableStateOf(false) }

    if (showFoodDialog) {
        EditFoodDialog9(
            oldFood = Food.createBlank(),
            closeDialog = { showFoodDialog = false },
            setFood = { newFood ->
                scope.launch {
                    customListVM.upsertFood(newFood)
                    customListVM.insertPair(
                        ListFoodMap(
                            listId = groceryList.listId,
                            foodId = newFood.foodId
                        )
                    )
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
                CustomUpdateTextField9(
                    modifier = Modifier
                        .height(45.dp)
                        .fillMaxWidth(),
                    text = name,
                    onValueChange = { name = it },
                    onSaveClick = {
                        customListVM.updateName(
                            GroceryListNameUpdate(
                                listId = groceryList.listId,
                                listName = it
                            )
                        )
                    },
                    label = "List Name",
                    singleLine = true
                )
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
                            viewModel = customListVM,
                            checkBoxShown = false,
                            onEditFood = { newFood ->
                                scope.launch { customListVM.upsertFood(newFood) }
                            }
                        )
                    }
                }
            }
        }
    }
}
