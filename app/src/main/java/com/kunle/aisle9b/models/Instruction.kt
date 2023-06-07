package com.kunle.aisle9b.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "instructions")
data class Instruction(
    @PrimaryKey
    val instructionId: UUID = UUID.randomUUID(),
    @ColumnInfo
    var step: String,
    @ColumnInfo
    val mealId: UUID,
    @ColumnInfo
    var position: Int
) {
    companion object {
        fun createBlank(mealId: UUID): Instruction {
            return Instruction(step = "", mealId = mealId, position = 0)
        }
    }
}