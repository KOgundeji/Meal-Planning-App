package com.kunle.aisle9b.util

import android.util.Log
import com.kunle.aisle9b.models.Food
import com.kunle.aisle9b.screens.ShoppingViewModel

fun filterForReconciliation(
    lists: List<List<Food>>,
    shoppingViewModel: ShoppingViewModel
): Map<String, List<Food>> {
    val foodMap: MutableMap<String, MutableList<Food>> = mutableMapOf()
    val filteredFoodMap: MutableMap<String, MutableList<Food>> = mutableMapOf()

    for (list in lists) {
        for (food in list) {
            if (foodMap.containsKey(food.name)) {
                foodMap[food.name]!!.add(food)
            } else {
                foodMap[food.name] = mutableListOf(food)
            }
        }
    }

    foodMap.forEach { (key, value) ->
        Log.d("Test", "key: $key, value: $value")
        if (value.size == 1) {
            val newFood = Food(
                foodId = value[0].foodId,
                name = key,
                quantity = value[0].quantity,
                category = value[0].category,
                isInGroceryList = true
            )
            shoppingViewModel.updateFood(newFood)
        } else {
            filteredFoodMap[key] = value
        }
    }
    return filteredFoodMap
}