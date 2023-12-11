package com.kunle.aisle9b.util

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowCircleLeft
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.FileOpen
import androidx.compose.material.icons.outlined.LocalGroceryStore
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomBottomSheet9(
    sheetState: SheetState,
    textLabels: Array<String>,
    apiSourced: Boolean = false,
    closeBottomSheet: () -> Unit,
    viewList: () -> Unit,
    transferFood: () -> Unit,
    edit: () -> Unit,
    delete: () -> Unit,
    headerContent: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        onDismissRequest = {
            scope.launch {
                sheetState.hide()
            }.invokeOnCompletion {
                if (!sheetState.isVisible) {
                    closeBottomSheet()
                }
            }
        }) {
        Column(
            modifier = Modifier.padding(start = 15.dp, bottom = 25.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            headerContent()
            Divider()
            BottomSheetItems9(
                icon = Icons.Outlined.LocalGroceryStore,
                text = textLabels[0],
                onClick = { viewList() }
            )
            if (!apiSourced) {
                BottomSheetItems9(
                    icon = Icons.Outlined.ArrowCircleLeft,
                    text = textLabels[1],
                    onClick = { transferFood() }
                )
                BottomSheetItems9(
                    icon = Icons.Outlined.EditNote,
                    text = textLabels[2],
                    onClick = { edit() }
                )
            }
            BottomSheetItems9(
                icon = Icons.Outlined.Delete,
                text = textLabels[3],
                onClick = { delete() }
            )
        }
        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}

@Composable
fun BottomSheetItems9(
    icon: ImageVector,
    text: String,
    contentDescription: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        horizontalArrangement = Arrangement.spacedBy(25.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(30.dp),
            imageVector = icon, contentDescription = contentDescription
        )
        Text(text = text, fontSize = 18.sp)
    }
}