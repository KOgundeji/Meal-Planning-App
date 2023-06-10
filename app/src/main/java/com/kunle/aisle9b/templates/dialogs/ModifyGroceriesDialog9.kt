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
import com.kunle.aisle9b.models.ListFoodMap
import com.kunle.aisle9b.screens.SharedVM
import com.kunle.aisle9b.screens.customLists.CustomListVM
import com.kunle.aisle9b.templates.CustomTextField9
import com.kunle.aisle9b.templates.items.ListItem9
import java.util.*

@Composable
fun ModifyGroceriesDialog9(
    id: UUID,
    categoryMap: Map<String, String>,
    sharedVM: SharedVM,
    customListVM: CustomListVM,
    setShowDialog: () -> Unit
) {
    val food =
        customListVM.groceriesOfCustomLists.collectAsState().value.first { it.list.listId == id }.groceries

    val sourceName =
        customListVM.groceriesOfCustomLists.collectAsState().value.first { it.list.listId == id }.list.name

    val foodList by remember { mutableStateOf(food) }
    var name by remember { mutableStateOf(sourceName) }
    var showFoodDialog by remember { mutableStateOf(false) }

    if (showFoodDialog) {
        EditFoodDialog9(
            oldFood = Food.createBlank(),
            category = "Uncategorized",
            closeDialog = { showFoodDialog = false },
            setCategory = { sharedVM.upsertCategory(it) },
            setFood = { _, newFood ->
                sharedVM.upsertFood(newFood)
                customListVM.insertPair(ListFoodMap(listId = id, foodId = newFood.foodId))
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
                CustomTextField9(
                    modifier = Modifier
                        .height(45.dp)
                        .fillMaxWidth(),
                    text = name,
                    onValueChange = { name = it },
                    label = "Meal Name",
                    singleLine = true,
                    textStyle = TextStyle(fontSize = 16.sp)
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
                            category = categoryMap[it.name] ?: "Uncategorized",
                            sharedVM = sharedVM,
                            checkBoxShown = false,
                            setCategory = { category -> sharedVM.upsertCategory(category) },
                            onEditFood = { _, newFood ->
                                sharedVM.upsertFood(newFood)
                            }
                        )
                    }
                }
            }
        }
    }
}
