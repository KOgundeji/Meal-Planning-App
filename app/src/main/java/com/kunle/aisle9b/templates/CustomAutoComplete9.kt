package com.kunle.aisle9b.templates

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties

@Composable
fun CustomAutoComplete9(
    modifier: Modifier = Modifier,
    value: String,
    setValue: (String) -> Unit,
    originalList: List<String>,
    label: String
) {
    var suggestions by remember { mutableStateOf(emptyList<String>()) }
    val expanded = remember { derivedStateOf { suggestions.isNotEmpty()} }

    Box(modifier = modifier) {
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
            shape = RoundedCornerShape(3.dp)
        ) {
            CustomTextField9(
                modifier = Modifier
                    .height(45.dp)
                    .fillMaxWidth(),
                text = value,
                onValueChange = {
                    suggestions =
                        originalList.filter { text ->
                            text.contains(it, ignoreCase = true) && text != it
                        }.take(3)
                    setValue(it)
                },
                textStyle = TextStyle(fontSize = 14.sp),
                label = label,
            )
        }
        if (expanded.value && value.isNotEmpty()) {
            DropdownMenu(
                modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer),
                expanded = expanded.value,
                onDismissRequest = { },
                properties = PopupProperties(
                    focusable = false,
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true
                )
            ) {
                suggestions.forEach { autoCompleteListItem ->
                    DropdownMenuItem(
                        modifier = Modifier
                            .height(30.dp)
                            .fillMaxWidth(),
                        text = {
                            Text(
                                text = autoCompleteListItem,
                                style = TextStyle(
                                    fontStyle = FontStyle.Italic,
                                    fontSize = 14.sp
                                )
                            )
                        },
                        onClick = {
                            setValue(autoCompleteListItem)
                            suggestions = emptyList()
                        })
                }
            }
        }
    }
}