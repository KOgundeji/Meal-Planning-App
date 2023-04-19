package com.kunle.aisle9b

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.kunle.aisle9b.data.addFakeToDatabase
import com.kunle.aisle9b.data.sampleFoodData
import com.kunle.aisle9b.models.AppSetting
import com.kunle.aisle9b.navigation.Aisle9Navigation
import com.kunle.aisle9b.navigation.BottomNavigationBar9
import com.kunle.aisle9b.navigation.GroceryScreens
import com.kunle.aisle9b.navigation.navDrawerList
import com.kunle.aisle9b.screens.ShoppingViewModel
import com.kunle.aisle9b.ui.theme.Aisle9bTheme
import com.kunle.aisle9b.ui.theme.BaseOrange
import com.kunle.aisle9b.util.AdditionalScreenOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val shoppingViewModel: ShoppingViewModel by viewModels()
            ShoppingApp(shoppingViewModel = shoppingViewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingApp(shoppingViewModel: ShoppingViewModel) {
    val navController = rememberNavController()
    var screenHeader by remember { mutableStateOf("") }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var selectedItem by remember { mutableStateOf(navDrawerList[0]) }
    var topBarOptions by remember { mutableStateOf(TopBarOptions.Default.name) }
    val scope = rememberCoroutineScope()

    shoppingViewModel.groceryBadgeCount.value =
        shoppingViewModel.groceryList.collectAsState().value.size
    shoppingViewModel.screenList[2].name =
        "Meals (${shoppingViewModel.mealList.collectAsState().value.size})"

//    addFakeToDatabase(list = sampleFoodData,viewModel = shoppingViewModel)
    shoppingViewModel.darkModeSetting.value = shoppingViewModel.settingsList.collectAsState()
        .value.firstOrNull() {
            it.settingsName == AppSetting.DarkMode.name
        }?.value ?: isSystemInDarkTheme()

    Aisle9bTheme(darkTheme = shoppingViewModel.darkModeSetting.value) {
        ModalNavigationDrawer(
            gesturesEnabled = true,
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(
                    modifier = Modifier
                        .width(300.dp)
                        .fillMaxHeight()
                ) {
                    Column {
                        Text(
                            text = "Aisle 9",
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            modifier = Modifier.padding(20.dp)
                        )
                        Divider(modifier = Modifier.fillMaxWidth())
                        navDrawerList.forEach {
                            NavigationDrawerItem(
                                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                                icon = { Icon(imageVector = it.icon, contentDescription = null) },
                                label = {
                                    Text(
                                        text = it.name,
                                        fontWeight = FontWeight.Bold
                                    )
                                },
                                shape = RoundedCornerShape(bottomEnd = 40.dp, topEnd = 40.dp),
                                selected = it == selectedItem,
                                onClick = {
                                    scope.launch { drawerState.close() }
                                    navController.navigate(it.route)
                                    selectedItem = it
                                })
                        }
                    }
                }
            }) {
            Scaffold(
                topBar = {
                    when (topBarOptions) {
                        TopBarOptions.SearchEnabled.name -> {
                            SearchableTopAppBar(
                                screenHeader = screenHeader,
                                drawerState = drawerState,
                                topBarOption = { topBarOptions = it }
                            )
                        }
                        TopBarOptions.Searchbar.name -> {
                            SearchBar(
                                screenHeader = screenHeader,
                                shoppingViewModel = shoppingViewModel,
                                topBarOption = { topBarOptions = it }
                            )
                        }
                        else -> {
                            DefaultTopAppBar(
                                screenHeader = screenHeader,
                                drawerState = drawerState
                            )
                        }
                    }
                }, bottomBar = {
                    BottomNavigationBar9(
                        items = shoppingViewModel.screenList,
                        navController = navController,
                        shoppingViewModel = shoppingViewModel,
                        badgeCount = shoppingViewModel.groceryBadgeCount.value,
                        onItemClick = {
                            navController.navigate(it.route)
                        })
                }) {
                Aisle9Navigation(
                    navController = navController,
                    shoppingViewModel = shoppingViewModel,
                    modifier = Modifier.padding(it),
                    topBarOption = { top -> topBarOptions = top },
                ) { headline ->
                    screenHeader = headline
                }
            }
        }

    }
}

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
    topBarOption: (String) -> Unit
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
                        topBarOption(TopBarOptions.Searchbar.name)
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
fun SearchBar(
    screenHeader: String,
    shoppingViewModel: ShoppingViewModel,
    topBarOption: (String) -> Unit
) {
    val scope = rememberCoroutineScope()
    var searchWord by remember { mutableStateOf("") }

    val customLists = shoppingViewModel.premadeLists.collectAsState().value
    val filteredCustomLists = remember { mutableStateOf(customLists) }
    shoppingViewModel.filteredList.value = filteredCustomLists.value

    CenterAlignedTopAppBar(
        navigationIcon = {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Back Arrow",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .clickable {
                        topBarOption(TopBarOptions.SearchEnabled.name)
                    }
            )
        },
        title = {
            TextField(
                modifier = Modifier.fillMaxWidth(.8f),
                value = searchWord,
                onValueChange = {
                    searchWord = it
                    filteredCustomLists.value = customLists.filter { list ->
                        list.name.lowercase().contains(searchWord.lowercase())
                    }
                },
                placeholder = { Text(text = "Search in lists") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                        modifier = Modifier
                            .padding(15.dp)
                            .size(24.dp)
                    )
                },
                trailingIcon = {
                    if (searchWord.isNotEmpty()) {
                        IconButton(onClick = {
                            searchWord = ""
                            filteredCustomLists.value = customLists.filter { list ->
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
                shape = RoundedCornerShape(30.dp),
                colors = TextFieldDefaults.textFieldColors(cursorColor = BaseOrange)
            )
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

enum class TopBarOptions {
    Default,
    SearchEnabled,
    Searchbar;
}
