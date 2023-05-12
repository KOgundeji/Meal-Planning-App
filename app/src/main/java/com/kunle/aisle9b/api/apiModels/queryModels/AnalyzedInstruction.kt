package com.kunle.aisle9b.models.apiModels.queryModels

data class AnalyzedInstruction(
    val name: String,
    val steps: List<Step>
)