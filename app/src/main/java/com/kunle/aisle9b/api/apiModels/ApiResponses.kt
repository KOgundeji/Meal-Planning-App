package com.kunle.aisle9b.api.apiModels

import com.kunle.aisle9b.models.apiModels.instructionModels.Instructions
import com.kunle.aisle9b.models.apiModels.queryModels.Result
import com.kunle.aisle9b.api.apiModels.recipeModels.Recipe

sealed class ApiResponseList {
    object Loading: ApiResponseList()
    object Neutral: ApiResponseList()
    class Error(private val exception: Exception) : ApiResponseList() {
        fun getMessage(): String? = exception.localizedMessage
    }
    data class Success(val recipes: List<Result>) : ApiResponseList()
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