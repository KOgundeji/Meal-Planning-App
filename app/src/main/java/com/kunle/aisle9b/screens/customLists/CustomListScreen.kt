package com.kunle.aisle9b.screens.customLists

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kunle.aisle9b.models.Food
import com.kunle.aisle9b.screens.GeneralVM
import com.kunle.aisle9b.templates.items.CustomListItem9
import com.kunle.aisle9b.util.CustomBottomSheet9
import com.kunle.aisle9b.util.ReconciliationDialog
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomListScreen(
    modifier: Modifier = Modifier,
    generalVM: GeneralVM = hiltViewModel(),
    customListVM: CustomListVM = hiltViewModel(),
    navToListDetails: (Long) -> Unit
) {
    val context = LocalContext.current
    val filteredCustomLists = customListVM.filteredCustomList.collectAsState().value

    var showBottomSheet by remember { mutableStateOf(false) }
    var bottomSheetListId by remember { mutableLongStateOf(0L) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    var transferFoodsToGroceryList by remember { mutableStateOf(false) }
    var listsToAddToGroceryList by remember { mutableStateOf(emptyList<Food>()) }

    if (showBottomSheet) {
        val listWithGroceries = customListVM.findLWG(bottomSheetListId)
        CustomBottomSheet9(
            sheetState = sheetState,
            textLabels = arrayOf(
                "View List",
                "Transfer to Grocery List",
                "Edit List",
                "Delete List"
            ),
            closeBottomSheet = { showBottomSheet = false },
            viewList = { //same as edit
                if (listWithGroceries != null) {
                    navToListDetails(listWithGroceries.list.listId)
                }
                scope.launch {
                    sheetState.hide()
                    showBottomSheet = false
                }
            },
            transferFood = {
                if (listWithGroceries != null) {
                    listsToAddToGroceryList = listWithGroceries.groceries
                    transferFoodsToGroceryList = true
                }
                scope.launch {
                    sheetState.hide()
                    showBottomSheet = false
                }
            },
            edit = {
                if (listWithGroceries != null) {
                    navToListDetails(listWithGroceries.list.listId)
                }
                scope.launch {
                    sheetState.hide()
                    showBottomSheet = false
                }
            },
            delete = {
                if (listWithGroceries != null) {
                    customListVM.deleteList(listWithGroceries.list)
                    customListVM.deleteSpecificListWithGroceries(listWithGroceries.list.listId)
                    Toast.makeText(
                        context,
                        "${listWithGroceries.list.listName} deleted from Meals",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                scope.launch {
                    sheetState.hide()
                    showBottomSheet = false
                }
            },
            headerContent = {
                if (listWithGroceries != null) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 5.dp),
                        text = listWithGroceries.list.listName,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Start,
                        lineHeight = 24.sp
                    )
                }
            }
        )
    }

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
        items(items = filteredCustomLists, key = { it.listId }) { listItem ->
            CustomListItem9(
                lwg = customListVM.findLWG(listItem.listId),
                showBottomSheet = {
                    bottomSheetListId = listItem.listId
                    showBottomSheet = true
                },
            )
        }
    }
}

