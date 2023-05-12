package com.kunle.aisle9b.models.apiModels.instructionModels

import com.kunle.aisle9b.api.apiModels.instructionModels.Step

data class InstructionsItem(
    val name: String,
    val steps: List<Step>
)