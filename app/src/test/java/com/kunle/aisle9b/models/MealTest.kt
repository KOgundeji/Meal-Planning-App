package com.kunle.aisle9b.models

import android.net.Uri
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class MealTest {

    @Test
    fun createBlankTest() {
        val meal = Meal.createBlank()
        assertThat(meal).isEqualTo(
            Meal(
                mealId = 0L,
                name = "",
                servingSize = "?",
                mealPic =  Uri.EMPTY,
                notes = "",
                apiID = -1,
                visible = false
            )
        )
    }

    @Test
    fun createClassUsingSecondaryConstructor() {
        val meal = Meal("Chili","8 servings",618)
        assertThat(meal).isEqualTo(
            Meal(
                mealId = 0L,
                name = "Chili",
                servingSize = "8 servings",
                mealPic =  Uri.EMPTY,
                notes = "",
                apiID = 618,
                visible = true
            )
        )
    }

}