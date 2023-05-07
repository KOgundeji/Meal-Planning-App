package com.kunle.aisle9b.models.apiModels.trendingRecipeModels

data class Result(
    val category: String,
    val item: Item,
    val items: List<ItemX>,
    val min_items: Int,
    val name: String,
    val type: String
)