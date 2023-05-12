package com.kunle.aisle9b.models.apiModels.queryModels

data class Equipment(
    val id: Int,
    val image: String,
    val localizedName: String,
    val name: String,
    val temperature: Temperature
)