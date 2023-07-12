package com.kunle.aisle9b.models

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ListFoodMapTest {

    @Test
    fun createListFoodMap() {
        val listId = 42L
        val foodId = 5L
        val listMap = ListFoodMap(listId = listId, foodId = foodId)

        assertThat(listMap).isEqualTo(
            ListFoodMap(
                listId = 42L,
                foodId = 5L
            )
        )
    }
}