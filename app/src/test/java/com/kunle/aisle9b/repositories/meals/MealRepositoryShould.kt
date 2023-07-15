package com.kunle.aisle9b.repositories.meals

import com.google.common.truth.Truth.assertThat
import com.kunle.aisle9b.api.RecipeAPI
import com.kunle.aisle9b.api.apiModels.recipeModels.Recipe
import com.kunle.aisle9b.data.GroceryDao
import com.kunle.aisle9b.data.InstructionDao
import com.kunle.aisle9b.data.MealDao
import com.kunle.aisle9b.data.MealWithIngredientsDao
import com.kunle.aisle9b.models.*
import com.kunle.aisle9b.models.apiModels.instructionModels.Instructions
import io.mockk.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.random.Random

class MealRepositoryShould {

    private val mMealDao: MealDao = mockk()
    private val mGroceryDao: GroceryDao = mockk()
    private val mInstructionDao: InstructionDao = mockk()
    private val mMWIDao: MealWithIngredientsDao = mockk()
    private val mRecipeAPI: RecipeAPI = mockk()
    private lateinit var sutRepository: MealRepositoryImpl

    @Before
    fun setUp() {
        sutRepository =
            MealRepositoryImpl(mGroceryDao, mMealDao, mInstructionDao, mMWIDao, mRecipeAPI)
    }

    @Test
    fun getRecipe_callsCorrectDaoMethod() {
        runTest {
            val recipe: Recipe = mockk()
            val recipeId: Int = Random(42L).nextInt()
            coEvery { mRecipeAPI.getRecipe(id = any()) } returns recipe

            sutRepository.getRecipe(recipeId)
            coVerify { mRecipeAPI.getRecipe(id = any()) }
        }
    }

    @Test
    fun getRecipe_returnsCorrectRecipe() {
        runTest {
            val recipe: Recipe = mockk()
            val recipeId: Int = Random(42L).nextInt()
            coEvery { mRecipeAPI.getRecipe(id = any()) } returns recipe

            val actualRecipe = sutRepository.getRecipe(recipeId)
            assertThat(actualRecipe).isEqualTo(recipe)
        }
    }

    @Test
    fun getInstructions_callsCorrectDaoMethod() {
        runTest {
            val instructions: Instructions = mockk()
            val instructionsId: Int = Random(42L).nextInt()
            coEvery { mRecipeAPI.getInstructions(id = any()) } returns instructions

            sutRepository.getInstructions(instructionsId)
            coVerify { mRecipeAPI.getInstructions(id = any()) }
        }
    }

    @Test
    fun getInstructions_returnsCorrectInstructions() {
        runTest {
            val instructions: Instructions = mockk()
            val instructionsId: Int = Random(42L).nextInt()
            coEvery { mRecipeAPI.getInstructions(id = any()) } returns instructions

            val actualInstructions = sutRepository.getInstructions(instructionsId)
            assertThat(actualInstructions).isEqualTo(instructions)
        }
    }

    @Test
    fun getAllMeals_callsCorrectDaoMethod() {
        runTest {
            val mealsList: List<Meal> = listOf(mockk())
            every { mMealDao.getAllMeals() } returns flow { emit(mealsList) }

            sutRepository.getAllMeals()
            verify { mMealDao.getAllMeals() }
        }
    }

    @Test
    fun getAllMeals_returnsCorrectList() {
        runTest {
            val mealsList: List<Meal> = listOf(mockk())
            every { mMealDao.getAllMeals() } returns flow { emit(mealsList) }

            val actualList = sutRepository.getAllMeals().first()
            assertThat(actualList).isEqualTo(mealsList)
        }
    }

    @Test
    fun getAllInstructions_callsCorrectDaoMethod() {
        runTest {
            val instructionsList: List<Instruction> = listOf(mockk())
            every { mInstructionDao.getAllInstructions() } returns flow { emit(instructionsList) }

            sutRepository.getAllInstructions()
            verify { mInstructionDao.getAllInstructions() }
        }
    }

