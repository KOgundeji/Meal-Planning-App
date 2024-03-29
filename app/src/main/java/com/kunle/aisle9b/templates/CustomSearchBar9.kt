package com.kunle.aisle9b.templates

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSearchBar9(
    modifier: Modifier = Modifier,
    text: String,
    onValueChange: (String) -> Unit,
    label: String,
    trailingIcon: @Composable () -> Unit = {},
    textStyle: TextStyle = TextStyle()
) {
    val interactionSource = remember { MutableInteractionSource() }

    Card(shape = RoundedCornerShape(3.dp)) {
        BasicTextField(
            modifier = modifier
                .height(45.dp)
                .fillMaxWidth(0.85f),
            value = text,
            textStyle = textStyle,
            singleLine = true,
            keyboardOptions = KeyboardOptions(),
            onValueChange = { onValueChange(it) },
            interactionSource = interactionSource
        ) {
            TextFieldDefaults.DecorationBox(
                value = text,
                innerTextField = it,
                enabled = true,
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                        modifier = Modifier.size(24.dp)
                    )
                },
                trailingIcon = trailingIcon,
                label = { Text(text = label, fontSize = 14.sp) },
                visualTransformation = VisualTransformation.None,
                interactionSource = interactionSource,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    focusedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer ,
                    cursorColor = MaterialTheme.colorScheme.onSecondaryContainer
                ),
                contentPadding = PaddingValues(horizontal = 15.dp),
            )
        }
    }
}