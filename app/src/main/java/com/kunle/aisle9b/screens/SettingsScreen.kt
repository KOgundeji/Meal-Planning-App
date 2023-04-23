package com.kunle.aisle9b.screens

import android.view.View
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.kunle.aisle9b.TopBarOptions
import com.kunle.aisle9b.models.AppSettings
import com.kunle.aisle9b.models.AppSetting
import com.kunle.aisle9b.navigation.GroceryScreens
import com.kunle.aisle9b.ui.theme.BaseOrange

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    shoppingVM: ShoppingVM
) {
    shoppingVM.screenHeader.value = GroceryScreens.headerTitle(GroceryScreens.SettingsScreen)
    shoppingVM.topBar.value = TopBarOptions.BackButton

    var darkMode by remember { mutableStateOf(shoppingVM.darkModeSetting.value) }
    var screenPermOn by remember { mutableStateOf(shoppingVM.keepScreenOn.value) }
    var categories by remember { mutableStateOf(shoppingVM.categoriesOn.value) }

    if (screenPermOn) { KeepScreenOn() }

    Column(modifier = modifier) {
        Text(
            text = "Display options",
            fontWeight = FontWeight.Bold,
            color = BaseOrange,
            fontSize = 16.sp,
            modifier = Modifier.padding(start = 10.dp, bottom = 5.dp, top = 15.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(.95f),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Dark Mode",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 15.dp)
            )
            Switch(
                checked = darkMode,
                colors = SwitchDefaults.colors(),
                onCheckedChange = {
                    darkMode = !darkMode
                    shoppingVM.insertSettings(
                        AppSettings(
                            settingsName = AppSetting.DarkMode.name,
                            value = darkMode
                        )
                    )
                }
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(.95f),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Keep Screen on",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 15.dp)
            )
            Switch(
                checked = screenPermOn,
                colors = SwitchDefaults.colors(),
                onCheckedChange = {
                    screenPermOn = !screenPermOn
                    shoppingVM.insertSettings(
                        AppSettings(
                            settingsName = AppSetting.ScreenPermOn.name,
                            value = screenPermOn
                        )
                    )
                }
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(.95f),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Add Categories to Grocery List",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 15.dp),
                maxLines = 2
            )
            Switch(
                checked = categories,
                colors = SwitchDefaults.colors(),
                onCheckedChange = {
                    categories = !categories
                    shoppingVM.insertSettings(
                        AppSettings(
                            settingsName = AppSetting.Categories.name,
                            value = categories
                        )
                    )
                }
            )
        }

        Column() {
            Box(modifier = Modifier.weight(1f, true))
            Text(
                text = "About",
                fontWeight = FontWeight.Bold,
                color = BaseOrange,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 10.dp, bottom = 4.dp)
            )
            Text(
                text = "Created by Kunle Ogundeji",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 15.dp, bottom = 1.dp)
            )
            Text(
                text = "Sun King Studios",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 15.dp, bottom = 1.dp)
            )
            Text(
                text = "Version 2.0.0",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 15.dp, bottom = 10.dp)
            )
        }
    }
}

@Composable
fun KeepScreenOn() = AndroidView({ View(it).apply { keepScreenOn = true } })

@Preview
@Composable
fun SettingsPreview() {
//    SettingsScreen()
}