    @Test
    fun getAllInstructions_returnsCorrectList() {
        runTest {
            val instructionsList: List<Instruction> = listOf(mockk())
            every { mInstructionDao.getAllInstructions() } returns flow { emit(instructionsList) }

            val actualList = sutRepository.getAllInstructions().first()
            assertThat(actualList).isEqualTo(instructionsList)
        }
    }

    @Test
    fun getAllMealsWithIngredients_callsCorrectDaoMethod() {
        runTest {
            val mwiList: List<MealWithIngredients> = listOf(mockk())
            every { mMWIDao.getAllMealsWithIngredients() } returns flow { emit(mwiList) }

            sutRepository.getAllMealsWithIngredients()
            verify { mMWIDao.getAllMealsWithIngredients() }
        }
    }

    @Test
    fun getAllMealsWithIngredients_returnsCorrectList() {
        runTest {
            val mwiList: List<MealWithIngredients> = listOf(mockk())
            every { mMWIDao.getAllMealsWithIngredients() } returns flow { emit(mwiList) }

            val actualList = sutRepository.getAllMealsWithIngredients().first()
            assertThat(actualList).isEqualTo(mwiList)
        }
    }

    @Test
    fun updateName_callsCorrectDaoMethod() {
        runTest {
            val updatedName: MealNameUpdate = mockk()
            coEvery { mMealDao.updateName(any()) } returns Unit

            sutRepository.updateName(updatedName)
            coVerify { mMealDao.updateName(any()) }
        }
    }

    @Test
    fun updatePic_callsCorrectDaoMethod() {
        runTest {
            val updatedPic: PicUpdate = mockk()
            coEvery { mMealDao.updatePic(any()) } returns Unit

            sutRepository.updatePic(updatedPic)
            coVerify { mMealDao.updatePic(any()) }
        }
    }

    @Test
    fun updateServingSize_callsCorrectDaoMethod() {
        runTest {
            val updatedServingSize: ServingSizeUpdate = mockk()
            coEvery { mMealDao.updateServingSize(any()) } returns Unit

            sutRepository.updateServingSize(updatedServingSize)
            coVerify { mMealDao.updateServingSize(any()) }
        }
    }

    @Test
    fun updateNotes_callsCorrectDaoMethod() {
        runTest {
            val updatedNote: NotesUpdate = mockk()
            coEvery { mMealDao.updateNotes(any()) } returns Unit

            sutRepository.updateNotes(updatedNote)
            coVerify { mMealDao.updateNotes(any()) }
        }
    }

    @Test
    fun insertMeal_callsCorrectDaoMethod() {
        runTest {
            val meal: Meal = mockk()
            val returnId: Long = Random(42L).nextLong()
            coEvery { mMealDao.insertMeal(any()) } returns returnId

            sutRepository.insertMeal(meal)
            coVerify { mMealDao.insertMeal(any()) }
        }
    }

    @Test
    fun insertMeal_returnsCorrectList() {
        runTest {
            val meal: Meal = mockk()
            val returnId: Long = Random(42L).nextLong()
            coEvery { mMealDao.insertMeal(any()) } returns returnId

            val actualReturnedId = sutRepository.insertMeal(meal)
            assertThat(actualReturnedId).isEqualTo(returnId)
        }
    }

    @Test
    fun upsertMeal_callsCorrectDaoMethod() {
        runTest {
            val meal: Meal = mockk()
            coEvery { mMealDao.upsertMeal(any()) } returns Unit

            sutRepository.upsertMeal(meal)
            coVerify { mMealDao.upsertMeal(any()) }
        }
    }

    @Test
    fun deleteMeal_callsCorrectDaoMethod() {
        runTest {
            val meal: Meal = mockk()
            coEvery { mMealDao.deleteMeal(any()) } returns Unit

            sutRepository.deleteMeal(meal)
            coVerify { mMealDao.deleteMeal(any()) }
        }
    }

