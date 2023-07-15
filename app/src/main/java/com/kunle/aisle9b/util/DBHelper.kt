package com.kunle.aisle9b.util

import com.kunle.aisle9b.data.InstructionDao
import com.kunle.aisle9b.models.Instruction
import javax.inject.Inject

class DBHelper @Inject constructor(private val instructionDao: InstructionDao) {

    suspend fun reorganizeInstructions(
        updatedInstruction: Instruction,
        oldInstructionList: List<Instruction>
    ) {
        val oldPosition =
            oldInstructionList.find { it.instructionId == updatedInstruction.instructionId }!!.position
        val newPosition = updatedInstruction.position

        if (newPosition > 0) {
            instructionDao.upsertInstruction(updatedInstruction)

            when {
                newPosition < oldPosition -> {
                    oldInstructionList.forEach {
                        if (it.position in newPosition until oldPosition) {
                            instructionDao.upsertInstruction(
                                Instruction(
                                    instructionId = it.instructionId,
                                    step = it.step,
                                    mealId = it.mealId,
                                    position = it.position + 1
                                )
                            )
                        }
                    }
                }
                newPosition > oldPosition -> {
                    oldInstructionList.forEach {
                        if (it.position in (oldPosition + 1)..newPosition) {
                            instructionDao.upsertInstruction(
                                Instruction(
                                    instructionId = it.instructionId,
                                    step = it.step,
                                    mealId = it.mealId,
                                    position = it.position - 1
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}