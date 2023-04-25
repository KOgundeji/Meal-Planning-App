package com.kunle.aisle9b

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.core.*
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.kunle.aisle9b.models.AppSetting
import com.kunle.aisle9b.navigation.Aisle9Navigation
import com.kunle.aisle9b.navigation.BottomNavigationBar9
import com.kunle.aisle9b.navigation.navDrawerList
import com.kunle.aisle9b.screens.ShoppingVM
import com.kunle.aisle9b.ui.theme.Aisle9bTheme
import com.kunle.aisle9b.util.*
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
    var fab by remember { mutableStateOf(shoppingVM.multiFloatingState.value)}

    val scope = rememberCoroutineScope()
    val settings = shoppingVM.settingsList.collectAsState().value

    shoppingVM.groceryBadgeCount.value =
        shoppingVM.groceryList.collectAsState().value.size
    shoppingVM.screenList[2].name =
        "Meals (${shoppingVM.mealList.collectAsState().value.size})"

//    addFakeToDatabase(list = sampleFoodData,viewModel = shoppingViewModel)
    shoppingVM.darkModeSetting.value = settings.firstOrNull() {
        it.settingsName == AppSetting.DarkMode.name
    }?.value ?: isSystemInDarkTheme()

    shoppingVM.categoriesOn.value = settings.firstOrNull {
        it.settingsName == AppSetting.Categories.name
    }?.value ?: false

    shoppingVM.keepScreenOn.value = settings.firstOrNull() {
        it.settingsName == AppSetting.ScreenPermOn.name
    }?.value ?: false

    Aisle9bTheme(darkTheme = shoppingVM.darkModeSetting.value) {
        ModalNavigationDrawer(
            gesturesEnabled = true,
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(
                    modifier = Modifier
                        .width(300.dp)
                        .fillMaxHeight(),
                    drawerContainerColor = MaterialTheme.colorScheme.background,
                    drawerContentColor = MaterialTheme.colorScheme.onBackground
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
                                colors = NavigationDrawerItemDefaults.colors(
                                    selectedContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                    unselectedContainerColor = Color.Transparent,
                                    selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                    unselectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                    selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                    unselectedTextColor = MaterialTheme.colorScheme.onSurface
                                ),
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
                        badgeCount = shoppingVM.groceryBadgeCount.value,
                        onItemClick = {
                            navController.navigate(it.route)
                        })
                },
                floatingActionButton = {
                    if (shoppingVM.fabEnabled.value) {
                        FAB(
                            navController = navController,
                            shoppingVM = shoppingVM,
                            multiFloatingState = fab,
                            onMultiFabStateChange = { fab = it }
                        )
                    }
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


enum class TopBarOptions {
    Default,
    SearchEnabled,
    Searchbar,
    BackButton;
}

enum class MultiFloatingState {
    Expanded,
    Collapsed;
}
