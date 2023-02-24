package com.kunle.aisle9b.util

import com.kunle.aisle9b.models.Food
import com.kunle.aisle9b.models.FoodCategory

fun separateFoodIntoCategories(groceryList: List<Food>): MutableList<FoodCategory> {

    val categories = mutableListOf<FoodCategory>()
    val tempCategoryMap = mutableMapOf<String, MutableList<Food>>()

    for (i in groceryList.indices) {
        val item: Food = groceryList[i]
        val category: String = item.category

        if (tempCategoryMap.containsKey(category)) {
            tempCategoryMap[category]!!.add(item)
        } else {
            val itemList = mutableListOf(item)
            tempCategoryMap[category] = itemList
        }
    }

    tempCategoryMap.keys.forEach { categories.add(FoodCategory(it, tempCategoryMap[it]!!)) }

    return categories
}