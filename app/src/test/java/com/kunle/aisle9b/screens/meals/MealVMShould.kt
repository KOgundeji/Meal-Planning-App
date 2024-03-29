package com.kunle.aisle9b.screens.meals

import com.kunle.aisle9b.MainDispatcherRule
import com.kunle.aisle9b.repositories.meals.MealRepositoryImpl
import kotlinx.coroutines.flow.flow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.google.common.truth.Truth.assertThat
import com.kunle.aisle9b.models.*
import io.mockk.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.random.Random

class MealVMShould {

    @get:Rule
    val coroutineRule = MainDispatcherRule()

    private val allMeals: List<Meal> = listOf(mockk())
    private val allMWI: List<MealWithIngredients> = listOf(mockk())
    private val allInstructions: List<Instruction> = listOf(mockk())
    private val mRepositoryImpl: MealRepositoryImpl = mockk()
    private lateinit var sutMealViewModel: MealVM

    private val oldInstructionList = listOf(
        Instruction(instructionId = 11, step = "first thing", mealId = 1, position = 1),
        Instruction(instructionId = 12, step = "second thing", mealId = 1, position = 2),
        Instruction(instructionId = 33, step = "third thing", mealId = 1, position = 3),
        Instruction(instructionId = 44, step = "fourth thing", mealId = 1, position = 4),
        Instruction(instructionId = 55, step = "fifth thing", mealId = 1, position = 5)
    )

    @Before
    fun setUp() {
        every { mRepositoryImpl.getAllMeals() } returns flow { emit(allMeals) }
        every { mRepositoryImpl.getAllMealsWithIngredients() } returns flow { emit(allMWI) }
        every { mRepositoryImpl.getAllInstructions() } returns flow { emit(allInstructions) }
        sutMealViewModel = MealVM(mRepositoryImpl)
    }

    @Test
    fun getAllMeals_callsCorrectRepositoryMethod() {
        sutMealViewModel.visibleMealList
        verify { mRepositoryImpl.getAllMeals() }
    }

    @Test
    fun getAllMeals_returnsCorrectList() {
        runTest {
            val actualList = sutMealViewModel.visibleMealList.first()
            assertThat(actualList).isEqualTo(allMeals)
        }
    }

    @Test
    fun getAllMealsWithIngredients_callsCorrectRepositoryMethod() {
        sutMealViewModel.mealsWithIngredients
        verify { mRepositoryImpl.getAllMealsWithIngredients() }
    }

    @Test
    fun getAllMealsWithIngredients_returnsCorrectList() {
        runTest {
            val actualList = sutMealViewModel.mealsWithIngredients.first()
            assertThat(actualList).isEqualTo(allMWI)
        }
    }

    @Test
    fun getAllInstructions_callsCorrectRepositoryMethod() {
        sutMealViewModel.instructions
        verify { mRepositoryImpl.getAllInstructions() }
    }

    @Test
    fun getAllInstructions_returnsCorrectList() {
        runTest {
            val actualList = sutMealViewModel.instructions.first()
            assertThat(actualList).isEqualTo(allInstructions)
        }
    }

    @Test
    fun insertMeal_callsCorrectRepositoryMethod() {
        val meal: Meal = mockk()
        val returnId: Long = Random(42L).nextLong()
        coEvery { mRepositoryImpl.insertMeal(any()) } returns returnId

        sutMealViewModel.insertMeal(meal)
        coVerify { mRepositoryImpl.insertMeal(any()) }
    }

    @Test
    fun insertMeal_returnsCorrectList() {
        val meal: Meal = mockk()
        val returnId: Long = Random(42L).nextLong()
        coEvery { mRepositoryImpl.insertMeal(any()) } returns returnId

        val actualReturnedId = sutMealViewModel.insertMeal(meal)
        assertThat(actualReturnedId).isEqualTo(returnId)
    }

    @Test
    fun upsertMeal_callsCorrectRepositoryMethod() {
        val meal: Meal = mockk()
        coEvery { mRepositoryImpl.upsertMeal(any()) } returns Unit

        sutMealViewModel.upsertMeal(meal)
        coVerify { mRepositoryImpl.upsertMeal(any()) }
    }

    @Test
    fun deleteMeal_callsCorrectRepositoryMethod() {
        val meal: Meal = mockk()
        coEvery { mRepositoryImpl.deleteMeal(any()) } returns Unit

        sutMealViewModel.deleteMeal(meal)
        coVerify { mRepositoryImpl.deleteMeal(any()) }
    }

    @Test
    fun updateName_callsCorrectRepositoryMethod() {
        val mealNameUpdate: MealNameUpdate = mockk()
        coEvery { mRepositoryImpl.updateName(any()) } returns Unit

        sutMealViewModel.updateName(mealNameUpdate)
        coVerify { mRepositoryImpl.updateName(any()) }
    }

    @Test
    fun updatePic_callsCorrectRepositoryMethod() {
        val mealPicUpdate: MealPicUpdate = mockk()
        coEvery { mRepositoryImpl.updatePic(any()) } returns Unit

        sutMealViewModel.updatePic(mealPicUpdate)
        coVerify { mRepositoryImpl.updatePic(any()) }
    }

