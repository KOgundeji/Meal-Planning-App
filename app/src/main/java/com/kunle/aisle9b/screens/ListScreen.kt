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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kunle.aisle9b.models.Food
import com.kunle.aisle9b.navigation.GroceryScreens
import com.kunle.aisle9b.templates.ListItem9
import com.kunle.aisle9b.ui.theme.OrangeTintDark
import kotlinx.coroutines.launch

@Composable
fun ListScreen(
    shoppingViewModel: ShoppingViewModel,
    modifier: Modifier = Modifier,
    screenHeader: (String) -> Unit
) {
    val listHeader = GroceryScreens.headerTitle(GroceryScreens.ListScreen)
    screenHeader(listHeader)

    val groceryList = shoppingViewModel.groceryList.collectAsState().value
    val groceryListCount = groceryList.size
    shoppingViewModel.screenList[0].badgeCount = groceryListCount

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = modifier.fillMaxSize()) {
        GroceryInputTextField(
            onAddGrocery = {
                shoppingViewModel.insertFood(it)
                coroutineScope.launch { listState.animateScrollToItem(index = 0) }
            }
        )
        LazyColumn(state = listState, verticalArrangement = Arrangement.spacedBy(4.dp)) {
            items(items = groceryList) {
                ListItem9(food = it, shoppingViewModel = shoppingViewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroceryInputTextField(onAddGrocery: (Food) -> Unit = {}) {

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
        TextField(
            value = item,
            singleLine = true,
            onValueChange = { item = it },
            modifier = Modifier.weight(.6f),
            colors = TextFieldDefaults.textFieldColors(containerColor = OrangeTintDark),
            label = { Text(text = "New item") }
        )
        Spacer(modifier = Modifier.width(10.dp))
        TextField(
            value = quantity,
            singleLine = true,
            onValueChange = { quantity = it },
            modifier = Modifier.weight(.4f),
            colors = TextFieldDefaults.textFieldColors(containerColor = Color.LightGray),
            label = { Text(text = "#") },
        )
        Spacer(modifier = Modifier.width(10.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .weight(.25f)
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
            colors = CardDefaults.cardColors(containerColor = Color.LightGray)
        ) {
            Box(
                contentAlignment = Alignment.Center,
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = Icons.Filled.AddCircle,
                        contentDescription = "Add Circle",
                        tint = Color.Black
                    )
                    Text(
                        text = "Add",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(widthDp = 393, heightDp = 830, showBackground = true)
@Composable
fun ListPreview() {
    GroceryInputTextField()
}