    @Test
    fun insertGrocery_callsCorrectDaoMethod() {
        runTest {
            val grocery: Grocery = mockk()
            coEvery { mGroceryDao.insertGrocery(any()) } returns Unit

            sutRepository.insertGrocery(grocery)

            coVerify { mGroceryDao.insertGrocery(any()) }
        }
    }

    @Test
    fun deleteGroceryByName_callsCorrectDaoMethod() {
        runTest {
            val name = "Test Name"
            coEvery { mGroceryDao.deleteGroceryByName(any()) } returns Unit

            sutRepository.deleteGroceryByName(name)
            coVerify { mGroceryDao.deleteGroceryByName(any()) }
        }
    }

    @Test
    fun insertFood_callsCorrectDaoMethod() {
        runTest {
            val food: Food = mockk()
            val returnId = Random(42L).nextLong()
            coEvery { mGroceryDao.insertFood(any()) } returns returnId

            sutRepository.insertFood(food)

            coVerify { mGroceryDao.insertFood(any()) }
        }
    }

    @Test
    fun insertFood_returnsFoodId() {
        runTest {
            val food: Food = mockk()
            val returnId = Random(42L).nextLong()
            coEvery { mGroceryDao.insertFood(any()) } returns returnId

            val actual = sutRepository.insertFood(food)

            assertThat(actual).isEqualTo(returnId)
        }
    }

    @Test
    fun upsertFood_callsCorrectDaoMethod() {
        runTest {
            val food: Food = mockk()
            coEvery { mGroceryDao.upsertFood(any()) } returns Unit

            sutRepository.upsertFood(food)

            coVerify { mGroceryDao.upsertFood(any()) }
        }
    }

    @Test
    fun deleteFood_callsCorrectDaoMethod() {
        runTest {
            val food: Food = mockk()
            coEvery { mGroceryDao.deleteFood(any()) } returns Unit

            sutRepository.deleteFood(food)
            coVerify { mGroceryDao.deleteFood(any()) }
        }
    }

    @Test
    fun deleteGrocery_callsCorrectDaoMethod() {
        runTest {
            val grocery: Grocery = mockk()
            coEvery { mGroceryDao.deleteGrocery(any()) } returns Unit

            sutRepository.deleteGrocery(grocery)
            coVerify { mGroceryDao.deleteGrocery(any()) }
        }
    }

    @Test
    fun insertPair_callsCorrectDaoMethod() {
        runTest {
            val pair: MealFoodMap = mockk()
            coEvery { mMWIDao.insertPair(any()) } returns Unit

            sutRepository.insertPair(pair)
            coVerify { mMWIDao.insertPair(any()) }
        }
    }

    @Test
    fun deletePair_callsCorrectDaoMethod() {
        runTest {
            val pair: MealFoodMap = mockk()
            coEvery { mMWIDao.deletePair(any()) } returns Unit

            sutRepository.deletePair(pair)
            coVerify { mMWIDao.deletePair(any()) }
        }
    }

    @Test
    fun deleteSpecificMealWithIngredients_callsCorrectDaoMethod() {
        runTest {
            val mwiId: Long = Random(42L).nextLong()
            coEvery { mMWIDao.deleteSpecificMealWithIngredients(any()) } returns Unit

            sutRepository.deleteSpecificMealWithIngredients(mwiId)
            coVerify { mMWIDao.deleteSpecificMealWithIngredients(any()) }
        }
    }

    @Test
    fun upsertInstruction_callsCorrectDaoMethod() {
        runTest {
            val instruction: Instruction = mockk()
            coEvery { mInstructionDao.upsertInstruction(any()) } returns Unit

            sutRepository.upsertInstruction(instruction)
            coVerify { mInstructionDao.upsertInstruction(any()) }
        }
    }

    @Test
    fun deleteInstruction_callsCorrectDaoMethod() {
        runTest {
            val instruction: Instruction = mockk()
            coEvery { mInstructionDao.deleteInstruction(any()) } returns Unit

            sutRepository.deleteInstruction(instruction)
            coVerify { mInstructionDao.deleteInstruction(any()) }
        }
    }

}