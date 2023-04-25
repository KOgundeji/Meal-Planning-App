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
import com.kunle.aisle9b.navigation.GroceryScreens
import com.kunle.aisle9b.templates.MealItem9
import com.kunle.aisle9b.util.ReconciliationDialog
import com.kunle.aisle9b.util.filterForReconciliation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealScreen(
    shoppingVM: ShoppingVM,
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    shoppingVM.screenHeader.value = GroceryScreens.headerTitle(GroceryScreens.MealScreen)
    shoppingVM.topBar.value = TopBarOptions.Default
    shoppingVM.searchSource.value = GroceryScreens.MealScreen.name
    shoppingVM.multiFloatingState.value = MultiFloatingState.Collapsed
    shoppingVM.fabEnabled.value = true
    shoppingVM.fabSource.value = GroceryScreens.MealScreen.name
    shoppingVM.filteredMeals.value = shoppingVM.mealList.collectAsState().value

    var primaryButtonBar by remember { mutableStateOf(shoppingVM.mealPrimaryButtonBar.value) }
    var transferFoodsToGroceryList by remember { mutableStateOf(false) }
    val listsToAddToGroceryList = remember { mutableStateListOf(shoppingVM.groceryList.value) }
    var searchWord by remember { mutableStateOf("") }
    val interactionSource = remember { MutableInteractionSource() }
    val mealList = shoppingVM.mealList.collectAsState().value
    var filteredMealLists by remember { mutableStateOf(mealList) }

    if (transferFoodsToGroceryList) {
        val foodsForReconciliation = filterForReconciliation(
            lists = listsToAddToGroceryList,
            shoppingVM = shoppingVM
        )
        ReconciliationDialog(
            items = foodsForReconciliation,
            shoppingVM = shoppingVM,
            resetButtonBarToDefault = { primaryButtonBar = MealButtonBar.Default }
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
                filteredMealLists = mealList.filter { meal ->
                    meal.name.lowercase().contains(searchWord.lowercase())
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
            MealButtonBar.Default -> {
                AddDeleteBar(
                    navController = navController,
                    primaryButtonBar = { primaryButtonBar = it })
            }
            MealButtonBar.Delete -> {
                FinalDeleteMeal_ButtonBar(
                    primaryButtonBar = { primaryButtonBar = it },
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
                    addLists = { transferFoodsToGroceryList = true }
                ) {
                    shoppingVM.mealPrimaryButtonBar.value = MealButtonBar.Default
                    primaryButtonBar = MealButtonBar.Default
                }
            }
        }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            items(items = filteredMealLists) {
                MealItem9(
                    meal = it,
                    primaryButtonBarAction = primaryButtonBar,
                    shoppingVM = shoppingVM,
                    transferList = listsToAddToGroceryList
                )
            }
        }
    }
}

@Composable
fun AddDeleteBar(
    navController: NavController,
    primaryButtonBar: (MealButtonBar) -> Unit
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
            onClick = { navController.navigate(GroceryScreens.AddMealsScreen.name) }) {
            Icon(
                imageVector = Icons.Filled.AddCircle,
                contentDescription = "Add button",
                modifier = Modifier.size(30.dp)
            )
        }
        Button(
            modifier = Modifier.width(75.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
            ),
            onClick = { primaryButtonBar(MealButtonBar.Delete) }) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Delete button",
                modifier = Modifier.size(30.dp)
            )
        }
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
            ),
            onClick = { primaryButtonBar(MealButtonBar.Transfer) }) {
            Icon(
                modifier = Modifier.size(30.dp),
                imageVector = Icons.Filled.DriveFileMoveRtl,
                contentDescription = null
            )
        }
    }
}

@Composable
fun FinalDeleteMeal_ButtonBar(
    primaryButtonBar: (MealButtonBar) -> Unit,
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
            onClick = { primaryButtonBar(MealButtonBar.Default) }) {
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
    addLists: () -> Unit,
    primaryButtonBar: (MealButtonBar) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Button(
            onClick = { primaryButtonBar(MealButtonBar.Default) },
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


