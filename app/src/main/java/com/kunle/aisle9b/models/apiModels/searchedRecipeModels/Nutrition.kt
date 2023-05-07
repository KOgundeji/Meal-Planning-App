package com.kunle.aisle9b.models.apiModels.searchedRecipeModels

data class Nutrition(
    val calories: Int,
    val carbohydrates: Int,
    val fat: Int,
    val fiber: Int,
    val protein: Int,
    val sugar: Int,
    val updated_at: String
)