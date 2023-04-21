package com.kunle.aisle9b.util

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kunle.aisle9b.TopBarOptions
import com.kunle.aisle9b.navigation.GroceryScreens
import com.kunle.aisle9b.screens.ShoppingVM
import com.kunle.aisle9b.ui.theme.BaseOrange
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultTopAppBar(
    screenHeader: String,
    drawerState: DrawerState,
) {
    val scope = rememberCoroutineScope()
    CenterAlignedTopAppBar(
        navigationIcon = {
            Icon(
                imageVector = Icons.Filled.Menu,
                contentDescription = "Open navigation",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .clickable {
                        scope.launch { drawerState.open() }
                    }
            )
        },
        title = {
            Text(
                text = screenHeader,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchableTopAppBar(
    screenHeader: String,
    drawerState: DrawerState,
    topBarOption: (TopBarOptions) -> Unit
) {
    val scope = rememberCoroutineScope()
    CenterAlignedTopAppBar(
        navigationIcon = {
            Icon(
                imageVector = Icons.Filled.Menu,
                contentDescription = "Open navigation",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .clickable {
                        scope.launch { drawerState.open() }
                    }
            )
        },
        title = {
            Text(
                text = screenHeader,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        },
        actions = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search Bar",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .clickable {
                        topBarOption(TopBarOptions.Searchbar)
                    }
            )
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackTopAppBar(
    navController: NavController,
    screenHeader: String
) {
    CenterAlignedTopAppBar(
        navigationIcon = {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Go back",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .clickable {
                        navController.popBackStack()
                    }
            )
        },
        title = {
            Text(
                text = screenHeader,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    shoppingVM: ShoppingVM,
    topBarOption: (TopBarOptions) -> Unit
) {
    var searchWord by remember { mutableStateOf("") }

    if (shoppingVM.searchSource.value == GroceryScreens.MealScreen.name) {
        val mealList = shoppingVM.mealList.collectAsState().value
        val filteredList = remember { mutableStateOf(mealList) }
        shoppingVM.filteredMeals.value = filteredList.value

        CenterAlignedTopAppBar(
            navigationIcon = {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back Arrow",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .clickable {
                            topBarOption(TopBarOptions.SearchEnabled)
                        }
                )
            },
            title = {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(.8f),
                    value = searchWord,
                    onValueChange = {
                        searchWord = it
                        filteredList.value = mealList.filter { list ->
                            list.name.lowercase().contains(searchWord.lowercase())
                        }
                    },
                    placeholder = { Text(text = "Search in lists") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Icon",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    trailingIcon = {
                        if (searchWord.isNotEmpty()) {
                            IconButton(onClick = {
                                searchWord = ""
                                filteredList.value = mealList.filter { list ->
                                    list.name.lowercase().contains(searchWord.lowercase())
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Cancel,
                                    contentDescription = "Cancel button",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RectangleShape,
                    colors = TextFieldDefaults.textFieldColors(cursorColor = BaseOrange)
                )
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )
    } else {
        val customGroceryList = shoppingVM.customLists.collectAsState().value
        val filteredList = remember { mutableStateOf(customGroceryList) }
        shoppingVM.filteredCustomLists.value = filteredList.value

        CenterAlignedTopAppBar(
            navigationIcon = {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back Arrow",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .clickable {
                            topBarOption(TopBarOptions.SearchEnabled)
                        }
                )
            },
            title = {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(.8f),
                    value = searchWord,
                    onValueChange = {
                        searchWord = it
                        filteredList.value = customGroceryList.filter { list ->
                            list.name.lowercase().contains(searchWord.lowercase())
                        }
                    },
                    placeholder = { Text(text = "Search in lists") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Icon",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    trailingIcon = {
                        if (searchWord.isNotEmpty()) {
                            IconButton(onClick = {
                                searchWord = ""
                                filteredList.value = customGroceryList.filter { list ->
                                    list.name.lowercase().contains(searchWord.lowercase())
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Cancel,
                                    contentDescription = "Cancel button",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RectangleShape,
                    colors = TextFieldDefaults.textFieldColors(cursorColor = BaseOrange)
                )
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )
    }
}