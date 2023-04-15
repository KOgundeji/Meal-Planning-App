package com.kunle.aisle9b.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kunle.aisle9b.models.GroceryList
import com.kunle.aisle9b.navigation.GroceryScreens
import com.kunle.aisle9b.templates.AddPreMadeListDialog9
import com.kunle.aisle9b.templates.PreMadeListItem9
import com.kunle.aisle9b.ui.theme.BaseOrange

//this will be a screen that houses custom grocery lists that users can
//jumpstart their grocery lists
//option to add list to grocery list only shows up when grocery list is empty
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListLibrary(
    shoppingViewModel: ShoppingViewModel,
    modifier: Modifier = Modifier,
    screenHeader: (String) -> Unit
) {
    val preMadeListHeader = GroceryScreens.headerTitle(GroceryScreens.PremadeListScreen)
    screenHeader(preMadeListHeader)

    var listDeleteEnabled by remember { mutableStateOf(false) }
    var showAddCustomListDialog by remember { mutableStateOf(false) }
    var searchWord by remember { mutableStateOf("") }
    val customLists = shoppingViewModel.premadeLists.collectAsState().value
    val filteredCustomLists = remember { mutableStateOf(customLists)}
    val tempGroceryList = shoppingViewModel.tempGroceryList

    if (showAddCustomListDialog) {
        AddPreMadeListDialog9(
            list = GroceryList(name = ""),
            shoppingViewModel = shoppingViewModel,
            setShowAddMealDialog = { showAddCustomListDialog = it })
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        if (!listDeleteEnabled) {
            AddDeleteListBar(
                onAddClick = { showAddCustomListDialog = it },
                deleteEnabled = { listDeleteEnabled = it })
        } else {
            SubDeleteListBar(
                shoppingViewModel = shoppingViewModel,
                deleteEnabled = { listDeleteEnabled = it },
                onDeleteClick = {
                    it.forEach { customList ->
                        shoppingViewModel.deleteList(customList)
                        shoppingViewModel.deleteSpecificListWithGroceries(customList.listId)
                    }
                    listDeleteEnabled = false
                })
        }
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(.9f),
            value = searchWord,
            onValueChange = {
                searchWord = it
                filteredCustomLists.value = customLists.filter {list ->
                    list.name.lowercase().contains(searchWord.lowercase())
                }
            },
            label = { Text(text = "Search") },
            placeholder = { Text(text = "Search custom grocery lists") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon",
                    modifier = Modifier
                        .padding(15.dp)
                        .size(24.dp)
                )
            },
            trailingIcon = {
                if (searchWord.isNotEmpty()) {
                    IconButton(onClick = {
                        searchWord = ""
                        filteredCustomLists.value = customLists.filter {list ->
                            list.name.lowercase().contains(searchWord.lowercase())
                        }}) {
                        Icon(
                            imageVector = Icons.Filled.Cancel,
                            contentDescription = "Cancel button",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            },
            singleLine = true,
            shape = RectangleShape,
            colors = TextFieldDefaults.textFieldColors(cursorColor = BaseOrange)
        )
        LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            items(items = filteredCustomLists.value) {
                PreMadeListItem9(
                    list = it,
                    deleteEnabled = listDeleteEnabled,
                    shoppingViewModel = shoppingViewModel
                )
            }
        }
    }
}


@Composable
fun AddDeleteListBar(
    onAddClick: (Boolean) -> Unit,
    deleteEnabled: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Row(
            modifier = Modifier.clickable { onAddClick(true) },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Filled.AddCircle,
                contentDescription = "Add button",
                modifier = Modifier.size(48.dp),
                tint = BaseOrange
            )
            Text(
                text = "Add",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
            )
        }
        Row(
            modifier = Modifier.clickable { deleteEnabled(true) },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Delete button",
                modifier = Modifier.size(48.dp),
                tint = BaseOrange
            )
            Text(
                text = "Delete",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
fun SubDeleteListBar(
    shoppingViewModel: ShoppingViewModel,
    deleteEnabled: (Boolean) -> Unit,
    onDeleteClick: (List<GroceryList>) -> Unit,
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Row(
            modifier = Modifier.clickable { onDeleteClick(shoppingViewModel.groceryListDeleteList) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Delete button",
                modifier = Modifier.size(48.dp),
                tint = BaseOrange
            )
            Text(
                text = "Delete",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Row(
            modifier = Modifier.clickable { deleteEnabled(false) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Back button",
                modifier = Modifier.size(48.dp),
                tint = BaseOrange
            )
            Text(
                text = "Go Back",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
