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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import com.kunle.aisle9b.screens.groceries.GroceryVM

@Composable
fun CustomAutoComplete9(
    viewModel: GroceryVM,
    value: String,
    setValue: (String) -> Unit,
    label: String
) {
    val suggestions = viewModel.suggestions.collectAsState().value

    Box {
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
                    viewModel.updateAutoComplete(it)
                    setValue(it)
                },
                textStyle = TextStyle(fontSize = 14.sp),
                label = label,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            )
        }
        if (value.isNotEmpty()) {
            DropdownMenu(
                modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer),
                expanded = true,
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
                            viewModel.updateAutoComplete("")
                        })
                }
            }
        }
    }
}