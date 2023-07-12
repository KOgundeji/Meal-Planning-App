package com.kunle.aisle9b.models

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class GroceryListTest {

    @Test
    fun createBlankTest() {
        val groceryList = GroceryList.createBlank()
        assertThat(groceryList).isEqualTo(
            GroceryList(
                listId = 0L,
                listName = "",
                visible = false
            )
        )
    }
}