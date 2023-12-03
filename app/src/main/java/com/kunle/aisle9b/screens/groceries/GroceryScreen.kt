package com.kunle.aisle9b.screens.groceries

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DriveFileMoveRtl
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kunle.aisle9b.R
import com.kunle.aisle9b.TopBarOptions
import com.kunle.aisle9b.models.Grocery
import com.kunle.aisle9b.navigation.GroceryScreens
import com.kunle.aisle9b.screens.GeneralVM
import com.kunle.aisle9b.templates.CustomAutoComplete9
import com.kunle.aisle9b.templates.CustomTextField9
import com.kunle.aisle9b.templates.headers.CategoryHeader
import com.kunle.aisle9b.templates.items.GroceryListItem9
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GroceryScreen(
    modifier: Modifier = Modifier,
    generalVM: GeneralVM = hiltViewModel(),
    groceryVM: GroceryVM = hiltViewModel(),
    navToCustomLists: ()-> Unit,
    navToMealScreen: ()-> Unit
) {
    val groceryList = groceryVM.groceryList.collectAsState().value
    val groupedGroceryList = groceryVM.groupedGroceryList.collectAsState().value
    val categoriesOn = generalVM.categoriesSetting

    val listState = rememberLazyListState()
    LaunchedEffect(key1 = groceryList.size) {
        if (groceryList.isNotEmpty() && listState.firstVisibleItemIndex < 5) listState.animateScrollToItem(
            0
        )
    }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .semantics { contentDescription = R.string.grocery_screen.toString() }
    ) {
        GroceryInputTextField(groceryVM) { name, quantity ->
            coroutineScope.launch {
                groceryVM.insertGrocery(Grocery(name = name, quantity = quantity))
            }
        }
        if (groceryList.isEmpty()) {
            Column(
                modifier = modifier
                    .fillMaxHeight(.7f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.empty_list_message),
                    modifier = Modifier.padding(15.dp)
                )
                Button(
                    modifier = Modifier
                        .width(275.dp)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    shape = RoundedCornerShape(30.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp),
                    onClick = { navToCustomLists() }) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.DriveFileMoveRtl,
                            contentDescription = stringResource(R.string.transfer_custom_list_description)
                        )
                        Spacer(modifier = Modifier.width(7.dp))
                        Text(
                            text = stringResource(R.string.load_saved_grocery_list),
                            fontSize = 16.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(15.dp))
                Button(
                    modifier = Modifier
                        .width(275.dp)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    shape = RoundedCornerShape(30.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp),
                    onClick = { navToMealScreen() }) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.DriveFileMoveRtl,
                            contentDescription = stringResource(R.string.transfer_meal_description)
                        )
                        Spacer(modifier = Modifier.width(7.dp))
                        Text(
                            text = stringResource(R.string.add_meal_to_grocery_list),
                            fontSize = 16.sp
                        )
                    }
                }
            }
        } else {
            LazyColumn(state = listState, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                if (categoriesOn) {
                    groupedGroceryList.forEach { (category, groceries) ->
                        stickyHeader {
                            CategoryHeader(string = category)
                        }
                        items(items = groceries) { grocery ->
                            GroceryListItem9(
                                grocery = grocery,
                                viewModel = groceryVM,
                                modifier = Modifier.animateItemPlacement(),
                                onEditGrocery = { newGrocery ->
                                    coroutineScope.launch { groceryVM.upsertGrocery(newGrocery) }
                                }
                            )
                        }
                    }
                } else {
                    items(items = groceryList) { grocery ->
                        GroceryListItem9(
                            grocery = grocery,
                            viewModel = groceryVM,
                            modifier = Modifier.animateItemPlacement(),
                            onEditGrocery = { newGrocery ->
                                coroutineScope.launch { groceryVM.upsertGrocery(newGrocery) }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GroceryInputTextField(viewModel: GroceryVM, onAddGrocery: (String, String) -> Unit) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    var item by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp, bottom = 15.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.weight(.7f)) {
            CustomAutoComplete9(
                viewModel = viewModel,
                value = item,
                setValue = { item = it },
                label = "Add grocery"
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Card(
            modifier = Modifier.weight(.4f),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
            shape = RoundedCornerShape(3.dp)
        ) {
            CustomTextField9(
                modifier = Modifier
                    .height(45.dp)
                    .fillMaxWidth()
                    .semantics { contentDescription = "Grocery quantity" },
                text = quantity,
                onValueChange = { quantity = it },
                label = "#",
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(.2f)
                .height(45.dp)
                .clickable {
                    if (item.isNotEmpty()) {
                        onAddGrocery(item, quantity)
                        Toast
                            .makeText(context, "$item added", Toast.LENGTH_SHORT)
                            .show()
                        item = ""
                        quantity = ""
                        focusManager.clearFocus()
                    } else {
                        Toast
                            .makeText(context, "Please add a name", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .semantics { contentDescription = "Add grocery" },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "Add", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}


