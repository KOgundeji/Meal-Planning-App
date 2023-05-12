package com.kunle.aisle9b.models.apiModels.queryModels

data class RawJSON(
    val number: Int,
    val offset: Int,
    val results: List<Result>,
    val totalResults: Int
)