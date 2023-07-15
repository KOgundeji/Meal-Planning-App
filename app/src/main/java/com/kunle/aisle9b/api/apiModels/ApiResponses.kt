package com.kunle.aisle9b.api.apiModels

import com.kunle.aisle9b.api.apiModels.recipeModels.Recipe
import com.kunle.aisle9b.models.apiModels.instructionModels.Instructions
import com.kunle.aisle9b.models.apiModels.queryModels.Result

sealed class ApiResponseSearch {
    object Loading: ApiResponseSearch()
    object Neutral: ApiResponseSearch()
    class Error(private val exception: Exception) : ApiResponseSearch() {
        fun getMessage(): String? = exception.localizedMessage
    }
    data class Success(val recipes: List<Result>) : ApiResponseSearch()
}

sealed class ApiResponseRecipe {
    object Loading: ApiResponseRecipe()
    object Neutral: ApiResponseRecipe()
    class Error(private val exception: Exception) : ApiResponseRecipe() {
        fun getMessage(): String? = exception.localizedMessage
    }
    data class Success(val recipe: Recipe) : ApiResponseRecipe()
}

sealed class ApiResponseInstructions {
    object Loading: ApiResponseInstructions()
    class Error(private val exception: Exception) : ApiResponseInstructions() {
        fun getMessage(): String? = exception.localizedMessage
    }
    data class Success(val instructions: Instructions) : ApiResponseInstructions()
}