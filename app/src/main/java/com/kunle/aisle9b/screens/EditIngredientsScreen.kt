package com.kunle.aisle9b.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kunle.aisle9b.models.Food
import com.kunle.aisle9b.navigation.GroceryScreens
import com.kunle.aisle9b.ui.theme.BaseOrange
import java.util.*

@Composable
fun EditIngredientsScreen(
    modifier: Modifier,
    ingredientIDString: String?,
    navController: NavController,
    shoppingViewModel: ShoppingViewModel
) {
    val backStackEntry = navController.currentBackStackEntryAsState()

    lateinit var convertedFoodID: UUID
    var name = ""
    var quantity = ""
    var category = ""
    var isInGroceryList = false

    if (ingredientIDString != null) {
        Log.d("Screen", "EditIngredientsScreen: converted = $ingredientIDString")
        convertedFoodID = UUID.fromString(ingredientIDString)
        val ingredient = shoppingViewModel.foodList.collectAsState().value.first { food ->
            Log.d("Screen", "EditIngredientsScreen: foodId = ${food.foodId}, converted = $convertedFoodID")
            food.foodId == convertedFoodID
        }
        name = ingredient.name
        quantity = ingredient.quantity
        category = ingredient.category
        isInGroceryList = ingredient.isInGroceryList
    }

    val ingredientName = remember {
        mutableStateOf(name)
    }
    val ingredientQuantity = remember {
        mutableStateOf(quantity)
    }
    val ingredientCategory = remember {
        mutableStateOf(category)
    }

    Surface(
        modifier = modifier
            .height(350.dp)
            .width(250.dp),
        color = Color.Gray
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            TextField(
                value = ingredientName.value,
                onValueChange = {
                    if (it.all { char ->
                            char.isLetter() || char.isWhitespace()
                        }) ingredientName.value = it
                },
                placeholder = { Text(text = "Ingredient") },
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
            )
            TextField(
                value = ingredientQuantity.value,
                onValueChange = {
                    if (it.all { char ->
                            char.isLetter() || char.isWhitespace()
                        }) ingredientQuantity.value = it
                },
                placeholder = { Text(text = "How much/How many?") },
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
            )
            TextField(
                value = ingredientCategory.value,
                onValueChange = {
                    if (it.all { char ->
                            char.isLetter() || char.isWhitespace()
                        }) ingredientCategory.value = it
                },
                placeholder = { Text(text = "Select Category") },
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.AddCircle,
                    contentDescription = "Confirm button",
                    modifier = Modifier
                        .size(48.dp)
                        .clickable {
                            if (backStackEntry.value?.destination?.route == GroceryScreens.AddMealScreen.name) {
                                shoppingViewModel.tempFoodList.add(
                                    Food(
                                        name = ingredientName.value.trim(),
                                        quantity = ingredientQuantity.value.trim(),
                                        category = ingredientCategory.value.trim(),
                                        isInGroceryList = false
                                    )
                                )
                                navController.popBackStack()
                                TODO("Add mutable list for lazy column")
                            } else {
                                shoppingViewModel.updateFood(
                                    Food(
                                        foodId = convertedFoodID,
                                        name = ingredientName.value.trim(),
                                        quantity = ingredientQuantity.value.trim(),
                                        category = ingredientCategory.value.trim(),
                                        isInGroceryList = isInGroceryList
                                    )
                                )
                                navController.popBackStack()
                            }
                        },
                    tint = BaseOrange
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = "Confirm",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 4.dp)
                )
                Spacer(modifier = Modifier.width(35.dp))
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Go back button",
                    modifier = Modifier
                        .size(48.dp)
                        .clickable { navController.popBackStack() },
                    tint = BaseOrange
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = "Go Back",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }
    }
}