package com.kunle.aisle9b.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
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
import com.kunle.aisle9b.navigation.GroceryScreens
import com.kunle.aisle9b.templates.EditFoodDialog9
import com.kunle.aisle9b.templates.ListItem9
import com.kunle.aisle9b.ui.theme.BaseOrange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPreMadeListScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    shoppingVM: ShoppingVM
) {
    shoppingVM.screenHeader.value = GroceryScreens.headerTitle(GroceryScreens.AddCustomListScreen)
    shoppingVM.topBar.value = TopBarOptions.BackButton

    val list = GroceryList(name = "")
    var customListName by remember { mutableStateOf("") }
    var showAddGroceryDialog by remember { mutableStateOf(false) }


    if (showAddGroceryDialog) {
        EditFoodDialog9(
            food = Food(name = "", quantity = "", isInGroceryList = false),
            setShowSelfDialog = { showAddGroceryDialog = it },
            setFood = { shoppingVM.tempGroceryList.add(it) }
        )
    }
    Column(
        modifier = modifier
            .padding(20.dp)
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(16.dp),
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        TextField(
            value = customListName,
            onValueChange = { customListName = it },
            placeholder = { Text(text = "Type custom grocery list name") },
            colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )
        Spacer(modifier = Modifier.height(15.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Filled.AddCircle,
                contentDescription = "Add button",
                tint = BaseOrange,
                modifier = Modifier
                    .size(48.dp)
                    .clickable {
                        showAddGroceryDialog = true
                    }
            )
            Spacer(modifier = Modifier.width(40.dp))
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Delete button",
                tint = BaseOrange,
                modifier = Modifier
                    .size(48.dp)
                    .clickable {

                    }
            )
            Spacer(modifier = Modifier.width(40.dp))
            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = "Save button",
                tint = BaseOrange,
                modifier = Modifier
                    .size(48.dp)
                    .clickable {
                        shoppingVM.tempGroceryList.forEach {
                            shoppingVM.insertFood(it)
                            shoppingVM.insertList(
                                list = GroceryList(listId = list.listId, name = customListName)
                            )
                            shoppingVM.insertPair(
                                ListFoodMap(listId = list.listId, foodId = it.foodId)
                            )
                        }
                        shoppingVM.tempGroceryList.clear()
                        navController.navigate(GroceryScreens.PremadeListScreen.name)
                    }
            )

        }
        Spacer(modifier = Modifier.height(10.dp))
        LazyColumn {
            items(items = shoppingVM.tempGroceryList) {
                ListItem9(
                    food = it,
                    shoppingVM = shoppingVM,
                    checkBoxShown = false,
                    onEditClickNewFood = true
                )
            }
        }
    }
}