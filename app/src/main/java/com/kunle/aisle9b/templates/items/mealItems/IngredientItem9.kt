package com.kunle.aisle9b.templates.items.mealItems

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kunle.aisle9b.models.Food
import com.kunle.aisle9b.templates.dialogs.OptionPopup
import com.kunle.aisle9b.templates.dialogs.Options
import com.kunle.aisle9b.templates.dialogs.mealDialogs.EditIngredientDialog9
import com.kunle.aisle9b.templates.dialogs.mealDialogs.EditInstructionsDialog9

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IngredientItem9(
    ingredient: Food,
    deleteIngredient: () -> Unit,
    updatedIngredient: (Food) -> Unit,
) {
    var edit by remember { mutableStateOf(false) }
    var popup by remember { mutableStateOf(false) }

    if (edit) {
        EditIngredientDialog9(
            oldFood = ingredient,
            closeDialog = { edit = false },
            deleteIngredient = {
                deleteIngredient()
                edit = false
            },
            updateIngredient = {
                updatedIngredient(it)
                edit = false
            })
    }

    if (popup) {
        OptionPopup(
            optionType = Options.MoveUpDown,
            closeDialog = { popup = false }) {
            //empty until I implement a positioning system for ingredients
            popup = false
        }
    }

    Card(
        modifier = Modifier
            .padding(horizontal = 6.dp)
            .fillMaxWidth()
            .combinedClickable(onLongClick = {
                popup = true
            }) { edit = true },
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = RoundedCornerShape(corner = CornerSize(6.dp))
    ) {
        Text(text = "${ingredient.name} (${ingredient.quantity})", fontSize = 14.sp)
    }
}