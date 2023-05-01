package com.kunle.aisle9b.templates

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleLeft
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kunle.aisle9b.models.Food
import com.kunle.aisle9b.models.GroceryList
import com.kunle.aisle9b.screens.SharedVM
import com.kunle.aisle9b.screens.customLists.CustomListButtonBar
import com.kunle.aisle9b.screens.customLists.CustomListVM

@Composable
fun PreMadeListItem9(
    list: GroceryList,
    primaryButtonBarAction: CustomListButtonBar,
    shoppingVM: SharedVM,
    customListVM: CustomListVM,
    transferList: MutableList<List<Food>>
) {
    var isChecked by remember { mutableStateOf(false) }
    var showEditMealDialog by remember { mutableStateOf(false) }
    val lwg = customListVM.groceriesOfCustomLists.collectAsState().value.find { LWG ->
        LWG.list.listId == list.listId
    }
    val listedGroceries: String = lwg?.groceries
        ?.joinToString(separator = ", ") { it.name }
        ?: ""  //its the default separator, but wanted to include anyway

    if (showEditMealDialog) {
        ModifyIngredientsDialog9(
            id = list.listId,
            source = EditSource.CustomList,
            shoppingVM = shoppingVM,
            customListVM = customListVM,
            setShowDialog = { showEditMealDialog = false }
        )
    }

    Card(
        modifier = Modifier
            .padding(horizontal = 6.dp)
            .fillMaxWidth(),
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
                when (primaryButtonBarAction) {
                    CustomListButtonBar.Delete -> {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = {
                                isChecked = !isChecked
                                if (isChecked) {
                                    shoppingVM.groceryListDeleteList.add(list)
                                } else {
                                    shoppingVM.groceryListDeleteList.remove(list)
                                }
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = MaterialTheme.colorScheme.secondaryContainer,
                                uncheckedColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                checkmarkColor = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    CustomListButtonBar.Transfer -> {
                        Icon(
                            modifier = Modifier
                                .clickable {
                                    isChecked = !isChecked
                                    if (isChecked) {
                                        transferList.add(lwg!!.groceries)
                                    } else {
                                        transferList.remove(lwg!!.groceries)
                                    }
                                }
                                .size(32.dp)
                                .border(
                                    border = BorderStroke(
                                        1.dp,
                                        color = MaterialTheme.colorScheme.tertiary
                                    ),
                                    shape = CircleShape
                                ),
                            imageVector = Icons.Filled.ArrowCircleLeft,
                            contentDescription = "Transfer button",
                            tint = if (!isChecked) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                    else -> {
                        Icon(
                            modifier = Modifier.size(32.dp),
                            imageVector = Icons.Filled.ListAlt,
                            contentDescription = "List Icon",
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                }

                Column(
                    modifier = Modifier.padding(horizontal = 3.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = list.name,
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
            Icon(
                modifier = Modifier
                    .size(36.dp)
                    .clickable { showEditMealDialog = true },
                imageVector = Icons.Filled.Edit,
                contentDescription = "Edit Icon",
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}