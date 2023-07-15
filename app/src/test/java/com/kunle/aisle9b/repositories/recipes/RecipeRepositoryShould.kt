package com.kunle.aisle9b.repositories.recipes

import com.google.common.truth.Truth.assertThat
import com.kunle.aisle9b.api.RecipeAPI
import com.kunle.aisle9b.api.apiModels.recipeModels.Recipe
import com.kunle.aisle9b.data.MealDao
import com.kunle.aisle9b.data.MealWithIngredientsDao
import com.kunle.aisle9b.models.Meal
import com.kunle.aisle9b.models.MealFoodMap
import com.kunle.aisle9b.models.apiModels.instructionModels.Instructions
import com.kunle.aisle9b.models.apiModels.queryModels.RawJSON
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import kotlin.random.Random

class RecipeRepositoryShould {

    private val mMealDao: MealDao = mockk()
    private val mMWIDao: MealWithIngredientsDao = mockk()
    private val mRecipeAPI: RecipeAPI = mockk()
    private lateinit var sutRepository: RecipeRepositoryImpl

    @Before
    fun setUp() {
        sutRepository = RecipeRepositoryImpl(mMealDao, mRecipeAPI, mMWIDao)
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
    fun getRecipe_throwsException() {
        runTest {
            val recipeId: Int = Random(42L).nextInt()
            coEvery { mRecipeAPI.getRecipe(id = any()) } throws Exception("API didn't return Recipe")
            var exception = false

            try {
                sutRepository.getRecipe(recipeId)
            } catch (e: Exception) {
                assertThat(e.message).isEqualTo("API didn't return Recipe")
                exception = true
            }
            Assert.assertTrue(exception)
        }
    }

    @Test
    fun getSearchResults_callsCorrectDaoMethod() {
        runTest {
            val searchResults: RawJSON = mockk()
            val query = "Test Query"
            coEvery { mRecipeAPI.getSearchResults(query = any()) } returns searchResults

            sutRepository.getSearchResults(query)
            coVerify { mRecipeAPI.getSearchResults(query = any()) }
        }
    }

    @Test
    fun getSearchResults_returnsCorrectSearchResults() {
        runTest {
            val searchResults: RawJSON = mockk()
            val query = "Test Query"
            coEvery { mRecipeAPI.getSearchResults(query = any()) } returns searchResults

            val actualInstructions = sutRepository.getSearchResults(query)
            assertThat(actualInstructions).isEqualTo(searchResults)
        }
    }

    @Test
    fun getSearchResults_throwsException() {
        runTest {
            val query = "Test Query"
            coEvery { mRecipeAPI.getSearchResults(query = any()) } throws Exception("API didn't return Search Results")
            var exception = false

            try {
                sutRepository.getSearchResults(query)
            } catch (e: Exception) {
                assertThat(e.message).isEqualTo("API didn't return Search Results")
                exception = true
            }
            Assert.assertTrue(exception)
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
    fun getInstructions_throwsException() {
        runTest {
            val instructionsId: Int = Random(42L).nextInt()
            coEvery { mRecipeAPI.getInstructions(id = any()) } throws Exception("API didn't return Instructions")
            var exception = false

            try {
                sutRepository.getInstructions(instructionsId)
            } catch (e: Exception) {
                assertThat(e.message).isEqualTo("API didn't return Instructions")
                exception = true
            }
            Assert.assertTrue(exception)
        }
    }
}