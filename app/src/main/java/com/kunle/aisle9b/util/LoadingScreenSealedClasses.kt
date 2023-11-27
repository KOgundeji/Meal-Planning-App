package com.kunle.aisle9b.util

import com.kunle.aisle9b.models.GroceryList
import com.kunle.aisle9b.models.Meal

sealed class MealGateState {
    object Loading : MealGateState()
    object Neutral : MealGateState()
    class Error(private val exception: Exception) : MealGateState() {
        fun getMessage(): String? = exception.localizedMessage
    }

    data class Success(val foodId: Long) : MealGateState()
}


sealed class MealResponse {
    object Loading : MealResponse()
    class Error(private val exception: Exception) : MealResponse() {
        fun getMessage(): String? = exception.localizedMessage
    }

    data class Success(val meal: Meal) : MealResponse()
}

sealed class IngredientResponse {
    object Loading : IngredientResponse()
    object Neutral : IngredientResponse()
    class Error(private val exception: Exception) : IngredientResponse() {
        fun getMessage(): String? = exception.localizedMessage
    }

    data class Success(val foodId: Long) : IngredientResponse()
}

sealed class CustomListGateState {
    object Loading : CustomListGateState()
    class Error(private val exception: Exception) : CustomListGateState() {
        fun getMessage(): String? = exception.localizedMessage
    }

    data class Success(var groceryList: GroceryList) : CustomListGateState()
}