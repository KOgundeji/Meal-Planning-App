package com.kunle.aisle9b.repositories

import com.kunle.aisle9b.models.Instruction
import com.kunle.aisle9b.repositories.general.GeneralRepository

class FakeShoppingRepository: GeneralRepository {
    override suspend fun upsertFood(ingredient: Ingredient) {

    }

    override suspend fun upsertInstruction(instruction: Instruction) {

    }


}