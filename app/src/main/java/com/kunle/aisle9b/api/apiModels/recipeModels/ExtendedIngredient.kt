package com.kunle.aisle9b.api.apiModels.recipeModels

import com.kunle.aisle9b.models.apiModels.recipeModels.Measures

data class ExtendedIngredient(
    val aisle: String,
    val amount: Double,
    val consistency: String,
    val id: Int,
    val image: String,
    val measures: Measures,
    val meta: List<String>,
    val name: String,
    val original: String,
    val originalName: String,
    val unit: String
)