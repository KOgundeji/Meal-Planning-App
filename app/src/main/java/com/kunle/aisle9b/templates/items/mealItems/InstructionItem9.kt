package com.kunle.aisle9b.templates.items.mealItems

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kunle.aisle9b.models.Instruction
import com.kunle.aisle9b.templates.dialogs.OptionPopup
import com.kunle.aisle9b.templates.dialogs.Options
import com.kunle.aisle9b.templates.dialogs.mealDialogs.EditInstructionsDialog9
import com.kunle.aisle9b.util.DropActions

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InstructionItem(
    instruction: Instruction,
    positionChange: (DropActions) -> Unit,
    deleteInstruction: () -> Unit,
    updatedInstruction: (Instruction) -> Unit,
) {
    var edit by remember { mutableStateOf(false) }
    var popup by remember { mutableStateOf(false) }

    if (edit) {
        EditInstructionsDialog9(
            instruction = instruction,
            updatedInstruction = {
                updatedInstruction(it)
                edit = false
            },
            deleteInstruction = {
                deleteInstruction()
                edit = false
            },
            exitDialog = { edit = false }
        )
    }

    if (popup) {
        OptionPopup(optionType = Options.MoveUpDown, closeDialog = { popup = false }) {
            positionChange(it)
            popup = false
        }
    }

    Card(
        modifier = Modifier
            .padding(horizontal = 6.dp)
            .fillMaxWidth()
            .combinedClickable(onLongClick = {
                popup = true
            }) { edit = true },
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = RoundedCornerShape(corner = CornerSize(6.dp))
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = instruction.position.toString(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = instruction.step,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

    }
}