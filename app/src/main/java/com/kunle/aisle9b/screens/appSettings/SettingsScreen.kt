package com.kunle.aisle9b.screens.appSettings

import android.view.View
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kunle.aisle9b.TopBarOptions
import com.kunle.aisle9b.navigation.GroceryScreens
import com.kunle.aisle9b.screens.GeneralVM

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    generalVM: GeneralVM = hiltViewModel()
) {
    generalVM.setTopBarOption(TopBarOptions.Default)
    generalVM.setClickSource(GroceryScreens.SettingsScreen)

    val screenPermOn = generalVM.screenOnSetting
    val categoriesOn = generalVM.categoriesSetting
    val darkMode = generalVM.darkModeSetting ?: generalVM.setDarkModeSetting(isSystemInDarkTheme())

    if (screenPermOn) {
        KeepScreenOn()
    }

    Column(modifier = modifier) {
        Text(
            text = "Display options",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 16.sp,
            modifier = Modifier.padding(start = 10.dp, bottom = 5.dp, top = 15.dp)
        )
        SettingsRow(text = "Dark Mode", checked = darkMode) {
            generalVM.setDarkModeSetting(it)
        }
        SettingsRow(text = "Keep Screen on", checked = screenPermOn) {
            generalVM.setScreenPermOnSetting(it)
        }
        SettingsRow(text = "Add Categories to Grocery List", checked = categoriesOn) {
            generalVM.setCategoriesOnSetting(it)
        }

        Column {
            Box(modifier = Modifier.weight(1f, true))
            Text(
                text = "About",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
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
                text = "Sun Forged Studios",
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
private fun SettingsRow(
    text: String,
    checked: Boolean,
    onChecked: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(.95f),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 16.sp,
            modifier = Modifier.padding(start = 15.dp)
        )
        Switch(
            checked = checked,
            colors = SwitchDefaults.colors(),
            onCheckedChange = {
                onChecked(it)
            }
        )
    }
}

@Composable
fun KeepScreenOn() = AndroidView({ View(it).apply { keepScreenOn = true } })

@Composable
@Preview
fun ScreenPreview() {
    SettingsRow(text = "Dark Mode", checked = true) { }
}


