package com.kunle.aisle9b.templates.items.mealItems

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kunle.aisle9b.models.Instruction
import com.kunle.aisle9b.screens.meals.MealVM
import com.kunle.aisle9b.templates.dialogs.mealDialogs.EditInstructionsDialog9

@Composable
fun InstructionItem(
    instruction: Instruction,
    updatedInstruction: (Instruction, Int) -> Unit,
) {
    var edit by remember { mutableStateOf(false) }

    if (edit) {
        EditInstructionsDialog9(
            instruction = instruction,
            updatedInstruction = { updatedIns, newPos ->
                updatedInstruction(updatedIns, newPos)
                edit = false
            },
            setShowDialog = { edit = false }
        )
    }

    Card(
        modifier = Modifier
            .padding(horizontal = 6.dp)
            .fillMaxWidth()
            .clickable { edit = true },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        shape = RoundedCornerShape(corner = CornerSize(6.dp))
    ) {
        Row(
            modifier = Modifier.padding(3.dp),
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            verticalAlignment = Alignment.Top
        ) {
            Text(text = "Step ${instruction.position}:")
            Text(text = instruction.step)
        }

    }
}