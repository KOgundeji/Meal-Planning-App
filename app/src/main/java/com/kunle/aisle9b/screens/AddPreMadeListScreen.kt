package com.kunle.aisle9b.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kunle.aisle9b.TopBarOptions
import com.kunle.aisle9b.models.*
import com.kunle.aisle9b.navigation.GroceryScreens
import com.kunle.aisle9b.screens.customLists.CustomListVM
import com.kunle.aisle9b.templates.dialogs.EditFoodDialog9
import com.kunle.aisle9b.templates.items.CustomUpdateTextField9
import com.kunle.aisle9b.templates.items.ListItem9
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@Composable
fun AddPreMadeListScreen(
    modifier: Modifier = Modifier,
    customListVM: CustomListVM = hiltViewModel(),
    generalVM: GeneralVM = hiltViewModel(),
    navController: NavController
) {
    generalVM.setTopBarOption(TopBarOptions.Back)
    generalVM.setClickSource(GroceryScreens.AddNewCustomListScreen)

    val scope = rememberCoroutineScope()
    val listId = remember { customListVM.insertList(GroceryList.createBlank()) }
    var customListName by remember { mutableStateOf("") }
    var showAddGroceryDialog by remember { mutableStateOf(false) }

    val lwg = customListVM.groceriesOfCustomLists.collectAsState().value.first {
        it.list.listId == listId
    }

    if (showAddGroceryDialog) {
        EditFoodDialog9(
            oldFood = Food.createBlank(),
            closeDialog = { showAddGroceryDialog = false },
            setFood = { newFood ->
                scope.launch {
                    val foodId = async { customListVM.insertFood(newFood) }.await()
                    customListVM.insertPair(ListFoodMap(listId = listId, foodId = foodId))
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
        CustomUpdateTextField9(
            text = customListName,
            onValueChange = { customListName = it },
            onSaveClick = {
                customListVM.updateName(
                    GroceryListNameUpdate(
                        listId = listId,
                        listName = customListName
                    )
                )
            },
            label = "Type custom grocery list name",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { showAddGroceryDialog = true },
                modifier = Modifier.width(75.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                )
            ) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = Icons.Filled.AddCircle,
                    contentDescription = "Add button"
                )
            }
            Button(
                onClick = { },
                modifier = Modifier.width(75.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                )
            ) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete button"
                )
            }
            Button(
                onClick = {
                    customListVM.updateVisibility(GroceryListVisibilityUpdate(listId = listId))
                    navController.navigate(GroceryScreens.CustomListScreen.name)
                },
                modifier = Modifier.width(75.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer
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
                        scope.launch { customListVM.upsertFood(newFood) }
                    }
                )
            }
        }
    }
}