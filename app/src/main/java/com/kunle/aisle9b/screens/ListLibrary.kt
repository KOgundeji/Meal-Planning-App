package com.kunle.aisle9b.screens

import android.widget.Toast
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kunle.aisle9b.MultiFloatingState
import com.kunle.aisle9b.TopBarOptions
import com.kunle.aisle9b.models.Food
import com.kunle.aisle9b.navigation.GroceryScreens
import com.kunle.aisle9b.templates.PreMadeListItem9
import com.kunle.aisle9b.ui.theme.DM_MediumGray
import com.kunle.aisle9b.util.ReconciliationDialog
import com.kunle.aisle9b.util.filterForReconciliation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListLibrary(
    shoppingVM: ShoppingVM,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    shoppingVM.screenHeader.value = GroceryScreens.headerTitle(GroceryScreens.PremadeListScreen)
    shoppingVM.topBar.value = TopBarOptions.Default
    shoppingVM.multiFloatingState.value = MultiFloatingState.Collapsed
    shoppingVM.fabEnabled.value = true
    shoppingVM.fabSource.value = GroceryScreens.PremadeListScreen.name
//    shoppingVM.searchSource.value = GroceryScreens.PremadeListScreen.name
//    shoppingVM.filteredCustomLists.value = shoppingVM.customLists.collectAsState().value

    var searchWord by remember { mutableStateOf("") }
    val interactionSource = remember { MutableInteractionSource() }
    var primaryButtonBar by remember { mutableStateOf(shoppingVM.listPrimaryButtonBar.value) }
    var transferFoodsToGroceryList by remember { mutableStateOf(false) }
    val listsToAddToGroceryList = remember { mutableStateListOf(shoppingVM.groceryList.value) }
    val customLists = shoppingVM.customLists.collectAsState().value
    var filteredCustomLists by remember { mutableStateOf(customLists) }

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
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        BasicTextField(
            modifier = Modifier
                .height(45.dp)
                .fillMaxWidth(0.85f),
            value = searchWord,
            singleLine = true,
            onValueChange = {
                searchWord = it
                filteredCustomLists = customLists.filter { list ->
                    list.name.lowercase().contains(searchWord.lowercase())
                }
            },
            interactionSource = interactionSource
        ) {
            TextFieldDefaults.TextFieldDecorationBox(
                value = searchWord,
                innerTextField = it,
                enabled = true,
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                        modifier = Modifier.size(24.dp)
                    )
                },
                trailingIcon = {
                    if (searchWord.isNotEmpty()) {
                        IconButton(onClick = {
                            searchWord = ""
                            filteredCustomLists = customLists.filter { list ->
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
                shape = RoundedCornerShape(40.dp),
                label = { Text(text = "Search in Custom Lists") },
                visualTransformation = VisualTransformation.None,
                interactionSource = interactionSource,
                contentPadding = PaddingValues(horizontal = 15.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    textColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
        }
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
                    shoppingVM.listPrimaryButtonBar.value = CustomListButtonBar.Default
                    primaryButtonBar = CustomListButtonBar.Default
                }
            }
        }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            items(items = filteredCustomLists) {
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