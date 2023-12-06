package com.kunle.aisle9b.templates.dialogs.mealDialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import com.kunle.aisle9b.models.Meal
import com.kunle.aisle9b.templates.CustomTextField9

@Composable
fun EditSummaryDialog9(
    meal: Meal,
    updateMeal: (String, String) -> Unit,
    setShowDialog: () -> Unit
) {
    var name by remember { mutableStateOf(meal.name) }
    var notes by remember { mutableStateOf(meal.notes) }

    Dialog(onDismissRequest = { setShowDialog() }) {
        Surface(shape = RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.secondaryContainer) {
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
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Icon(
                        imageVector = Icons.Filled.Close,
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
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
                CustomTextField9(
                    modifier = Modifier
                        .height(235.dp)
                        .fillMaxWidth(),
                    text = notes,
                    onValueChange = { notes = it },
                    label = "Notes",
                    singleLine = false,
                    textStyle = TextStyle(fontSize = 16.sp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        modifier = Modifier.width(200.dp),
                        onClick = {
                            updateMeal(name, notes)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    ) {
                        Text(text = "Save Changes", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }

            }
        }
    }
}