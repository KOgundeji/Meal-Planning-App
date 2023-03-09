package com.kunle.aisle9b.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kunle.aisle9b.models.Settings
import com.kunle.aisle9b.models.SettingsEnum
import com.kunle.aisle9b.ui.theme.BaseOrange

@Composable
fun SettingsScreen(shoppingViewModel: ShoppingViewModel, modifier: Modifier = Modifier) {
    val darkMode = remember { mutableStateOf(false) }
    val screenPermOn = remember { mutableStateOf(false) }
    val categories = remember { mutableStateOf(true) }

    Surface(modifier = modifier) {
        Column {
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "General",
                fontWeight = FontWeight.Bold,
                color = BaseOrange,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 10.dp, bottom = 5.dp)
            )
            Row {
                Text(
                    text = "Dark Mode",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .padding(start = 15.dp, top = 5.dp, bottom = 5.dp)
                        .weight(.8f)
                )
                Checkbox(
                    checked = darkMode.value,
                    onCheckedChange = {
                        darkMode.value = it
                        shoppingViewModel.updateSettings(
                            Settings(
                                settingsName = SettingsEnum.DarkMode.name,
                                value = it
                            )
                        )
                    },
                    enabled = true,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .weight(.2f)
                        .size(36.dp),
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color.Black,
                        uncheckedColor = Color.Black,
                        checkmarkColor = Color.Black
                    )
                )
            }
            Row {
                Text(
                    text = "Keep Screen on",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .padding(start = 15.dp, top = 5.dp, bottom = 5.dp)
                        .weight(.8f)
                )
                Checkbox(
                    checked = screenPermOn.value,
                    onCheckedChange = {
                        screenPermOn.value = it
                        shoppingViewModel.updateSettings(
                            Settings(
                                settingsName = SettingsEnum.ScreenPermOn.name,
                                value = it
                            )
                        )
                    },
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .weight(.2f)
                        .size(36.dp),
                    enabled = true,
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color.Black,
                        uncheckedColor = Color.Black,
                        checkmarkColor = Color.Black
                    )
                )
            }
            Row {
                Text(
                    text = "Remove Categories from Grocery List",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .padding(start = 15.dp, top = 5.dp, bottom = 5.dp)
                        .weight(.8f)
                )
                Checkbox(
                    checked = categories.value,
                    onCheckedChange = {
                        categories.value = it
                        shoppingViewModel.updateSettings(
                            Settings(
                                settingsName = SettingsEnum.Categories.name,
                                value = it
                            )
                        )
                    },
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .weight(.2f)
                        .size(36.dp),
                    enabled = true,
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color.Black,
                        uncheckedColor = Color.Black,
                        checkmarkColor = Color.Black
                    )
                )
            }

            Column() {
                Box(modifier = Modifier.weight(1f, true))
                Text(
                    text = "About",
                    fontWeight = FontWeight.Bold,
                    color = BaseOrange,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(start = 10.dp)
                )
                Text(
                    text = "Created by Kunle Ogundeji",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 15.dp, top = 4.dp, bottom = 1.dp)
                )
                Text(
                    text = "Sun King Studios",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 15.dp, bottom = 1.dp)
                )
                Text(
                    text = "Version 1.1.0",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 15.dp, bottom = 5.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun SettingsPreview() {
//    SettingsScreen()
}


