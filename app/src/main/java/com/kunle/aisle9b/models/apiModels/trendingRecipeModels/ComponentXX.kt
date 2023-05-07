package com.kunle.aisle9b.models.apiModels.trendingRecipeModels

data class ComponentXX(
    val extra_comment: String,
    val id: Int,
    val ingredient: Ingredient,
    val measurements: List<MeasurementXX>,
    val position: Int,
    val raw_text: String
)