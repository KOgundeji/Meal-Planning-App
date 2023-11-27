package com.kunle.aisle9b.util

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ModeEdit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.ArrowCircleLeft
import androidx.compose.material.icons.outlined.DeleteForever
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kunle.aisle9b.navigation.GroceryScreens

@Composable
fun AdditionalScreenOptions(navigate: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopEnd)
    ) {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = "Additional screen options",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
        DropdownMenu(
            modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer),
            expanded = expanded,
            onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                text = { Text(text = GroceryScreens.headerTitle(GroceryScreens.SettingsScreen)) },
                onClick = {
                    navigate()
                    expanded = false
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = "Settings"
                    )
                })
        }
    }
}

@Composable
fun ActionDropdown(
    expanded: (Boolean) -> Unit,
    onActionConfirm: (DropActions) -> Unit
) {
    var openAlertDialog by remember { mutableStateOf(false) }

    if (openAlertDialog) {
        CustomAlertDialog9(
            onCancelRequest = {
                openAlertDialog = false
                expanded(false)
            },
            onConfirmation = {
                onActionConfirm(DropActions.Delete)
                openAlertDialog = false
                expanded(false)
            },
            dialogTitle = "Delete",
            dialogText = "Are you sure you want to delete this meal?"
        )
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.CenterEnd)
    ) {
        DropdownMenu(
            modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer),
            expanded = true,
            onDismissRequest = { expanded(false) }) {
            DropdownMenuItem(
                text = { Text(text = DropActions.Edit.name) },
                onClick = {
                    onActionConfirm(DropActions.Edit)
                    expanded(false)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.ModeEdit,
                        contentDescription = "Edit"
                    )
                })
            Divider()
            DropdownMenuItem(
                text = { Text(text = DropActions.Transfer.name) },
                onClick = {
                    onActionConfirm(DropActions.Transfer)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.ArrowCircleLeft,
                        contentDescription = "Transfer"
                    )
                })
            Divider()
            DropdownMenuItem(
                text = { Text(text = DropActions.Delete.name) },
                onClick = {
                    openAlertDialog = true
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.DeleteForever,
                        contentDescription = "Delete"
                    )
                })
        }
    }
}

@Composable
fun OptionBubble(
    expanded: (Boolean) -> Unit,
    onActionConfirm: (DropActions) -> Unit
) {
    Box {
        Canvas(
            modifier = Modifier
                .size(200.dp)
                .padding(40.dp)
        ) {
            val trianglePath = Path().let {
                it.moveTo(this.size.width * .40f, 0f)
                it.lineTo(this.size.width * .50f, -30f)
                it.lineTo(this.size.width * .60f, 0f)
                it.close()
                it
            }
            drawRoundRect(
                Color.LightGray,
                size = Size(this.size.width, this.size.height * 0.95f),
                cornerRadius = CornerRadius(60f)
            )
            drawPath(
                path = trianglePath,
                Color.LightGray,
            )
        }
    }
}

@Composable
fun CustomAlertDialog9(
    onCancelRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String
) {
    AlertDialog(
        modifier = Modifier
            .shadow(5.dp, RoundedCornerShape(24.dp))
            .border(
                width = Dp.Hairline,
                color = MaterialTheme.colorScheme.tertiary,
                shape = RoundedCornerShape(24.dp)
            ),
        icon = { Icon(Icons.Filled.Info, contentDescription = null) },
        title = { Text(text = dialogTitle) },
        text = { Text(text = dialogText) },
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        onDismissRequest = { onCancelRequest() },
        confirmButton = {
            TextButton(onClick = { onConfirmation() }) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = { onCancelRequest() }) {
                Text("Cancel")
            }
        }
    )
}

enum class DropActions {
    Edit,
    Transfer,
    Delete,
    MoveUp,
    MoveDown
}