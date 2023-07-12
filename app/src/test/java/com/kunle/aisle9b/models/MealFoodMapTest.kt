package com.kunle.aisle9b.models

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class MealFoodMapTest {

    @Test
    fun createMealFoodMap() {
        val mealId = 42L
        val foodId = 5L
        val mealMap = MealFoodMap(mealId = mealId, foodId = foodId)

        assertThat(mealMap).isEqualTo(
            MealFoodMap(
                mealId = 42L,
                foodId = 5L
            )
        )
    }
}