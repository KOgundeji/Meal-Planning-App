package com.kunle.aisle9b.screens.customLists

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
import com.kunle.aisle9b.navigation.BottomNavigationBar9
import com.kunle.aisle9b.navigation.GroceryScreens
import com.kunle.aisle9b.screens.SharedVM
import com.kunle.aisle9b.templates.PreMadeListItem9
import com.kunle.aisle9b.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomListScreen(
    modifier: Modifier = Modifier,
    shoppingVM: SharedVM,
    customListVM: CustomListVM,
    topBar: (TopBarOptions) -> Unit,
    source: (GroceryScreens) -> Unit
) {
    topBar(TopBarOptions.Default)
    source(GroceryScreens.CustomListScreen)

    val context = LocalContext.current

    var primaryButtonBar = shoppingVM.customListButtonBar.value

    var searchWord by remember { mutableStateOf("") }
    val interactionSource = remember { MutableInteractionSource() }

    var transferFoodsToGroceryList by remember { mutableStateOf(false) }
    val listsToAddToGroceryList = remember { mutableStateListOf(shoppingVM.groceryList.value) }
    val customLists = customListVM.customLists.collectAsState().value
    var filteredCustomLists by remember { mutableStateOf(customLists) }

    if (transferFoodsToGroceryList) {
        val foodsForReconciliation = filterForReconciliation(
            lists = listsToAddToGroceryList,
            shoppingVM = shoppingVM
        )
        if (foodsForReconciliation.isNotEmpty()) {
            ReconciliationDialog(
                items = foodsForReconciliation,
                shoppingVM = shoppingVM,
                resetButtonBarToDefault = {
                    topBar(TopBarOptions.Default)
                    shoppingVM.customListButtonBar.value = CustomListButtonBar.Default
                }
            ) {
                transferFoodsToGroceryList = false
            }
        } else {
            topBar(TopBarOptions.Default)
            shoppingVM.customListButtonBar.value = CustomListButtonBar.Default
        }
        Toast.makeText(context, "Groceries added to Grocery List", Toast.LENGTH_SHORT)
            .show()
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
            CustomListButtonBar.Default -> {}
            CustomListButtonBar.Delete -> {
                FinalDeleteListButtonBar(
                    topAppBar = topBar,
                    onBackClick = {
                        topBar(TopBarOptions.Default)
                        shoppingVM.customListButtonBar.value = CustomListButtonBar.Default
                    },
                    onDeleteClick = {
                        shoppingVM.groceryListDeleteList.forEach { customList ->
                            customListVM.deleteList(customList)
                            customListVM.deleteSpecificListWithGroceries(customList.listId)
                        }
                        topBar(TopBarOptions.Default)
                        shoppingVM.customListButtonBar.value = CustomListButtonBar.Default
                    })
            }
            CustomListButtonBar.Transfer -> {
                AddToGroceryListButtonBar(
                    transferList = listsToAddToGroceryList,
                    topAppBar = topBar,
                    addLists = { food -> transferFoodsToGroceryList = food }
                ) {
                    topBar(TopBarOptions.Default)
                    shoppingVM.customListButtonBar.value = CustomListButtonBar.Default
                }
            }
        }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            items(items = filteredCustomLists) { listItem ->
                PreMadeListItem9(
                    list = listItem,
                    primaryButtonBarAction = primaryButtonBar,
                    shoppingVM = shoppingVM,
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
    topAppBar(TopBarOptions.BackButton)
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
    topAppBar(TopBarOptions.BackButton)
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