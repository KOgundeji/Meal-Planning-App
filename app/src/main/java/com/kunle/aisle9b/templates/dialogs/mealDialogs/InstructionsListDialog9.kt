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
import com.kunle.aisle9b.models.Instruction
import com.kunle.aisle9b.templates.items.mealItems.InstructionItem

@Composable
fun InstructionsListDialog9(
    mealInstructionList: List<Instruction>,
    updatedInstruction: (Instruction, Int) -> Unit,
    mealId: Long,
    setShowDialog: () -> Unit
) {
    var addInstructionDialog by remember { mutableStateOf(false) }

    if (addInstructionDialog) {
        EditInstructionsDialog9(
            instruction = Instruction.createBlank(mealId),
            setShowDialog = { addInstructionDialog = false },
            updatedInstruction = { instruction, newPos ->
                updatedInstruction(instruction, newPos)
            }
        )
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { addInstructionDialog = true },
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
                LazyColumn {
                    items(items = mealInstructionList) {
                        InstructionItem(
                            instruction = it,
                            updatedInstruction = { updateIns, newPos ->
                                updatedInstruction(updateIns, newPos)
                            },
                        )
                    }
                }
            }
        }
    }

}