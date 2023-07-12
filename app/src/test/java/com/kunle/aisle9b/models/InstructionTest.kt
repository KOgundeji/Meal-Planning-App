package com.kunle.aisle9b.models

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class InstructionTest {

    @Test
    fun createBlankTest() {
        val mealId = 101L
        val instruction = Instruction.createBlank(mealId = mealId)
        assertThat(instruction).isEqualTo(
            Instruction(
                instructionId = 0L,
                step = "",
                mealId = 101L,
                position = 0
            )
        )
    }
}