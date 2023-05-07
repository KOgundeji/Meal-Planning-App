package com.kunle.aisle9b.models.apiModels.trendingRecipeModels

data class Instruction(
    val appliance: String,
    val display_text: String,
    val end_time: Int,
    val id: Int,
    val position: Int,
    val start_time: Int,
    val temperature: Int
)