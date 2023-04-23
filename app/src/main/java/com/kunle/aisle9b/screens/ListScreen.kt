package com.kunle.aisle9b.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.DriveFileMoveRtl
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kunle.aisle9b.TopBarOptions
import com.kunle.aisle9b.models.Food
import com.kunle.aisle9b.navigation.GroceryScreens
import com.kunle.aisle9b.templates.ListItem9
import com.kunle.aisle9b.ui.theme.DM_MediumGray
import kotlinx.coroutines.launch

@Composable
fun ListScreen(
    shoppingVM: ShoppingVM,
    modifier: Modifier = Modifier,
    navController: NavController
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
        if (groceryList.isEmpty()) {
            Column(
                modifier = modifier
                    .fillMaxHeight(.7f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Your Grocery List is currently empty!",
                    modifier = Modifier.padding(15.dp)
                )
                Button(
                    modifier = Modifier
                        .width(275.dp)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    ),
                    shape = RoundedCornerShape(30.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp),
                    onClick = {
                        navController.navigate(GroceryScreens.PremadeListScreen.name)
                        shoppingVM.topBar.value = TopBarOptions.SearchEnabled
                        shoppingVM.searchSource.value = GroceryScreens.PremadeListScreen.name
                        shoppingVM.listPrimaryButtonBar.value = CustomListButtonBar.Transfer
                    }) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.DriveFileMoveRtl,
                            contentDescription = "transfer saved list button"
                        )
                        Spacer(modifier = Modifier.width(7.dp))
                        Text(
                            text = "Load Saved Grocery List",
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
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    ),
                    shape = RoundedCornerShape(30.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp),
                    onClick = {
                        navController.navigate(GroceryScreens.MealScreen.name)
                        shoppingVM.topBar.value = TopBarOptions.SearchEnabled
                        shoppingVM.searchSource.value = GroceryScreens.MealScreen.name
                        shoppingVM.mealPrimaryButtonBar.value = MealButtonBar.Transfer
                    }) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.DriveFileMoveRtl,
                            contentDescription = "transfer meals button"
                        )
                        Spacer(modifier = Modifier.width(7.dp))
                        Text(
                            text = "Add Meal to Grocery List",
                            fontSize = 16.sp
                        )
                    }
                }
            }
        } else {
            LazyColumn(state = listState, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                items(items = groceryList) {
                    ListItem9(food = it, shoppingVM = shoppingVM)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroceryInputTextField(darkMode: Boolean, onAddGrocery: (Food) -> Unit) {

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val nameInteractionSource = remember { MutableInteractionSource() }
    val quantityInteractionSource = remember { MutableInteractionSource() }
    var item by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp, bottom = 15.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            modifier = Modifier.weight(.7f),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
            shape = RoundedCornerShape(3.dp)
        ) {
            BasicTextField(
                modifier = Modifier
                    .height(45.dp)
                    .fillMaxWidth(),
                value = item,
                singleLine = true,
                onValueChange = { item = it },
                interactionSource = nameInteractionSource
            ) {
                TextFieldDefaults.TextFieldDecorationBox(
                    value = item,
                    innerTextField = it,
                    enabled = true,
                    singleLine = true,
                    shape = RoundedCornerShape(3.dp),
                    label = { Text(text = "Add new item") },
                    visualTransformation = VisualTransformation.None,
                    interactionSource = nameInteractionSource,
                    contentPadding = PaddingValues(horizontal = 15.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = if (darkMode) DM_MediumGray else Color.White,
                        textColor = if (darkMode) Color.White else Color.Black
                    )
                )
            }
        }
        Spacer(modifier = Modifier.width(10.dp))
        Card(
            modifier = Modifier.weight(.4f),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
            shape = RoundedCornerShape(3.dp)
        ) {
            BasicTextField(
                modifier = Modifier
                    .height(45.dp)
                    .fillMaxWidth(),
                value = quantity,
                singleLine = true,
                onValueChange = { quantity = it },
                interactionSource = quantityInteractionSource
            ) {
                TextFieldDefaults.TextFieldDecorationBox(
                    value = quantity,
                    innerTextField = it,
                    enabled = true,
                    singleLine = true,
                    shape = RoundedCornerShape(3.dp),
                    label = { Text(text = "#") },
                    visualTransformation = VisualTransformation.None,
                    interactionSource = quantityInteractionSource,
                    contentPadding = PaddingValues(horizontal = 15.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = if (darkMode) DM_MediumGray else Color.White,
                        textColor = if (darkMode) Color.White else Color.Black
                    )
                )
            }
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
                    } else {
                        Toast
                            .makeText(context, "Please add a name", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "Add", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Preview(widthDp = 393, heightDp = 830, showBackground = true)
@Composable
fun ListPreview() {
    GroceryInputTextField(true) {}
}

