package com.kunle.aisle9b.templates

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kunle.aisle9b.models.Meal
import com.kunle.aisle9b.ui.theme.BaseOrange
import com.kunle.aisle9b.ui.theme.OrangeTintLight

@Composable
fun MealItem9(
    meal: Meal,
    deleteEnabled: Boolean,
    onCheckBoxChecked: (Int) -> Unit,
    onEditClicked: (String?) -> Unit
) {
    //move to ViewModel later
    val isChecked = remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .padding(4.dp)
            .height(60.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(corner = CornerSize(6.dp)),
        elevation = 6.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Checkbox(
                checked = isChecked.value,
                onCheckedChange = {
                    isChecked.value = true
                    onCheckBoxChecked(3) //change later
                },
                enabled = deleteEnabled,
                colors = CheckboxDefaults.colors(
                    checkedColor = OrangeTintLight,
                    uncheckedColor = OrangeTintLight,
                    checkmarkColor = Color.Black
                ),
                modifier = Modifier.size(36.dp)
            )
            Text(text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = BaseOrange, fontSize = 22.sp)) {
                    append(meal.name)
                }
            }, modifier = Modifier.weight(1f))
            Icon(
                modifier = Modifier.size(36.dp),
                imageVector = Icons.Filled.Edit,
                contentDescription = "Edit Icon",
            )
            Spacer(modifier = Modifier.width(5.dp))


        }
    }
}


//@Composable
//@Preview
//fun MealPreview() {
//    MealItem9(Meal(name = "Delicious Example"))
//}