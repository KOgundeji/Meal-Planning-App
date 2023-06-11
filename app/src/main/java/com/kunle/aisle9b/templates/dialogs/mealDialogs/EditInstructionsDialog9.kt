package com.kunle.aisle9b.templates.dialogs.mealDialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.kunle.aisle9b.models.Instruction
import com.kunle.aisle9b.screens.meals.MealVM
import com.kunle.aisle9b.templates.CustomTextField9

@Composable
fun EditInstructionsDialog9(
    instruction: Instruction,
    updatedInstruction: (Instruction, Int) -> Unit,
    setShowDialog: () -> Unit
) {
    var newPosition by remember { mutableStateOf(instruction.position.toString()) }
    var instructionStep by remember { mutableStateOf(instruction.step) }

    Dialog(onDismissRequest = { setShowDialog() }) {
        Surface(shape = RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.background) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)) {
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
                        .verticalScroll(rememberScrollState())
                        .fillMaxWidth(),
                    text = newPosition,
                    onValueChange = {
                        newPosition = if (it != "") {
                            it
                        } else {
                            ""
                        }
                    },
                    label = "Step #...",
                    textStyle = TextStyle(fontSize = 16.sp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                CustomTextField9(
                    modifier = Modifier
                        .height(235.dp)
                        .verticalScroll(rememberScrollState())
                        .fillMaxWidth(),
                    text = instructionStep,
                    onValueChange = { instructionStep = it },
                    label = "Recipe Step",
                    textStyle = TextStyle(fontSize = 16.sp),
                    singleLine = false
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Button(
                        onClick = {
                            updatedInstruction(
                                Instruction(
                                    instructionId = instruction.instructionId,
                                    step = instructionStep,
                                    mealId = instruction.mealId,
                                    position = instruction.position
                                ),
                                newPosition.toInt()
                            )
                        },
                        modifier = Modifier.width(200.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    ) {
                        Text(text = "Save Changes", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}