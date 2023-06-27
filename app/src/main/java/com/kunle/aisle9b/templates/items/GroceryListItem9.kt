package com.kunle.aisle9b.templates.items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kunle.aisle9b.models.Food
import com.kunle.aisle9b.models.Grocery
import com.kunle.aisle9b.repositories.BasicRepositoryFunctions
import com.kunle.aisle9b.templates.dialogs.EditGroceryDialog9
import kotlinx.coroutines.launch

@Composable
fun GroceryListItem9(
    modifier: Modifier = Modifier,
    grocery: Grocery,
    viewModel: BasicRepositoryFunctions,
    onEditGrocery: (Grocery) -> Unit
) {
    val scope = rememberCoroutineScope()
    var isChecked by remember { mutableStateOf(false) }
    var showEditGroceryDialog by remember { mutableStateOf(false) }

    if (showEditGroceryDialog) {
        EditGroceryDialog9(
            oldGrocery = grocery,
            closeDialog = { showEditGroceryDialog = false },
            setGrocery = { updatedGrocery ->
                onEditGrocery(updatedGrocery)
            })
    }
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp),
        shape = RoundedCornerShape(corner = CornerSize(6.dp)),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = modifier.padding(3.dp)
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = {
                    isChecked = true
                    scope.launch { viewModel.deleteGroceryByName(grocery.name) }
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.secondaryContainer,
                    uncheckedColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    checkmarkColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.size(36.dp)
            )

            Text(text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append(grocery.name)
                }
                withStyle(
                    style = SpanStyle(
                        color = Color.Gray,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append(" (${grocery.quantity})")
                }
            }, modifier = Modifier.weight(1f))

            Icon(
                modifier = Modifier
                    .size(30.dp)
                    .clickable {
                        showEditGroceryDialog = true
                    },
                imageVector = Icons.Filled.Edit,
                contentDescription = "Edit Icon",
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.width(5.dp))

        }
    }
}
