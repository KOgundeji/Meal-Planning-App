package com.kunle.aisle9b.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import com.kunle.aisle9b.models.Food
import com.kunle.aisle9b.models.Instruction
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

class InstructionDaoShould {

    private lateinit var sutDB: ShoppingRoomDB

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        sutDB = Room.inMemoryDatabaseBuilder(
            context, ShoppingRoomDB::class.java
        ).build()
    }

    @After
    @Throws(IOException::class)
    fun closeDB() {
        sutDB.close()
    }

    @Test
    fun insertInstruction_InstructionDao() {
        runTest {
            val instruction = Instruction(2, "Say we need to talk", 1, 1)
            sutDB.instructionDao().insertInstruction(instruction)

            val instructionList = sutDB.instructionDao().getAllInstructions().first()

            val testInstruction = instructionList[0]

            assertThat(instructionList.size).isEqualTo(1)
            assertThat(testInstruction.step).isEqualTo("Say we need to talk")
        }
    }

    @Test
    fun upsertInstruction_InstructionDao_insertNewInstance() {
        runTest {
            val instruction = Instruction(2, "Say we need to talk", 1, 1)
            sutDB.instructionDao().insertInstruction(instruction)

            val instructionList = sutDB.instructionDao().getAllInstructions().first()

            assertThat(instructionList.size).isEqualTo(1)

            val instructionNew = Instruction(3, "Step three", 1, 3)
            sutDB.instructionDao().upsertInstruction(instructionNew)

            val instructionListAfterNewInsert = sutDB.instructionDao().getAllInstructions().first()

            assertThat(instructionListAfterNewInsert.size).isEqualTo(2)
        }
    }

    @Test
    fun upsertInstruction_InstructionDao_insertSameInstance() {
        runTest {
            val instruction = Instruction(2, "Say we need to talk", 1, 1)
            sutDB.instructionDao().insertInstruction(instruction)

            val instructionList = sutDB.instructionDao().getAllInstructions().first()

            assertThat(instructionList.size).isEqualTo(1)

            sutDB.instructionDao().upsertInstruction(instruction)

            val instructionListAfterNewInsert = sutDB.instructionDao().getAllInstructions().first()

            assertThat(instructionListAfterNewInsert.size).isEqualTo(1)
        }
    }

    @Test
    fun upsertInstruction_InstructionDao_updateInstance() {
        runTest {
            val instruction = Instruction(2, "Say we need to talk", 1, 1)
            sutDB.instructionDao().insertInstruction(instruction)

            val instructionList = sutDB.instructionDao().getAllInstructions().first()

            assertThat(instructionList.size).isEqualTo(1)

            val instructionUpdated = Instruction(2, "Step two", 1, 2)
            sutDB.instructionDao().upsertInstruction(instructionUpdated)

            val instructionListAfterUpdatedInsert =
                sutDB.instructionDao().getAllInstructions().first()

            assertThat(instructionListAfterUpdatedInsert.size).isEqualTo(1)
            assertThat(instructionListAfterUpdatedInsert[0].step).isEqualTo("Step two")
        }
    }

    @Test
    fun deleteInstruction_InstructionDao() {
        runTest {
            val instruction = Instruction(2, "Say we need to talk", 1, 1)
            sutDB.instructionDao().insertInstruction(instruction)

            val instructionList = sutDB.instructionDao().getAllInstructions().first()

            assertThat(instructionList.size).isEqualTo(1)

            sutDB.instructionDao().deleteInstruction(instruction)

            val instructionListAfterDeletion = sutDB.instructionDao().getAllInstructions().first()

            assertThat(instructionListAfterDeletion.size).isEqualTo(0)
        }
    }

    @Test
    fun getAllInstructions_InstructionsDao() {
        runTest {
            val listOfInstructions = listOf(
                Instruction(step = "Step one", mealId = 1, position = 1),
                Instruction(step = "Step two", mealId = 1, position = 2),
                Instruction(step = "Step three", mealId = 1, position = 3),
                Instruction(step = "Step four", mealId = 1, position = 4)
            )

            listOfInstructions.forEach {
                sutDB.instructionDao().insertInstruction(it)
            }

            val instructionList = sutDB.instructionDao().getAllInstructions().first()

            assertThat(instructionList.size).isEqualTo(4)
        }
    }
}