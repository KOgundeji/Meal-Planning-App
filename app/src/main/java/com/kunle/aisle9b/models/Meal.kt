package com.kunle.aisle9b.models

import java.util.*

data class Meal(
    val mealId: UUID = UUID.randomUUID(),
    val name: String) {}
