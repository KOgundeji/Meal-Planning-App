package com.kunle.aisle9b.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kunle.aisle9b.TopBarOptions
import com.kunle.aisle9b.models.Food
import com.kunle.aisle9b.navigation.GroceryScreens
import com.kunle.aisle9b.templates.ListItem9
import com.kunle.aisle9b.ui.theme.DM_MediumGray
import kotlinx.coroutines.launch

@Composable
fun ListScreen(
    shoppingVM: ShoppingVM,
    modifier: Modifier = Modifier
) {
    shoppingVM.screenHeader.value = GroceryScreens.headerTitle(GroceryScreens.ListScreen)
    shoppingVM.topBar.value = TopBarOptions.Default

    val groceryList = shoppingVM.groceryList.collectAsState().value
    val darkMode = shoppingVM.darkModeSetting.value
    shoppingVM.groceryBadgeCount.value = groceryList.size

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = modifier.fillMaxSize()) {
        GroceryInputTextField(
            darkMode = darkMode
        ) {
            shoppingVM.insertFood(it)
            shoppingVM.groceryBadgeCount.value += 1
            coroutineScope.launch { listState.animateScrollToItem(index = 0) }
        }
        LazyColumn(state = listState, verticalArrangement = Arrangement.spacedBy(4.dp)) {
            items(items = groceryList) {
                ListItem9(food = it, shoppingVM = shoppingVM)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroceryInputTextField(darkMode: Boolean, onAddGrocery: (Food) -> Unit) {

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    var item by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = item,
            singleLine = true,
            onValueChange = { item = it },
            modifier = Modifier.weight(.7f),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = if (darkMode) DM_MediumGray else Color.White,
                textColor = if (darkMode) Color.White else Color.Black
            ),
            label = { Text(text = "New item") }
        )
        Spacer(modifier = Modifier.width(10.dp))
        OutlinedTextField(
            value = quantity,
            singleLine = true,
            onValueChange = { quantity = it },
            modifier = Modifier.weight(.4f),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = if (darkMode) DM_MediumGray else Color.White,
                textColor = if (darkMode) Color.White else Color.Black
            ),
            label = { Text(text = "#") },
        )
        Spacer(modifier = Modifier.width(10.dp))
        Icon(
            modifier = Modifier
                .height(55.dp)
                .weight(.2f)
                .clickable {
                    if (item.isNotEmpty()) {
                        onAddGrocery(
                            Food(
                                name = item,
                                quantity = quantity,
                                isInGroceryList = true
                            )
                        )
                        Toast
                            .makeText(context, "$item added", Toast.LENGTH_SHORT)
                            .show()
                        item = ""
                        quantity = ""
                        focusManager.clearFocus()
                    }
                },
            imageVector = Icons.Filled.AddCircle,
            contentDescription = "Add Circle",
            tint = if (darkMode) Color.White else Color.Black
        )
    }
}

@Preview(widthDp = 393, heightDp = 830, showBackground = true)
@Composable
fun ListPreview() {
    GroceryInputTextField(true) {}
}

