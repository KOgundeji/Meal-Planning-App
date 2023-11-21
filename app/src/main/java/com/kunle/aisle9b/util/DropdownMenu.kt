package com.kunle.aisle9b.util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ModeEdit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.ArrowCircleLeft
import androidx.compose.material.icons.outlined.DeleteForever
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import com.kunle.aisle9b.navigation.GroceryScreens

@Composable
fun AdditionalScreenOptions(navController: NavController) {
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
                    navController.navigate(GroceryScreens.SettingsScreen.name)
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
            onDismissRequest = {
                openAlertDialog = false
                expanded(false)
            },
            onConfirmation = {
                onActionConfirm(DropActions.Delete)
                openAlertDialog = false
                expanded(false)
            },
            dialogTitle = "Aisle9",
            dialogText = "Are you sure you want to delete this meal?",
            icon = Icons.Filled.ShoppingCart
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
fun CustomAlertDialog9(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}

enum class DropActions {
    Edit,
    Transfer,
    Delete
}