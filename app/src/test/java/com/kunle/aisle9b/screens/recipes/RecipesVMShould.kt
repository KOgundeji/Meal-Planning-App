package com.kunle.aisle9b.screens.recipes

import com.google.common.truth.Truth.assertThat
import com.kunle.aisle9b.MainDispatcherRule
import com.kunle.aisle9b.api.apiModels.ApiResponseInstructions
import com.kunle.aisle9b.api.apiModels.ApiResponseRecipe
import com.kunle.aisle9b.api.apiModels.ApiResponseSearch
import com.kunle.aisle9b.api.apiModels.recipeModels.Recipe
import com.kunle.aisle9b.models.Meal
import com.kunle.aisle9b.models.MealFoodMap
import com.kunle.aisle9b.models.apiModels.instructionModels.Instructions
import com.kunle.aisle9b.models.apiModels.queryModels.RawJSON
import com.kunle.aisle9b.repositories.recipes.RecipeRepositoryImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.random.Random
import com.kunle.aisle9b.models.apiModels.queryModels.*

class RecipesVMShould {

    @get:Rule
    val coroutineRule = MainDispatcherRule()

    private val mRepository: RecipeRepositoryImpl = mockk()
    private lateinit var sutRecipesViewModel: RecipesVM

    @Before
    fun setUp() {
        sutRecipesViewModel = RecipesVM(mRepository)
    }

    @Test
    fun getRecipe_callsCorrectRepositoryMethod() {
        runTest {
            val recipe: Recipe = mockk()
            val recipeId: Int = Random(42L).nextInt()
            coEvery { mRepository.getRecipe(id = any()) } returns recipe

            sutRecipesViewModel.getRecipe(recipeId)
            coVerify { mRepository.getRecipe(id = any()) }
        }
    }

    @Test
    fun getRecipe_returnsSuccessfulRecipe() {
        runTest {
            val recipe: Recipe = mockk()
            val recipeId: Int = Random(42L).nextInt()
            coEvery { mRepository.getRecipe(id = any()) } returns recipe

            sutRecipesViewModel.getRecipe(recipeId)
            val actualRecipe = sutRecipesViewModel.retrievedRecipeState.value
            assertThat(actualRecipe).isEqualTo(ApiResponseRecipe.Success(recipe))
        }
    }

    @Test
    fun getRecipe_returnsException() {
        runTest {
            val exception: Exception = Exception("API didn't return Recipe")
            val recipeId: Int = Random(42L).nextInt()
            coEvery { mRepository.getRecipe(id = any()) } throws exception

            sutRecipesViewModel.getRecipe(recipeId)
            val actualRecipe = sutRecipesViewModel.retrievedRecipeState.value

            var possibleMessage = ""
            if (actualRecipe is ApiResponseRecipe.Error) {
                possibleMessage = actualRecipe.getMessage().toString()
            }
            assertThat(possibleMessage).isEqualTo("API didn't return Recipe")
        }
    }

    @Test
    fun getSearchResults_callsCorrectRepositoryMethod() {
        runTest {
            val rawJSON: RawJSON = mockk()
            val query = "Test Query"
            coEvery { mRepository.getSearchResults(any()) } returns rawJSON

            sutRecipesViewModel.getSearchResults(query)
            coVerify { mRepository.getSearchResults(any()) }
        }
    }

    @Test
    fun getSearchResults_returnsSuccessfulSearchResults() {
        runTest {
            val searchResultsList: List<Result> = listOf(mockk())
            val rawJSON: RawJSON = RawJSON(1, 1, searchResultsList, 1)
            val query = "Test Query"
            coEvery { mRepository.getSearchResults(any()) } returns rawJSON

            sutRecipesViewModel.getSearchResults(query)
            val actualResults = sutRecipesViewModel.searchState.value
            assertThat(actualResults).isEqualTo(ApiResponseSearch.Success(searchResultsList))
        }
    }

    @Test
    fun getSearchResults_returnsException() {
        runTest {
            val exception: Exception = Exception("API didn't return Search Results")
            val query = "Test Query"
            coEvery { mRepository.getSearchResults(any()) } throws exception

            sutRecipesViewModel.getSearchResults(query)
            val actualResults = sutRecipesViewModel.searchState.value

            var possibleMessage = ""
            if (actualResults is ApiResponseSearch.Error) {
                possibleMessage = actualResults.getMessage().toString()
            }
            assertThat(possibleMessage).isEqualTo("API didn't return Search Results")
        }
    }

    @Test
    fun getInstructions_callsCorrectRepositoryMethod() {
        runTest {
            val instructions: Instructions = mockk()
            val instructionsId: Int = Random(42L).nextInt()
            coEvery { mRepository.getInstructions(id = any()) } returns instructions

            sutRecipesViewModel.getInstructions(instructionsId)
            coVerify { mRepository.getInstructions(id = any()) }
        }
    }

    @Test
    fun getInstructions_returnsSuccessfulInstructions() {
        runTest {
            val instructions: Instructions = mockk()
            val instructionsId: Int = Random(42L).nextInt()
            coEvery { mRepository.getInstructions(id = any()) } returns instructions

            sutRecipesViewModel.getInstructions(instructionsId)
            val actualInstructions = sutRecipesViewModel.instructions.value
            assertThat(actualInstructions).isEqualTo(ApiResponseInstructions.Success(instructions))
        }
    }

    @Test
    fun getInstructions_returnsException() {
        runTest {
            val exception: Exception = Exception("API didn't return Instructions")
            val instructionsId: Int = Random(42L).nextInt()
            coEvery { mRepository.getInstructions(any()) } throws exception

            sutRecipesViewModel.getInstructions(instructionsId)
            val actualResults = sutRecipesViewModel.instructions.value

            var possibleMessage = ""
            if (actualResults is ApiResponseInstructions.Error) {
                possibleMessage = actualResults.getMessage().toString()
            }
            assertThat(possibleMessage).isEqualTo("API didn't return Instructions")
        }
    }

    @Test
    fun upsertMeal_callsCorrectRepositoryMethod() {
        val meal: Meal = mockk()
        coEvery { mRepository.upsertMeal(any()) } returns Unit

        sutRecipesViewModel.upsertMeal(meal)
        coVerify { mRepository.upsertMeal(any()) }
    }

    @Test
    fun insertPair_callsCorrectRepositoryMethod() {
        val pair: MealFoodMap = mockk()
        coEvery { mRepository.insertPair(any()) } returns Unit

        sutRecipesViewModel.insertPair(pair)
        coVerify { mRepository.insertPair(any()) }
    }

    @Test
    fun deletePair_callsCorrectRepositoryMethod() {
        val pair: MealFoodMap = mockk()
        coEvery { mRepository.deletePair(any()) } returns Unit

        sutRecipesViewModel.deletePair(pair)
        coVerify { mRepository.deletePair(any()) }
    }

    @Test
    fun deleteMeal_callsCorrectRepositoryMethod() {
        val meal: Meal = mockk()
        coEvery { mRepository.deleteMeal(any()) } returns Unit

        sutRecipesViewModel.deleteMeal(meal)
        coVerify { mRepository.deleteMeal(any()) }
    }
}