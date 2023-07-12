package com.kunle.aisle9b.models

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class FoodClassTest {

    @Test
    fun createBlankTest() {
        val food = Food.createBlank()
        assertThat(food).isEqualTo(
            Food(
                foodId = 0L,
                name = "",
                quantity = "",
                category = "Uncategorized"
            )
        )
    }

    @Test
    fun foodToGrocery_returnsExactCopyOfGrocery() {
        val food = Food(foodId = 99L, name = "Almond", quantity = "12 oz", category = "Produce")
        val grocery = food.foodToGrocery()
        assertThat(grocery).isEqualTo(Grocery(
            groceryId = 0L,
            name = "Almond",
            quantity = "12 oz",
            category = "Produce"
        ))
    }
}