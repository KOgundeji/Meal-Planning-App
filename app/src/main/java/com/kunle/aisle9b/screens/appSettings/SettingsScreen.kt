package com.kunle.aisle9b.screens.appSettings

import android.view.View
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.kunle.aisle9b.screens.GeneralVM

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    generalVM: GeneralVM
) {
    val screenPermOn = generalVM.keepScreenOn.collectAsState().value
    val categoriesOn = generalVM.categoriesSetting
    val darkMode = generalVM.darkModeSetting ?: generalVM.setDarkModeSetting(isSystemInDarkTheme())

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
                text = "Sun King Studios",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 15.dp, bottom = 1.dp)
            )
            Text(
                text = "Version 2.2.0",
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
@Preview
fun ScreenPreview() {
    SettingsRow(text = "Dark Mode", checked = true) { }
}


