package com.kunle.aisle9b.templates.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleLeft
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.KeyboardDoubleArrowDown
import androidx.compose.material.icons.filled.KeyboardDoubleArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.kunle.aisle9b.util.CustomAlertDialog9
import com.kunle.aisle9b.util.DropActions
import com.kunle.aisle9b.util.createBubbleShape

@Composable
fun OptionPopup(optionType: Options, closeDialog: () -> Unit, actionClick: (DropActions) -> Unit) {
    var openAlertDialog by remember { mutableStateOf(false) }
    val bubbleShape = remember {
        createBubbleShape(arrowWidth = 200f, arrowHeight = 20f, arrowOffset = 225f)
    }
    val bubbleWidth = remember {
        when (optionType) {
            Options.ETD -> .75f
            Options.MoveUpDown -> .5f
            Options.Delete -> .25f
        }
    }

    if (openAlertDialog) {
        CustomAlertDialog9(
            onCancelRequest = {
                openAlertDialog = false
            },
            onConfirmation = {
                actionClick(DropActions.Delete)
                openAlertDialog = false
            },
            dialogTitle = "Delete",
            dialogText = "Are you sure you want to delete this meal?"
        )
    }

    Popup(
        alignment = Alignment.TopCenter,
        offset = IntOffset(0, -200),
        onDismissRequest = { closeDialog() },
        properties = PopupProperties(
            focusable = true
        )
    ) {
        Surface(
            modifier = Modifier
                .offset()
                .fillMaxWidth(bubbleWidth)
                .height(70.dp),
            contentColor = MaterialTheme.colorScheme.secondaryContainer,
            color = MaterialTheme.colorScheme.primary.copy(alpha = .95f),
            shadowElevation = 5.dp,
            shape = bubbleShape
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                when (optionType) {
                    Options.ETD -> {
                        StackedSelectOption(image = Icons.Filled.EditNote, text = "Edit") {
                            actionClick(DropActions.Edit)
                        }
                        StackedSelectOption(
                            image = Icons.Filled.ArrowCircleLeft,
                            text = "Transfer"
                        ) {
                            actionClick(DropActions.Transfer)
                        }
                        StackedSelectOption(image = Icons.Filled.Delete, text = "Delete") {
                            openAlertDialog = true
                        }
                    }

                    Options.MoveUpDown -> {
                        StackedSelectOption(
                            image = Icons.Filled.KeyboardDoubleArrowUp,
                            text = "Move Up"
                        ) {
                            actionClick(DropActions.MoveUp)
                        }
                        StackedSelectOption(
                            image = Icons.Filled.KeyboardDoubleArrowDown,
                            text = "Move Down"
                        ) {
                            actionClick(DropActions.MoveDown)
                        }
                    }

                    Options.Delete -> {
                        StackedSelectOption(image = Icons.Filled.Delete, text = "Delete") {
                            openAlertDialog = true
                        }
                    }
                }


            }
        }
    }
}


@Composable
private fun StackedSelectOption(
    image: ImageVector,
    text: String,
    contentDescription: String? = null,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 3.dp)
            .clickable { onClick() },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = image,
            contentDescription = contentDescription,
        )
        Text(
            text = text,
            fontSize = 14.sp,
            maxLines = 1
        )
    }
}

enum class Options {
    ETD,
    MoveUpDown,
    Delete
}