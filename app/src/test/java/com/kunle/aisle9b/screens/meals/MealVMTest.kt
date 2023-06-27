package com.kunle.aisle9b.screens.meals

import com.kunle.aisle9b.models.Instruction
import com.kunle.aisle9b.repositories.FakeShoppingRepository
import com.kunle.aisle9b.repositories.meals.FakeMealRepository
import dagger.hilt.android.testing.HiltAndroidTest

import org.junit.Before
import org.junit.Test

@HiltAndroidTest
class MealVMTest {

    private lateinit var viewModel: MealVM
    private lateinit var testList: List<Instruction>

    @Before
    fun setUp() {
        viewModel = MealVM(repository = FakeMealRepository())
        testList = listOf(
            Instruction(1, "Do the third thing", 4, 3),
            Instruction(2, "Do the fourth thing", 4, 4),
            Instruction(3, "Do the fifth thing", 4, 5),
            Instruction(4, "Do the first thing", 4, 1),
            Instruction(5, "Do the second thing", 4, 2)
        )

    }

    @Test
    fun reorganizeTempInstructions_listGiven_returnOrganizedList() {

    }
}