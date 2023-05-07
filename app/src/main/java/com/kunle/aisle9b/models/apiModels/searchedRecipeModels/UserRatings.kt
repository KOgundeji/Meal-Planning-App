package com.kunle.aisle9b.models.apiModels.searchedRecipeModels

data class UserRatings(
    val count_negative: Int,
    val count_positive: Int,
    val score: Double
)