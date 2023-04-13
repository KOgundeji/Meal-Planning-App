package com.kunle.aisle9b.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kunle.aisle9b.navigation.GroceryScreens
import com.kunle.aisle9b.templates.ListItem9
import kotlinx.coroutines.launch

@Composable
fun FoodListScreen(
    modifier: Modifier = Modifier,
    shoppingViewModel: ShoppingViewModel,
    screenHeader: (String) -> Unit
) {
    val foodList = shoppingViewModel.foodList.collectAsState().value
    val listState = rememberLazyListState()
    val coroutine = rememberCoroutineScope()

    screenHeader(GroceryScreens.FoodListScreen.name)

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
                modifier = Modifier.width(150.dp),
                onClick = {
                    coroutine.launch { listState.animateScrollToItem(0) }
                }) {
                Text(text = "Scroll to top")
            }

            Button(
                modifier = Modifier.width(150.dp),
                onClick = {
                    coroutine.launch { listState.animateScrollToItem(foodList.size - 1) }
                }) {
                Text(text = "Scroll to bottom")
            }
        }
        LazyColumn(state = listState, verticalArrangement = Arrangement.spacedBy(4.dp)) {
            items(items = foodList) {
                ListItem9(
                    food = it,
                    shoppingViewModel = shoppingViewModel,
                    checkBoxShown = false
                )
            }
        }
    }
}
