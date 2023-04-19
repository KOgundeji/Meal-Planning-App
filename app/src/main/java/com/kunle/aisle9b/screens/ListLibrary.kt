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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kunle.aisle9b.TopBarOptions
import com.kunle.aisle9b.models.Food
import com.kunle.aisle9b.models.GroceryList
import com.kunle.aisle9b.navigation.GroceryScreens
import com.kunle.aisle9b.templates.PreMadeListItem9
import com.kunle.aisle9b.ui.theme.BaseOrange
import com.kunle.aisle9b.util.ReconciliationDialog
import com.kunle.aisle9b.util.filterForReconciliation

//this will be a screen that houses custom grocery lists that users can
//jumpstart their grocery lists
//option to add list to grocery list only shows up when grocery list is empty
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListLibrary(
    shoppingViewModel: ShoppingViewModel,
    modifier: Modifier = Modifier,
    navController: NavController,
    topBarOption: (String) -> Unit,
    screenHeader: (String) -> Unit
) {
    val preMadeListHeader = GroceryScreens.headerTitle(GroceryScreens.PremadeListScreen)
    screenHeader(preMadeListHeader)
    topBarOption(TopBarOptions.SearchEnabled.name)

    val context = LocalContext.current

    var primaryButtonBar by remember { mutableStateOf(0) }
    var transferFoodsToGroceryList by remember { mutableStateOf(false) }
    var searchWord by remember { mutableStateOf("") }

    val customLists = shoppingViewModel.premadeLists.collectAsState().value
    val filteredCustomLists = remember { mutableStateOf(customLists) }

    //the below line starts the list with the existing grocery list
    val listsToAddToGroceryList =
        remember { mutableStateListOf(shoppingViewModel.groceryList.value) }

    if (transferFoodsToGroceryList) {
        val foodsForReconciliation = filterForReconciliation(
            lists = listsToAddToGroceryList,
            shoppingViewModel = shoppingViewModel
        )
        ReconciliationDialog(
            items = foodsForReconciliation,
            shoppingViewModel = shoppingViewModel,
            resetListLibraryToDefault = { primaryButtonBar = 0 }
        ) {
            transferFoodsToGroceryList = it
        }
        Toast.makeText(context, "Groceries added to Grocery List", Toast.LENGTH_SHORT).show()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        when (primaryButtonBar) {
            0 -> {
                AddDeleteButtonBar(
                    onAddClick = { navController.navigate(GroceryScreens.AddCustomListScreen.name) },
                    primaryButtonBar = { primaryButtonBar = it })
            }
            1 -> {
                FinalDeleteListButtonBar(
                    shoppingViewModel = shoppingViewModel,
                    primaryButtonBar = { primaryButtonBar = it },
                    onDeleteClick = {
                        it.forEach { customList ->
                            shoppingViewModel.deleteList(customList)
                            shoppingViewModel.deleteSpecificListWithGroceries(customList.listId)
                        }
                        primaryButtonBar = 0
                    })
            }
            else -> {
                AddToGroceryListButtonBar(
                    shoppingViewModel = shoppingViewModel,
                    transferList = listsToAddToGroceryList,
                    addLists = { transferFoodsToGroceryList = it }
                ) {
                    primaryButtonBar = 0
                }
            }
        }

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(.9f),
            value = searchWord,
            onValueChange = {
                searchWord = it
                filteredCustomLists.value = customLists.filter { list ->
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
                        filteredCustomLists.value = customLists.filter { list ->
                            list.name.lowercase().contains(searchWord.lowercase())
                        }
                    }) {
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
            items(items = shoppingViewModel.filteredList.value) {
                PreMadeListItem9(
                    list = it,
                    primaryButtonBarAction = primaryButtonBar,
                    shoppingViewModel = shoppingViewModel,
                    transferList = listsToAddToGroceryList
                )
            }
        }
    }
}


@Composable
fun AddDeleteButtonBar(
    onAddClick: (Boolean) -> Unit,
    primaryButtonBar: (Int) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                modifier = Modifier.width(140.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ),
                onClick = { onAddClick(true) }) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        imageVector = Icons.Filled.AddCircle,
                        contentDescription = null
                    )
                    Text(
                        text = "Add",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
            Button(
                modifier = Modifier.width(140.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ),
                onClick = { primaryButtonBar(1) }) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        imageVector = Icons.Filled.Delete,
                        contentDescription = null
                    )
                    Text(
                        text = "Delete",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ),
                onClick = { primaryButtonBar(2) }) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        imageVector = Icons.Filled.DriveFileMoveRtl,
                        contentDescription = null
                    )
                    Text(
                        text = "Transfer",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }
}

@Composable
fun FinalDeleteListButtonBar(
    shoppingViewModel: ShoppingViewModel,
    primaryButtonBar: (Int) -> Unit,
    onDeleteClick: (List<GroceryList>) -> Unit,
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(onClick = {}) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "delete button"
                )
                Text(
                    text = "Delete",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
        Button(onClick = { primaryButtonBar(0) }) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back button"
                )
                Text(
                    text = "Go Back",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Composable
fun AddToGroceryListButtonBar(
    shoppingViewModel: ShoppingViewModel,
    transferList: MutableList<List<Food>>,
    addLists: (Boolean) -> Unit,
    primaryButtonBar: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Button(onClick = {
            if (transferList.isNotEmpty()) {
                addLists(true)
            }
        }) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = Icons.Filled.DriveFileMoveRtl,
                    contentDescription = "transfer to grocery list button"
                )
                Text(
                    text = "Transfer",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
        Button(onClick = { primaryButtonBar(0) }) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back button"
                )
                Text(
                    text = "Go Back",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }

    }

}

@Preview(showBackground = true, widthDp = 320, heightDp = 800)
@Composable
fun Preview() {
    AddDeleteButtonBar(onAddClick = {}, primaryButtonBar = {})
}