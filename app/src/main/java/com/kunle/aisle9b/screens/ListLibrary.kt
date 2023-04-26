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
import com.kunle.aisle9b.navigation.BottomNavigationBar9
import com.kunle.aisle9b.navigation.GroceryScreens
import com.kunle.aisle9b.templates.PreMadeListItem9
import com.kunle.aisle9b.ui.theme.DM_MediumGray
import com.kunle.aisle9b.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListLibrary(
    shoppingVM: ShoppingVM,
    modifier: Modifier = Modifier,
    navController: NavController,
    drawerState: DrawerState
) {
    val context = LocalContext.current
    val transfer = shoppingVM.customListStartAsTransfer.value

    var multiFloatingState by remember { mutableStateOf(MultiFloatingState.Collapsed) }
    var topBar by remember { mutableStateOf(if (transfer) TopBarOptions.BackButton else TopBarOptions.Default) }
    var primaryButtonBar by remember { mutableStateOf(if (transfer) CustomListButtonBar.Transfer else CustomListButtonBar.Default) }
    shoppingVM.customListStartAsTransfer.value = false

    var searchWord by remember { mutableStateOf("") }
    val interactionSource = remember { MutableInteractionSource() }

    var transferFoodsToGroceryList by remember { mutableStateOf(false) }
    val listsToAddToGroceryList = remember { mutableStateListOf(shoppingVM.groceryList.value) }
    val customLists = shoppingVM.customLists.collectAsState().value
    var filteredCustomLists by remember { mutableStateOf(customLists) }

    Scaffold(
        topBar = {
            when (topBar) {
                TopBarOptions.BackButton -> {
                    BackTopAppBar(
                        screenHeader = GroceryScreens.headerTitle(GroceryScreens.PremadeListScreen)
                    ) {
                        topBar = TopBarOptions.Default
                        primaryButtonBar = CustomListButtonBar.Default
                    }
                }
                TopBarOptions.Default -> {
                    DefaultTopAppBar(
                        drawerState = drawerState,
                        screenHeader = GroceryScreens.headerTitle(GroceryScreens.PremadeListScreen)
                    )
                }
            }
        }, bottomBar = {
            BottomNavigationBar9(
                items = shoppingVM.screenList,
                navController = navController,
                badgeCount = shoppingVM.groceryBadgeCount.value,
                onItemClick = {
                    navController.navigate(it.route)
                })
        },
        floatingActionButton = {
            FAB(
                onAddClick = { navController.navigate(GroceryScreens.AddCustomListScreen.name) },
                onTransferClick = {
                    primaryButtonBar = CustomListButtonBar.Transfer
                    topBar = TopBarOptions.BackButton
                },
                onDeleteClick = {
                    primaryButtonBar = CustomListButtonBar.Delete
                    topBar = TopBarOptions.BackButton
                },
                multiFloatingState = multiFloatingState,
                onMultiFabStateChange = { multiFloatingState = it }
            )
        }) {
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
                        topBar = TopBarOptions.Default
                        primaryButtonBar = CustomListButtonBar.Default
                    }
                ) {
                    transferFoodsToGroceryList = false
                }
            } else {
                topBar = TopBarOptions.Default
                primaryButtonBar = CustomListButtonBar.Default
            }
            Toast.makeText(context, "Groceries added to Grocery List", Toast.LENGTH_SHORT)
                .show()
        }

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(it),
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
                        primaryButtonBar = { newBar -> primaryButtonBar = newBar },
                        topAppBar = { newTop -> topBar = newTop },
                        onDeleteClick = {
                            shoppingVM.groceryListDeleteList.forEach { customList ->
                                shoppingVM.deleteList(customList)
                                shoppingVM.deleteSpecificListWithGroceries(customList.listId)
                            }
                            topBar = TopBarOptions.Default
                            primaryButtonBar = CustomListButtonBar.Default
                        })
                }
                CustomListButtonBar.Transfer -> {
                    AddToGroceryListButtonBar(
                        transferList = listsToAddToGroceryList,
                        topAppBar = { newTop -> topBar = newTop },
                        addLists = { food -> transferFoodsToGroceryList = food }
                    ) {
                        topBar = TopBarOptions.Default
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
}


@Composable
fun FinalDeleteListButtonBar(
    primaryButtonBar: (CustomListButtonBar) -> Unit,
    topAppBar: (TopBarOptions) -> Unit,
    onDeleteClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = {
                topAppBar(TopBarOptions.Default)
                primaryButtonBar(CustomListButtonBar.Default)
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
    primaryButtonBar: (CustomListButtonBar) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Button(
            onClick = {
                topAppBar(TopBarOptions.Default)
                primaryButtonBar(CustomListButtonBar.Default)
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