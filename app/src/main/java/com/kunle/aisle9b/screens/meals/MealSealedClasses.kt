package com.kunle.aisle9b.screens.meals

import com.kunle.aisle9b.models.Meal

sealed class MealResponse {
    object Loading : MealResponse()
    object Neutral : MealResponse()
    class Error(private val exception: Exception) : MealResponse() {
        fun getMessage(): String? = exception.localizedMessage
    }

    data class Success(val meal: Meal) : MealResponse()
}