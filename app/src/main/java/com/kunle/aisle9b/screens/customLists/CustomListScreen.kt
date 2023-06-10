package com.kunle.aisle9b.screens.customLists

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
import com.kunle.aisle9b.TopBarOptions
import com.kunle.aisle9b.models.Food
import com.kunle.aisle9b.navigation.GroceryScreens
import com.kunle.aisle9b.screens.SharedVM
import com.kunle.aisle9b.templates.CustomSearchBar9
import com.kunle.aisle9b.templates.items.PreMadeListItem9
import com.kunle.aisle9b.util.*

@Composable
fun CustomListScreen(
    modifier: Modifier = Modifier,
    sharedVM: SharedVM,
    customListVM: CustomListVM,
    topBar: (TopBarOptions) -> Unit,
    source: (GroceryScreens) -> Unit
) {
    topBar(TopBarOptions.Default)
    source(GroceryScreens.CustomListScreen)

    val context = LocalContext.current

    val customLists = customListVM.customLists.collectAsState().value
    val categoryMap = sharedVM.categoryMap.collectAsState().value

    var primaryButtonBar = sharedVM.customListButtonBar.value

    var searchWord by remember { mutableStateOf("") }

    var transferFoodsToGroceryList by remember { mutableStateOf(false) }
    val listsToAddToGroceryList = remember { mutableStateListOf(sharedVM.groceryList.value) }

    var filteredCustomLists by remember { mutableStateOf(customLists) }

    if (transferFoodsToGroceryList) {
        val foodsForReconciliation = filterForReconciliation(
            lists = listsToAddToGroceryList,
            shoppingVM = sharedVM
        )
        if (foodsForReconciliation.isNotEmpty()) {
            ReconciliationDialog(
                items = foodsForReconciliation,
                sharedVM = sharedVM,
                resetButtonBarToDefault = {
                    topBar(TopBarOptions.Default)
                    sharedVM.customListButtonBar.value = CustomListButtonBar.Default
                }
            ) {
                transferFoodsToGroceryList = false
            }
        } else {
            topBar(TopBarOptions.Default)
            sharedVM.customListButtonBar.value = CustomListButtonBar.Default
        }
        Toast.makeText(context, "Groceries added to Grocery List", Toast.LENGTH_SHORT)
            .show()
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        CustomSearchBar9(
            text = searchWord,
            onValueChange = {
                searchWord = it
                filteredCustomLists = customLists.filter { list ->
                    list.name.contains(searchWord, ignoreCase = true)
                }
            },
            label = "Search in Custom Lists",
            trailingIcon = {
                if (searchWord.isNotEmpty()) {
                    IconButton(onClick = {
                        searchWord = ""
                        filteredCustomLists = customLists
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Cancel,
                            contentDescription = "Cancel button",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        )
        when (primaryButtonBar) {
            CustomListButtonBar.Default -> {}
            CustomListButtonBar.Delete -> {
                FinalDeleteListButtonBar(
                    topAppBar = topBar,
                    onBackClick = {
                        topBar(TopBarOptions.Default)
                        sharedVM.customListButtonBar.value = CustomListButtonBar.Default
                    },
                    onDeleteClick = {
                        sharedVM.groceryListDeleteList.forEach { customList ->
                            customListVM.deleteList(customList)
                            customListVM.deleteSpecificListWithGroceries(customList.listId)
                        }
                        topBar(TopBarOptions.Default)
                        sharedVM.customListButtonBar.value = CustomListButtonBar.Default
                    })
            }
            CustomListButtonBar.Transfer -> {
                AddToGroceryListButtonBar(
                    transferList = listsToAddToGroceryList,
                    topAppBar = topBar,
                    addLists = { food -> transferFoodsToGroceryList = food }
                ) {
                    topBar(TopBarOptions.Default)
                    sharedVM.customListButtonBar.value = CustomListButtonBar.Default
                }
            }
        }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            items(items = filteredCustomLists) { listItem ->
                PreMadeListItem9(
                    list = listItem,
                    categoryMap = categoryMap,
                    primaryButtonBarAction = primaryButtonBar,
                    sharedVM = sharedVM,
                    customListVM = customListVM,
                    transferList = listsToAddToGroceryList
                )
            }
        }
    }
}


@Composable
fun FinalDeleteListButtonBar(
    topAppBar: (TopBarOptions) -> Unit,
    onDeleteClick: () -> Unit,
    onBackClick: () -> Unit
) {
    topAppBar(TopBarOptions.Back)
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = {
                onBackClick()
            },
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
    topAppBar: (TopBarOptions) -> Unit,
    addLists: (Boolean) -> Unit,
    onBackClick: () -> Unit
) {
    topAppBar(TopBarOptions.Back)
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Button(
            onClick = {
                onBackClick()
            },
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