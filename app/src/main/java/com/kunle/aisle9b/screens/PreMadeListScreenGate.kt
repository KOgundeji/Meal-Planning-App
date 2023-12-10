package com.kunle.aisle9b.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kunle.aisle9b.TopBarOptions
import com.kunle.aisle9b.models.*
import com.kunle.aisle9b.navigation.GroceryScreens
import com.kunle.aisle9b.screens.customLists.CustomListVM
import com.kunle.aisle9b.screens.utilScreens.ErrorScreen
import com.kunle.aisle9b.screens.utilScreens.LoadingScreen
import com.kunle.aisle9b.templates.dialogs.EditFoodDialog9
import com.kunle.aisle9b.templates.CustomUpdateTextField9
import com.kunle.aisle9b.templates.dialogs.mealDialogs.HeadlineDialog9
import com.kunle.aisle9b.templates.items.ListItem9
import com.kunle.aisle9b.util.CustomListGateState
import com.kunle.aisle9b.util.IngredientResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@Composable
fun PreMadeListScreenGate(
    modifier: Modifier = Modifier,
    listId: Long?,
    customListVM: CustomListVM = hiltViewModel(),
    saveAndExitScreen: () -> Unit
) {
    if (listId == 0L) {
        LaunchedEffect(key1 = Unit) {
            customListVM.getBrandNewGroceryList()
        }
    } else {
        val list = customListVM.allGroceryLists.collectAsState().value.first {
            it.listId == listId
        }
        customListVM.setGateStateToSuccess(list)
    }

    when (val retrievedGroceryList = customListVM.gateState.collectAsState().value) {
        is CustomListGateState.Error -> ErrorScreen(errorText = retrievedGroceryList.getMessage())
        is CustomListGateState.Loading -> LoadingScreen()
        is CustomListGateState.Success -> {
            PreMadeListScreen(
                modifier = modifier,
                groceryList = retrievedGroceryList.groceryList,
                customListVM = customListVM,
                saveAndExitScreen = { saveAndExitScreen() }
            )

        }
    }
}

@Composable
private fun PreMadeListScreen(
    modifier: Modifier,
    groceryList: GroceryList,
    customListVM: CustomListVM,
    saveAndExitScreen: () -> Unit
) {
    when (val updateIngredients = customListVM.ingredientState.collectAsState().value) {
        is IngredientResponse.Error -> ErrorScreen(errorText = updateIngredients.getMessage())
        is IngredientResponse.Loading -> LoadingScreen()
        is IngredientResponse.Success, is IngredientResponse.Neutral -> {

            val scope = rememberCoroutineScope()
            val list =
                customListVM.allGroceryLists.collectAsState().value.find { it.listId == groceryList.listId }
            val listId = groceryList.listId
            var showAddGroceryDialog by remember { mutableStateOf(false) }
            var showChangeName by remember { mutableStateOf(false) }

            val lwg = customListVM.groceriesOfCustomLists.collectAsState().value.first {
                it.list.listId == listId
            }

            if (showChangeName) {
                HeadlineDialog9(
                    original = list?.listName ?: "No Name",
                    labelText = "Name of Grocery List",
                    closeDialog = { showChangeName = false },
                    onSave = {
                        customListVM.updateName(
                            GroceryListNameUpdate(
                                listId = listId,
                                listName = it
                            )
                        )
                        showChangeName = false
                    })
            }

            if (showAddGroceryDialog) {
                EditFoodDialog9(
                    oldFood = Food.createBlank(),
                    closeDialog = { showAddGroceryDialog = false },
                    setFood = { newFood ->
                        scope.launch {
                            customListVM.updateFoodList(food = newFood, listId = listId)
                        }
                    }
                )
            }
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(16.dp),
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = list?.listName ?: "No Name",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Icon(
                        modifier = Modifier
                            .size(36.dp)
                            .clickable { showChangeName = true },
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit Icon",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { showAddGroceryDialog = true },
                        modifier = Modifier.width(125.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        Icon(
                            modifier = Modifier.size(30.dp),
                            imageVector = Icons.Filled.AddCircle,
                            contentDescription = "Add button"
                        )
                    }
                    Button(
                        onClick = {
                            customListVM.updateVisibility(GroceryListVisibilityUpdate(listId = listId))
                            saveAndExitScreen()
                        },
                        modifier = Modifier.width(125.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        Icon(
                            modifier = Modifier.size(30.dp),
                            imageVector = Icons.Filled.Save,
                            contentDescription = "Save button"
                        )
                    }
                }
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 15.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(items = lwg.groceries) {
                        ListItem9(
                            modifier = Modifier.padding(vertical = 3.dp),
                            food = it,
                            viewModel = customListVM,
                            checkBoxShown = false,
                            onEditFood = { newFood ->
                                scope.launch { customListVM.updateFood(newFood) }
                            }
                        )
                    }
                }
            }
        }
    }
}