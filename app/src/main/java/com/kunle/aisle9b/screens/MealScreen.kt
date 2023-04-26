package com.kunle.aisle9b.screens

import android.widget.Toast
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.kunle.aisle9b.templates.MealItem9
import com.kunle.aisle9b.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealScreen(
    shoppingVM: ShoppingVM,
    modifier: Modifier = Modifier,
    navController: NavController,
    drawerState: DrawerState,
) {
    val context = LocalContext.current
    val transfer = shoppingVM.mealStartAsTransfer.value
    var topBar by remember { mutableStateOf(if (transfer) TopBarOptions.BackButton else TopBarOptions.Default) }
    var primaryButtonBar by remember { mutableStateOf(if (transfer) MealButtonBar.Transfer else MealButtonBar.Default) }
    shoppingVM.mealStartAsTransfer.value = false

    var multiFloatingState by remember { mutableStateOf(MultiFloatingState.Collapsed) }

    var transferFoodsToGroceryList by remember { mutableStateOf(false) }
    val listsToAddToGroceryList = remember { mutableStateListOf(shoppingVM.groceryList.value) }
    var searchWord by remember { mutableStateOf("") }
    val interactionSource = remember { MutableInteractionSource() }
    val mealList = shoppingVM.mealList.collectAsState().value
    var filteredMealLists by remember { mutableStateOf(mealList) }


    Scaffold(
        topBar = {
            when (topBar) {
                TopBarOptions.BackButton -> {
                    BackTopAppBar(
                        screenHeader = GroceryScreens.headerTitle(GroceryScreens.MealScreen)
                    ) {
                        topBar = TopBarOptions.Default
                        primaryButtonBar = MealButtonBar.Default
                    }
                }
                TopBarOptions.Default -> {
                    DefaultTopAppBar(
                        drawerState = drawerState,
                        screenHeader = GroceryScreens.headerTitle(GroceryScreens.MealScreen)
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
                onAddClick = { navController.navigate(GroceryScreens.AddMealsScreen.name) },
                onTransferClick = {
                    primaryButtonBar = MealButtonBar.Transfer
                    topBar = TopBarOptions.BackButton
                },
                onDeleteClick = {
                    primaryButtonBar = MealButtonBar.Delete
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
                        primaryButtonBar = MealButtonBar.Default
                    }
                ) {
                    transferFoodsToGroceryList = false
                }
            } else {
                topBar = TopBarOptions.Default
                primaryButtonBar = MealButtonBar.Default
            }
            Toast.makeText(context, "Groceries added to Grocery List", Toast.LENGTH_SHORT).show()
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
                onValueChange = { string ->
                    searchWord = string
                    filteredMealLists = mealList.filter { meal ->
                        meal.name.lowercase().contains(searchWord.lowercase())
                    }
                },
                interactionSource = interactionSource
            ) { onValueChange ->
                TextFieldDefaults.TextFieldDecorationBox(
                    value = searchWord,
                    innerTextField = onValueChange,
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
                                filteredMealLists = mealList.filter { meal ->
                                    meal.name.lowercase().contains(searchWord.lowercase())
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
                    label = { Text(text = "Search in Meals") },
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
                MealButtonBar.Default -> {}
                MealButtonBar.Delete -> {
                    FinalDeleteMeal_ButtonBar(
                        primaryButtonBar = { bar -> primaryButtonBar = bar },
                        topAppBar = { newTop -> topBar = newTop },
                        onDeleteClick = {
                            shoppingVM.mealDeleteList.forEach { meal ->
                                shoppingVM.deleteMeal(meal)
                                shoppingVM.deleteSpecificMealIngredients(meal.mealId)
                            }
                            primaryButtonBar = MealButtonBar.Default
                        })
                }
                MealButtonBar.Transfer -> {
                    AddMealToGroceryList_ButtonBar(
                        transferList = listsToAddToGroceryList,
                        topAppBar = { newTop -> topBar = newTop },
                        addLists = { transferFoodsToGroceryList = true }
                    ) {
                        primaryButtonBar = MealButtonBar.Default
                    }
                }
            }
            LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                items(items = filteredMealLists) { meal ->
                    MealItem9(
                        meal = meal,
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
fun FinalDeleteMeal_ButtonBar(
    primaryButtonBar: (MealButtonBar) -> Unit,
    topAppBar: (TopBarOptions) -> Unit,
    onDeleteClick: () -> Unit,
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
            onClick = {
                topAppBar(TopBarOptions.Default)
                primaryButtonBar(MealButtonBar.Default)
            }) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Back button",
                modifier = Modifier.size(30.dp)
            )
        }
        Button(
            modifier = Modifier.width(75.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
            ),
            onClick = { onDeleteClick() }) {
            Icon(
                imageVector = Icons.Filled.DeleteForever,
                contentDescription = "Delete button",
                modifier = Modifier.size(30.dp)
            )
        }
    }
}

@Composable
fun AddMealToGroceryList_ButtonBar(
    transferList: MutableList<List<Food>>,
    topAppBar: (TopBarOptions) -> Unit,
    addLists: () -> Unit,
    primaryButtonBar: (MealButtonBar) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Button(
            onClick = {
                topAppBar(TopBarOptions.Default)
                primaryButtonBar(MealButtonBar.Default)
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
                    addLists()
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

enum class MealButtonBar {
    Default,
    Delete,
    Transfer;
}


