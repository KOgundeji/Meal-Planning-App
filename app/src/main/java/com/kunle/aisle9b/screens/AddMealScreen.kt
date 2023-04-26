package com.kunle.aisle9b.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kunle.aisle9b.TopBarOptions
import com.kunle.aisle9b.models.Food
import com.kunle.aisle9b.models.Meal
import com.kunle.aisle9b.models.MealFoodMap
import com.kunle.aisle9b.navigation.BottomNavigationBar9
import com.kunle.aisle9b.navigation.GroceryScreens
import com.kunle.aisle9b.templates.EditFoodDialog9
import com.kunle.aisle9b.templates.ListItem9
import com.kunle.aisle9b.ui.theme.BaseOrange
import com.kunle.aisle9b.util.BackTopAppBar
import com.kunle.aisle9b.util.DefaultTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMealScreen(
    modifier: Modifier = Modifier,
    shoppingVM: ShoppingVM,
    navController: NavController
) {
    val meal = Meal(name = "")
    var mealName by remember { mutableStateOf("") }
    var showEditFoodDialog by remember { mutableStateOf(false) }
//    val tempIngredientList = remember { mutableStateListOf<Food>() }



    Scaffold(
        topBar = {
            BackTopAppBar(
                screenHeader = GroceryScreens.headerTitle(GroceryScreens.AddMealsScreen)
            ) {
                navController.popBackStack()
            }
        }, bottomBar = {
            BottomNavigationBar9(
                items = shoppingVM.screenList,
                navController = navController,
                badgeCount = shoppingVM.groceryBadgeCount.value,
                onItemClick = {
                    navController.navigate(it.route)
                })
        }) {

        if (showEditFoodDialog) {
            EditFoodDialog9(
                food = Food(name = "", quantity = "", isInGroceryList = false),
                setShowSelfDialog = {bool -> showEditFoodDialog = bool },
                setFood = {food -> shoppingVM.tempIngredientList.add(food) },
            )
        }

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(it)
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(16.dp),
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            TextField(
                value = mealName,
                onValueChange = { mealName = it },
                placeholder = { Text(text = "Type meal name") },
                colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { showEditFoodDialog = true },
                    modifier = Modifier.width(75.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                ) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        imageVector = Icons.Filled.AddCircle,
                        contentDescription = "Add button"
                    )
                }
                Button(
                    onClick = { },
                    modifier = Modifier.width(75.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                ) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete button"
                    )
                }
                Button(
                    onClick = {
                        shoppingVM.tempIngredientList.forEach {
                            shoppingVM.insertFood(it)
                            shoppingVM.insertMeal(
                                meal = Meal(mealId = meal.mealId, name = mealName)
                            )
                            shoppingVM.insertPair(
                                MealFoodMap(mealId = meal.mealId, foodId = it.foodId)
                            )
                        }
                        shoppingVM.tempIngredientList.clear()
                        navController.navigate(GroceryScreens.MealScreen.name)
                    },
                    modifier = Modifier.width(75.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                ) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        imageVector = Icons.Filled.Save,
                        contentDescription = "Save button"
                    )
                }
            }
            LazyColumn(
                modifier = Modifier.padding(horizontal = 15.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(items = shoppingVM.tempIngredientList) {
                    ListItem9(
                        modifier = Modifier.padding(vertical = 3.dp),
                        food = it,
                        shoppingVM = shoppingVM,
                        checkBoxShown = false,
                        onEditClickNewFood = true
                    )
                }
            }
        }
    }
}