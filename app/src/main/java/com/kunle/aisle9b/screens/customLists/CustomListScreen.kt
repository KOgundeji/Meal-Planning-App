package com.kunle.aisle9b.screens.customLists

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kunle.aisle9b.TopBarOptions
import com.kunle.aisle9b.models.Food
import com.kunle.aisle9b.navigation.GroceryScreens
import com.kunle.aisle9b.screens.GeneralVM
import com.kunle.aisle9b.templates.CustomSearchBar9
import com.kunle.aisle9b.templates.items.CustomListItem9
import com.kunle.aisle9b.util.ReconciliationDialog

@Composable
fun CustomListScreen(
    modifier: Modifier = Modifier,
    generalVM: GeneralVM = hiltViewModel(),
    customListVM: CustomListVM = hiltViewModel()
) {
    generalVM.setClickSource(GroceryScreens.CustomListScreen)

    val context = LocalContext.current
    val customLists = customListVM.customLists.collectAsState().value
    var searchWord by remember { mutableStateOf("") }

    var transferFoodsToGroceryList by remember { mutableStateOf(false) }
    var listsToAddToGroceryList by remember { mutableStateOf(emptyList<Food>()) }

    var filteredCustomLists by remember { mutableStateOf(customLists) }

    if (transferFoodsToGroceryList) {
        val foodsForReconciliation =
            generalVM.filterForReconciliation(
                listToAdd = listsToAddToGroceryList
            )

        if (foodsForReconciliation.isNotEmpty()) {
            ReconciliationDialog(
                items = foodsForReconciliation,
                viewModel = customListVM,
            ) {
                transferFoodsToGroceryList = false
            }
        } else {
            generalVM.setTopBarOption(TopBarOptions.Default)
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
                    list.listName.contains(searchWord, ignoreCase = true)
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
        LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            items(items = filteredCustomLists) { listItem ->
                CustomListItem9(
                    groceryList = listItem,
                    customListVM = customListVM,
                    deleteList = {
                        customListVM.deleteList(listItem)
                        customListVM.deleteSpecificListWithGroceries(listItem.listId)
                    },
                    transferFood = { groceries ->
                        listsToAddToGroceryList = groceries
                        transferFoodsToGroceryList = true
                    }


                )
            }
        }
    }
}