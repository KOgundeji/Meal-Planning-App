package com.kunle.aisle9b.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kunle.aisle9b.TopBarOptions
import com.kunle.aisle9b.models.Food
import com.kunle.aisle9b.navigation.GroceryScreens
import com.kunle.aisle9b.templates.PreMadeListItem9
import com.kunle.aisle9b.util.ReconciliationDialog
import com.kunle.aisle9b.util.filterForReconciliation

@Composable
fun ListLibrary(
    shoppingVM: ShoppingVM,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    shoppingVM.screenHeader.value = GroceryScreens.headerTitle(GroceryScreens.PremadeListScreen)
    shoppingVM.topBar.value = TopBarOptions.SearchEnabled
    shoppingVM.searchSource.value = GroceryScreens.PremadeListScreen.name
    shoppingVM.filteredCustomLists.value = shoppingVM.customLists.collectAsState().value

    var primaryButtonBar by remember { mutableStateOf(shoppingVM.listPrimaryButtonBar.value) }
    var transferFoodsToGroceryList by remember { mutableStateOf(false) }
    val listsToAddToGroceryList = remember { mutableStateListOf(shoppingVM.groceryList.value) }

    if (transferFoodsToGroceryList) {
        val foodsForReconciliation = filterForReconciliation(
            lists = listsToAddToGroceryList,
            shoppingVM = shoppingVM
        )
        ReconciliationDialog(
            items = foodsForReconciliation,
            shoppingVM = shoppingVM,
            resetButtonBarToDefault = { primaryButtonBar = CustomListButtonBar.Default }
        ) {
            transferFoodsToGroceryList = false
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        when (primaryButtonBar) {
            CustomListButtonBar.Default -> {
                AddDeleteButtonBar(
                    onAddClick = { navController.navigate(GroceryScreens.AddCustomListScreen.name) },
                    primaryButtonBar = { primaryButtonBar = it })
            }
            CustomListButtonBar.Delete -> {
                FinalDeleteListButtonBar(
                    primaryButtonBar = { primaryButtonBar = it },
                    onDeleteClick = {
                        shoppingVM.groceryListDeleteList.forEach { customList ->
                            shoppingVM.deleteList(customList)
                            shoppingVM.deleteSpecificListWithGroceries(customList.listId)
                        }
                        primaryButtonBar = CustomListButtonBar.Default
                    })
            }
            CustomListButtonBar.Transfer -> {
                AddToGroceryListButtonBar(
                    transferList = listsToAddToGroceryList,
                    addLists = { transferFoodsToGroceryList = it }
                ) {
                    primaryButtonBar = CustomListButtonBar.Default
                }
            }
        }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            items(items = shoppingVM.filteredCustomLists.value) {
                PreMadeListItem9(
                    list = it,
                    primaryButtonBarAction = primaryButtonBar,
                    shoppingVM = shoppingVM,
                    transferList = listsToAddToGroceryList
                )
            }
        }
    }
}


@Composable
fun AddDeleteButtonBar(
    onAddClick: (Boolean) -> Unit,
    primaryButtonBar: (CustomListButtonBar) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            modifier = Modifier.width(75.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
            ),
            onClick = { onAddClick(true) }) {
            Icon(
                modifier = Modifier.size(30.dp),
                imageVector = Icons.Filled.AddCircle,
                contentDescription = null
            )
        }
        Button(
            modifier = Modifier.width(75.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
            ),
            onClick = { primaryButtonBar(CustomListButtonBar.Delete) }) {
            Icon(
                modifier = Modifier.size(30.dp),
                imageVector = Icons.Filled.Delete,
                contentDescription = null
            )
        }
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
            ),
            onClick = { primaryButtonBar(CustomListButtonBar.Transfer) }) {
            Icon(
                modifier = Modifier.size(30.dp),
                imageVector = Icons.Filled.DriveFileMoveRtl,
                contentDescription = null
            )
        }
    }
}


@Composable
fun FinalDeleteListButtonBar(
    primaryButtonBar: (CustomListButtonBar) -> Unit,
    onDeleteClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = { primaryButtonBar(CustomListButtonBar.Default) },
            modifier = Modifier.width(75.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
            )
        ) {
            Icon(
                modifier = Modifier.size(30.dp),
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Back button"
            )
        }
        Button(
            onClick = { onDeleteClick() },
            modifier = Modifier.width(75.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
            )
        ) {
            Icon(
                modifier = Modifier.size(30.dp),
                imageVector = Icons.Filled.DeleteForever,
                contentDescription = "Delete button"
            )
        }
    }
}

@Composable
fun AddToGroceryListButtonBar(
    transferList: MutableList<List<Food>>,
    addLists: (Boolean) -> Unit,
    primaryButtonBar: (CustomListButtonBar) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Button(
            onClick = { primaryButtonBar(CustomListButtonBar.Default) },
            modifier = Modifier.width(75.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
            )
        ) {
            Icon(
                modifier = Modifier.size(30.dp),
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Back button"
            )
        }
        Button(
            onClick = {
                if (transferList.isNotEmpty()) {
                    addLists(true)
                }
            },
            modifier = Modifier.width(75.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
            )
        ) {
            Icon(
                modifier = Modifier.size(30.dp),
                imageVector = Icons.Filled.DriveFileMoveRtl,
                contentDescription = "transfer to grocery list button"
            )
        }
    }
}

enum class CustomListButtonBar {
    Default,
    Delete,
    Transfer;
}