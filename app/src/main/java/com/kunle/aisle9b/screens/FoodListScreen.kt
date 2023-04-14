package com.kunle.aisle9b.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kunle.aisle9b.navigation.GroceryScreens
import com.kunle.aisle9b.templates.ListItem9
import com.kunle.aisle9b.ui.theme.BaseOrange
import com.kunle.aisle9b.ui.theme.DM_DarkGray
import kotlinx.coroutines.launch

@Composable
fun FoodListScreen(
    modifier: Modifier = Modifier,
    shoppingViewModel: ShoppingViewModel,
    screenHeader: (String) -> Unit
) {
    val tempFoodList = shoppingViewModel.foodList.collectAsState().value
    val foodList = remember { mutableStateOf(tempFoodList) }
    
    val darkMode = shoppingViewModel.darkModeSetting.value
    val listState = rememberLazyListState()
    val coroutine = rememberCoroutineScope()
    var alphabeticalUp by remember { mutableStateOf(true) }

    val listHeader = GroceryScreens.headerTitle(GroceryScreens.FoodListScreen)
    screenHeader(listHeader)

    //eventually I want to be able to search by food in order to modify it!
    //might involve applying a filter function to foodList?
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "This is a list of all foods and ingredients that have ever been " +
                    "created using this app",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, top = 6.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                modifier = Modifier.width(125.dp),
                colors = ButtonDefaults.buttonColors(containerColor = if (darkMode) DM_DarkGray else BaseOrange),
                onClick = {
                    coroutine.launch { listState.animateScrollToItem(0) }
                }) {
                Text(text = "Scroll to top")
            }

            Button(
                modifier = Modifier.width(130.dp),
                colors = ButtonDefaults.buttonColors(containerColor = if (darkMode) DM_DarkGray else BaseOrange),
                onClick = {
                    coroutine.launch { listState.animateScrollToItem(foodList.size - 1) }
                }) {
                Text(text = "Scroll to end")
            }

            Button(
                modifier = Modifier.width(125.dp),
                colors = ButtonDefaults.buttonColors(containerColor = if (darkMode) DM_DarkGray else BaseOrange),
                onClick = {
                    alphabeticalUp = if (alphabeticalUp) {
                        foodList.value = foodList.value.sortedByDescending { it.name.lowercase() }
                        !alphabeticalUp
                    } else {
                        foodList.value = foodList.value.sortedBy { it.name.lowercase() }
                        !alphabeticalUp
                    }
                }) {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Order")
                    if (alphabeticalUp) {
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowUp,
                            contentDescription = "alphabetically sorting"
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowDown,
                            contentDescription = "reverse alphabetical sorting"
                        )
                    }
                }
            }
        }
        LazyColumn(state = listState, verticalArrangement = Arrangement.spacedBy(4.dp)) {
            items(items = foodList.value) {
                ListItem9(
                    food = it,
                    shoppingViewModel = shoppingViewModel,
                    checkBoxShown = false
                )
            }
        }
    }
}
