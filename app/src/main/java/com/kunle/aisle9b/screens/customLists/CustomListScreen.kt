package com.kunle.aisle9b.screens.customLists

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kunle.aisle9b.models.Food
import com.kunle.aisle9b.screens.GeneralVM
import com.kunle.aisle9b.templates.CustomSearchBar9
import com.kunle.aisle9b.templates.items.CustomListItem9
import com.kunle.aisle9b.util.ReconciliationDialog

@Composable
fun CustomListScreen(
    modifier: Modifier = Modifier,
    generalVM: GeneralVM = hiltViewModel(),
    customListVM: CustomListVM = hiltViewModel(),
    navToListDetails: (Long) -> Unit
) {
    val context = LocalContext.current
    val filteredCustomLists = customListVM.filteredCustomList.collectAsState().value

    var transferFoodsToGroceryList by remember { mutableStateOf(false) }
    var listsToAddToGroceryList by remember { mutableStateOf(emptyList<Food>()) }


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
            transferFoodsToGroceryList = false
        }

        Toast.makeText(context, "Groceries added to Grocery List", Toast.LENGTH_SHORT)
            .show()
    }
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(items = filteredCustomLists) { listItem ->
            CustomListItem9(
                groceryList = listItem,
                customListVM = customListVM,
                navToListDetails = { navToListDetails(listItem.listId) },
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