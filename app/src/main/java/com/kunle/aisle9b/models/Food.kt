package com.kunle.aisle9b.models

data class Food(
    val foodId: Long,
    val name: String,
    var quantity: String,
    var category: String,
    var isInGroceryList: Boolean
) {}