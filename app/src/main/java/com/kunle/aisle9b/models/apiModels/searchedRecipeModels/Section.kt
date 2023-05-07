package com.kunle.aisle9b.models.apiModels.searchedRecipeModels

data class Section(
    val components: List<Component>,
    val name: String,
    val position: Int
)