    @Test
    fun updateServingSize_callsCorrectRepositoryMethod() {
        val mealServingSizeUpdate: MealServingSizeUpdate = mockk()
        coEvery { mRepositoryImpl.updateServingSize(any()) } returns Unit

        sutMealViewModel.updateServingSize(mealServingSizeUpdate)
        coVerify { mRepositoryImpl.updateServingSize(any()) }
    }

    @Test
    fun updateNotes_callsCorrectRepositoryMethod() {
        val mealNotesUpdate: MealNotesUpdate = mockk()
        coEvery { mRepositoryImpl.updateNotes(any()) } returns Unit

        sutMealViewModel.updateNotes(mealNotesUpdate)
        coVerify { mRepositoryImpl.updateNotes(any()) }
    }

    @Test
    fun upsertInstruction_callsCorrectRepositoryMethod() {
        val instruction: Instruction = mockk()
        coEvery { mRepositoryImpl.upsertInstruction(any()) } returns Unit

        sutMealViewModel.upsertInstruction(instruction)
        coVerify { mRepositoryImpl.upsertInstruction(any()) }
    }

    @Test
    fun deleteInstruction_callsCorrectRepositoryMethod() {
        val instruction: Instruction = mockk()
        coEvery { mRepositoryImpl.deleteInstruction(any()) } returns Unit

        sutMealViewModel.deleteInstruction(instruction)
        coVerify { mRepositoryImpl.deleteInstruction(any()) }
    }

    @Test
    fun insertPair_callsCorrectRepositoryMethod() {
        val pair: MealFoodMap = mockk()
        coEvery { mRepositoryImpl.insertPair(any()) } returns Unit

        sutMealViewModel.insertPair(pair)
        coVerify { mRepositoryImpl.insertPair(any()) }
    }

    @Test
    fun deletePair_callsCorrectRepositoryMethod() {
        val pair: MealFoodMap = mockk()
        coEvery { mRepositoryImpl.deletePair(any()) } returns Unit

        sutMealViewModel.deletePair(pair)
        coVerify { mRepositoryImpl.deletePair(any()) }
    }

    @Test
    fun deleteSpecificMealWithIngredients_callsCorrectRepositoryMethod() {
        val mwiId: Long = Random(42L).nextLong()
        coEvery { mRepositoryImpl.deleteSpecificMealWithIngredients(any()) } returns Unit

        sutMealViewModel.deleteSpecificMealWithIngredients(mwiId)
        coVerify { mRepositoryImpl.deleteSpecificMealWithIngredients(any()) }
    }

    @Test
    fun insertFood_callsCorrectRepositoryMethod() {
        runTest {
            val food: Food = mockk()
            val returnId = Random(42L).nextLong()
            coEvery { mRepositoryImpl.insertFood(any()) } returns returnId

            sutMealViewModel.insertFood(food)

            coVerify { mRepositoryImpl.insertFood(any()) }
        }
    }

    @Test
    fun insertFood_returnsFoodId() {
        runTest {
            val food: Food = mockk()
            val returnId = Random(42L).nextLong()
            coEvery { mRepositoryImpl.insertFood(any()) } returns returnId

            val actual = sutMealViewModel.insertFood(food)

            assertThat(actual).isEqualTo(returnId)
        }
    }

    @Test
    fun insertGrocery_callsCorrectRepositoryMethod() {
        runTest {
            val grocery: Grocery = mockk()
            coEvery { mRepositoryImpl.insertGrocery(any()) } returns Unit

            sutMealViewModel.insertGrocery(grocery)

            coVerify { mRepositoryImpl.insertGrocery(any()) }
        }
    }

    @Test
    fun upsertFood_callsCorrectRepositoryMethod() {
        runTest {
            val food: Food = mockk()
            coEvery { mRepositoryImpl.upsertFood(any()) } returns Unit

            sutMealViewModel.upsertFood(food)

            coVerify { mRepositoryImpl.upsertFood(any()) }
        }
    }

    @Test
    fun deleteFood_callsCorrectRepositoryMethod() {
        runTest {
            val food: Food = mockk()
            coEvery { mRepositoryImpl.deleteFood(any()) } returns Unit

            sutMealViewModel.deleteFood(food)
            coVerify { mRepositoryImpl.deleteFood(any()) }
        }
    }

    @Test
    fun deleteGrocery_callsCorrectRepositoryMethod() {
        runTest {
            val grocery: Grocery = mockk()
            coEvery { mRepositoryImpl.deleteGrocery(any()) } returns Unit

            sutMealViewModel.deleteGrocery(grocery)
            coVerify { mRepositoryImpl.deleteGrocery(any()) }
        }
    }

    @Test
    fun deleteGroceryByName_callsCorrectRepositoryMethod() {
        runTest {
            val name = "Test Name"
            coEvery { mRepositoryImpl.deleteGroceryByName(any()) } returns Unit

            sutMealViewModel.deleteGroceryByName(name)
            coVerify { mRepositoryImpl.deleteGroceryByName(any()) }
        }
    }
}