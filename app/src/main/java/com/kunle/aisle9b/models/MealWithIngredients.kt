package com.kunle.aisle9b.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation

@Entity
data class MealWithIngredients(
    @Embedded
    val meal: Meal,
    @Relation(
        parentColumn = "mealId",
        entityColumn = "foodId",
        associateBy = Junction(MealFoodMap::class)
    ) val foods: List<Food>
)
