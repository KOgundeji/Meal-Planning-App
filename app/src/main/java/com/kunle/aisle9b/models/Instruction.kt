package com.kunle.aisle9b.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "instructions")
data class Instruction(
    @PrimaryKey(autoGenerate = true)
    val instructionId: Long = 0,
    val step: String,
    val mealId: Long,
    val position: Int
) {
    companion object {
        fun createBlank(mealId: Long): Instruction {
            return Instruction(step = "", mealId = mealId, position = 0)
        }
    }
}