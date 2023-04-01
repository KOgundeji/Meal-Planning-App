package com.kunle.aisle9b.data

import androidx.compose.runtime.Composable
import com.kunle.aisle9b.models.Food
import com.kunle.aisle9b.models.Meal
import com.kunle.aisle9b.screens.ShoppingViewModel
import com.kunle.aisle9b.screens.fakeMealList

var oranges = Food(name = "Oranges", quantity = "4 bunches", category = "Fruit", isInGroceryList = true)
var pineapple = Food(name = "Pineapple", quantity = "3", category = "Fruit", isInGroceryList = true)
var eggs = Food(name = "Eggs", quantity = "1 dozen", category = "Uncategorized", isInGroceryList = true)
var cheese = Food(name = "Cheese", quantity = "50 grams", category = "Uncategorized", isInGroceryList = false)
var pasta = Food(name = "Pasta", quantity = "2 boxes", category = "Condiments", isInGroceryList = true)
var tissues = Food(name = "Tissues", quantity = "1 box", category = "Uncategorized", isInGroceryList = true)
var potatoes = Food(name = "Potatoes", quantity = "3", category = "Uncategorized", isInGroceryList = true)

val sampleFoodData = listOf(oranges, pineapple,eggs,cheese,pasta,tissues, potatoes)

fun addFakeToDatabase(list: List<Food>, viewModel: ShoppingViewModel) {
    sampleFoodData.forEach {
        viewModel.insertFood(it)
    }
    fakeMealList.forEach {
        viewModel.insertMeal(it)
    }
}

var fruit_salad = Meal(name = "Fruit Salad")
var omelette = Meal(name = "Omelette")
var irish_pasta = Meal(name = "Irish Pasta")
