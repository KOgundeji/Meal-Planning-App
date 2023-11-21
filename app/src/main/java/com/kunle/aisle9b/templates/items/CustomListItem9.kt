package com.kunle.aisle9b.templates.items

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kunle.aisle9b.models.Food
import com.kunle.aisle9b.models.GroceryList
import com.kunle.aisle9b.screens.customLists.CustomListVM
import com.kunle.aisle9b.templates.dialogs.ModifyGroceriesDialog9
import com.kunle.aisle9b.util.ActionDropdown
import com.kunle.aisle9b.util.DropActions

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CustomListItem9(
    groceryList: GroceryList,
    customListVM: CustomListVM,
    deleteList: () -> Unit,
    transferFood: (List<Food>) -> Unit
) {
    var longPress by remember { mutableStateOf(false) }
    val haptics = LocalHapticFeedback.current

    var showEditMealDialog by remember { mutableStateOf(false) }
    val lwg = customListVM.groceriesOfCustomLists.collectAsState().value.find { LWG ->
        LWG.list.listId == groceryList.listId
    }
    val listedGroceries: String = lwg?.groceries
        ?.joinToString(separator = ", ") { it.name }
        ?: ""  //its the default separator, but wanted to include anyway

    if (showEditMealDialog) {
        ModifyGroceriesDialog9(
            groceryList = groceryList,
            customListVM = customListVM,
            setShowDialog = { showEditMealDialog = false }
        )
    }

    if (longPress) {
        ActionDropdown(expanded = { longPress = it }) { dropActions ->
            longPress = when (dropActions) {
                DropActions.Edit -> {
                    showEditMealDialog = true
                    false
                }
                DropActions.Transfer -> {
                    if (lwg?.groceries?.isNotEmpty() == true) {
                        transferFood(lwg.groceries)
                    }
                    false
                }
                DropActions.Delete -> {
                    deleteList()
                    false
                }
            }
        }
    }

    Card(
        modifier = Modifier
            .padding(horizontal = 6.dp)
            .fillMaxWidth()
            .combinedClickable(
                enabled = true, onLongClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    longPress = true
                },
                onLongClickLabel = "Action Dropdown"
            ) {},
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        shape = RoundedCornerShape(corner = CornerSize(6.dp))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 10.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(.9f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(32.dp),
                    imageVector = Icons.Filled.ListAlt,
                    contentDescription = "List Icon",
                    tint = MaterialTheme.colorScheme.primary,
                )
                Column(
                    modifier = Modifier.padding(horizontal = 3.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = groceryList.listName,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = listedGroceries,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}