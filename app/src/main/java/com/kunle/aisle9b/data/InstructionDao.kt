package com.kunle.aisle9b.data

import androidx.room.*
import com.kunle.aisle9b.models.Instruction
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface InstructionDao {
    @Upsert
    suspend fun upsertInstruction(instruction: Instruction)

    @Delete
    suspend fun deleteInstruction(instruction: Instruction)

    @Query("SELECT * FROM instructions")
    fun getAllInstructions(): Flow<List<Instruction>>

}