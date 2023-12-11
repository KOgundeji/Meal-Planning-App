package com.kunle.aisle9b.templates.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.kunle.aisle9b.models.Grocery
import com.kunle.aisle9b.ui.theme.LightYellow
import com.kunle.aisle9b.util.CategoryDropDownMenu

@Composable
fun EditGroceryDialog9(
    oldGrocery: Grocery,
    closeDialog: () -> Unit,
    setGrocery: (Grocery) -> Unit
) {
    var groceryName by remember { mutableStateOf(oldGrocery.name) }
    var groceryQuantity by remember { mutableStateOf(oldGrocery.quantity) }
    var groceryCategory by remember { mutableStateOf(oldGrocery.category) }

    Dialog(onDismissRequest = { closeDialog() }) {
        Surface(
            modifier = Modifier.padding(bottom = 20.dp),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.secondaryContainer
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Modify",
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close button",
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier
                            .width(30.dp)
                            .height(30.dp)
                            .clickable { closeDialog() }
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                TextField(
                    value = groceryName,
                    onValueChange = { groceryName = it },
                    label = { Text(text = "Ingredient") },
                    placeholder = { Text(text = "Type food name") },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.inverseSurface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.inverseSurface,
                        focusedLabelColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    shape = RectangleShape
                )
                TextField(
                    value = groceryQuantity,
                    onValueChange = { groceryQuantity = it },
                    label = { Text(text = "How much/How many?") },
                    placeholder = { Text(text = "Type quantity") },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.inverseSurface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.inverseSurface,
                        focusedLabelColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                    ),
                    shape = RectangleShape
                )
                CategoryDropDownMenu(
                    category = groceryCategory,
                    newCategory = { groceryCategory = it })
                Spacer(modifier = Modifier.height(20.dp))
                Box {
                    Button(
                        onClick = {
                            val newGrocery = Grocery(
                                groceryId = oldGrocery.groceryId,
                                name = groceryName,
                                quantity = groceryQuantity,
                                category = groceryCategory
                            )
                            setGrocery(newGrocery)
                            closeDialog()
                        },
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(text = "Save", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }
        }
    }
}

