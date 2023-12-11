package com.kunle.aisle9b.util

import androidx.compose.animation.core.EaseOutQuint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AddFAB(
    onAddClick: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FloatingActionButton(
            onClick = { onAddClick() },
            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 10.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            shape = CircleShape
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                modifier = Modifier.size(40.dp),
                contentDescription = null
            )
        }
    }
}

@Composable
fun SaveFAB(onSaveClick: () -> Unit) {
    var saveClicked by remember { mutableStateOf(false) }

    val fadeInOut by animateFloatAsState(
        targetValue = if (saveClicked) 0f else 1f,
        animationSpec = tween(
            durationMillis = 2000,
            easing = EaseOutQuint
        ),
        finishedListener = { saveClicked = false }
    )

    FloatingActionButton(
        modifier = Modifier.alpha(1 - fadeInOut),
        onClick = {},
        containerColor = Color.Green,
        shape = CircleShape
    ) {
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = null
        )
    }
    FloatingActionButton(
        modifier = Modifier.alpha(fadeInOut),
        onClick = {
            onSaveClick()
            saveClicked = true
        },
        containerColor = MaterialTheme.colorScheme.primary,
        shape = CircleShape
    ) {
        Icon(
            imageVector = Icons.Filled.Save,
            contentDescription = "Save button"
        )
    }
}

