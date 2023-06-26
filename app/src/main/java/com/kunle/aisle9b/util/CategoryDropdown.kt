package com.kunle.aisle9b.util

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import com.kunle.aisle9b.models.Food.Companion.categories

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropDownMenu(category: String, newCategory: (String) -> Unit) {

    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(category) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = selectedText,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor(),
                label = { Text(text = "Select Category") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.primaryContainer),
                shape = RectangleShape
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                categories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(text = category) },
                        onClick = {
                            selectedText = category
                            newCategory(selectedText)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

