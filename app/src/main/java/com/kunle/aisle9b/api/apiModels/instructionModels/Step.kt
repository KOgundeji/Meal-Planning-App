package com.kunle.aisle9b.api.apiModels.instructionModels

import com.kunle.aisle9b.models.apiModels.instructionModels.Equipment
import com.kunle.aisle9b.models.apiModels.instructionModels.Ingredient
import com.kunle.aisle9b.models.apiModels.instructionModels.Length

data class Step(
    val equipment: List<Equipment>,
    val ingredients: List<Ingredient>,
    val length: Length,
    val number: Int,
    val step: String
)