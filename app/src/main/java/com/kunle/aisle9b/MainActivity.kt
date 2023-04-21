package com.kunle.aisle9b

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.navigation.compose.rememberNavController
import com.kunle.aisle9b.models.AppSetting
import com.kunle.aisle9b.navigation.Aisle9Navigation
import com.kunle.aisle9b.navigation.BottomNavigationBar9
import com.kunle.aisle9b.navigation.navDrawerList
import com.kunle.aisle9b.screens.ShoppingVM
import com.kunle.aisle9b.ui.theme.Aisle9bTheme
import com.kunle.aisle9b.ui.theme.BaseOrange
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val shoppingVM: ShoppingVM by viewModels()
            ShoppingApp(shoppingVM = shoppingVM)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingApp(shoppingVM: ShoppingVM) {
    val navController = rememberNavController()
    var selectedItem by remember { mutableStateOf(navDrawerList[0]) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    shoppingVM.groceryBadgeCount.value =
        shoppingVM.groceryList.collectAsState().value.size
    shoppingVM.screenList[2].name =
        "Meals (${shoppingVM.mealList.collectAsState().value.size})"

//    addFakeToDatabase(list = sampleFoodData,viewModel = shoppingViewModel)
    shoppingVM.darkModeSetting.value = shoppingVM.settingsList.collectAsState()
        .value.firstOrNull() {
            it.settingsName == AppSetting.DarkMode.name
        }?.value ?: isSystemInDarkTheme()

    Aisle9bTheme(darkTheme = shoppingVM.darkModeSetting.value) {
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
                    when (shoppingVM.topBar.value) {
                        TopBarOptions.SearchEnabled -> {
                            SearchableTopAppBar(
                                drawerState = drawerState,
                                screenHeader = shoppingVM.screenHeader.value,
                                topBarOption = { shoppingVM.topBar.value = it }
                            )
                        }
                        TopBarOptions.Searchbar -> {
                            SearchBar(
                                shoppingVM = shoppingVM,
                                topBarOption = { shoppingVM.topBar.value = it }
                            )
                        }
                        TopBarOptions.BackButton -> {
                            BackTopAppBar(
                                navController = navController,
                                screenHeader = shoppingVM.screenHeader.value
                            )
                        }
                        TopBarOptions.Default -> {
                            DefaultTopAppBar(
                                drawerState = drawerState,
                                screenHeader = shoppingVM.screenHeader.value
                            )
                        }
                    }
                }, bottomBar = {
                    BottomNavigationBar9(
                        items = shoppingVM.screenList,
                        navController = navController,
                        shoppingVM = shoppingVM,
                        badgeCount = shoppingVM.groceryBadgeCount.value,
                        onItemClick = {
                            navController.navigate(it.route)
                        })
                }) {
                Aisle9Navigation(
                    navController = navController,
                    shoppingVM = shoppingVM,
                    modifier = Modifier.padding(it)
                )
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
fun SearchBar(
    shoppingVM: ShoppingVM,
    topBarOption: (TopBarOptions) -> Unit
) {
    var searchWord by remember { mutableStateOf("") }

    val customLists = shoppingVM.premadeLists.collectAsState().value
    val filteredCustomLists = remember { mutableStateOf(customLists) }
    shoppingVM.filteredList.value = filteredCustomLists.value

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
                    filteredCustomLists.value = customLists.filter { list ->
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


enum class TopBarOptions {
    Default,
    SearchEnabled,
    Searchbar,
    BackButton;
}
