package com.kunle.aisle9b.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kunle.aisle9b.TopBarOptions
import com.kunle.aisle9b.models.*
import com.kunle.aisle9b.navigation.BottomNavigationBar9
import com.kunle.aisle9b.navigation.GroceryScreens
import com.kunle.aisle9b.screens.customLists.CustomListVM
import com.kunle.aisle9b.templates.EditFoodDialog9
import com.kunle.aisle9b.templates.ListItem9
import com.kunle.aisle9b.util.BackTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPreMadeListScreen(
    modifier: Modifier = Modifier,
    shoppingVM: SharedVM,
    customListVM: CustomListVM,
    navController: NavController,
    topBar: (TopBarOptions) -> Unit,
    source: (GroceryScreens) -> Unit
) {
    topBar(TopBarOptions.BackButton)
    source(GroceryScreens.AddCustomListScreen)

    val list = GroceryList(name = "")
    var customListName by remember { mutableStateOf("") }
    var showAddGroceryDialog by remember { mutableStateOf(false) }

    if (showAddGroceryDialog) {
        EditFoodDialog9(
            food = Food(name = "", quantity = "", isInGroceryList = false),
            closeDialog = { showAddGroceryDialog = false },
            setFood = { shoppingVM.tempGroceryList.add(it) }
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
        TextField(
            value = customListName,
            onValueChange = { customListName = it },
            placeholder = { Text(text = "Type custom grocery list name") },
            colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent),
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
                    shoppingVM.tempGroceryList.forEach {
                        shoppingVM.insertFood(it)
                        customListVM.insertList(
                            list = GroceryList(listId = list.listId, name = customListName)
                        )
                        customListVM.insertPair(
                            ListFoodMap(listId = list.listId, foodId = it.foodId)
                        )
                    }
                    shoppingVM.tempGroceryList.clear()
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
            items(items = shoppingVM.tempGroceryList) {
                ListItem9(
                    modifier = Modifier.padding(vertical = 3.dp),
                    food = it,
                    shoppingVM = shoppingVM,
                    checkBoxShown = false,
                    onEditClickNewFood = true
                )
            }
        }
    }
}