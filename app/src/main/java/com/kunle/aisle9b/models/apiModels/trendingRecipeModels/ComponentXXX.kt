package com.kunle.aisle9b.models.apiModels.trendingRecipeModels

data class ComponentXXX(
    val extra_comment: String,
    val id: Int,
    val ingredient: Ingredient,
    val measurements: List<MeasurementXXX>,
    val position: Int,
    val raw_text: String
)