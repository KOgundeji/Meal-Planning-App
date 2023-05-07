package com.kunle.aisle9b.models.apiModels.trendingRecipeModels

data class NutritionX(
    val calories: Int,
    val carbohydrates: Int,
    val fat: Int,
    val fiber: Int,
    val protein: Int,
    val sugar: Int,
    val updated_at: String
)