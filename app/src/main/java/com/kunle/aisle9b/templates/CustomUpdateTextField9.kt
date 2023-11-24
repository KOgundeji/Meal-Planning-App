package com.kunle.aisle9b.templates

import androidx.compose.animation.core.EaseOutQuint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomUpdateTextField9(
    modifier: Modifier = Modifier,
    text: String,
    onValueChange: (String) -> Unit,
    onSaveClick: (String) -> Unit,
    label: String,
    singleLine: Boolean = true,
    readOnly: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions()
) {
    var saveClicked by remember { mutableStateOf(false) }

    val fadeInOut by animateFloatAsState(
        targetValue = if (saveClicked) 0f else 1f,
        animationSpec = tween(
            durationMillis = 1000,
            easing = EaseOutQuint
        ),
        finishedListener = { saveClicked = false }
    )

    CustomTextField9(
        modifier = modifier
            .height(45.dp)
            .fillMaxWidth(),
        text = text,
        onValueChange = { onValueChange(it) },
        label = label,
        singleLine = singleLine,
        readOnly = readOnly,
        textStyle = TextStyle(fontSize = 16.sp),
        keyboardOptions = keyboardOptions,
        trailingIcon = {
            Button(
                shape = RectangleShape,
                contentPadding = PaddingValues(7.dp),
                modifier = Modifier
                    .padding(5.dp)
                    .alpha(1 - fadeInOut),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                onClick = {}) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
            }
            Button(
                shape = RectangleShape,
                contentPadding = PaddingValues(7.dp),
                modifier = Modifier
                    .padding(5.dp)
                    .alpha(fadeInOut),
                onClick = {
                    if (text.isNotBlank()) {
                        onSaveClick(text)
                        saveClicked = true
                    }
                }) {
                Text(
                    text = "Save",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    )
}