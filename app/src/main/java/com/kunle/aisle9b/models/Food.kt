package com.kunle.aisle9b.models

import java.util.*

data class Food(
    val foodId: UUID = UUID.randomUUID(),
    val name: String,
    var quantity: String,
    var category: String,
    var isInGroceryList: Boolean
) {}