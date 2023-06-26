package com.kunle.aisle9b.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.util.UUID

@Entity(primaryKeys = ["mealId", "foodId"])
class MealFoodMap(
    @ColumnInfo(index = true)
    val mealId: Long,
    val foodId: Long) {
}