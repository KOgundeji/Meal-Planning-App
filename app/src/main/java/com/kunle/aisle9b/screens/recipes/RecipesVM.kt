package com.kunle.aisle9b.screens.recipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kunle.aisle9b.models.Meal
import com.kunle.aisle9b.models.MealFoodMap
import com.kunle.aisle9b.api.apiModels.ApiResponseInstructions
import com.kunle.aisle9b.api.apiModels.ApiResponseRecipe
import com.kunle.aisle9b.api.apiModels.ApiResponseList
import com.kunle.aisle9b.repository.ShoppingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesVM @Inject constructor(private val repository: ShoppingRepository) : ViewModel() {

    private val _searchState = MutableStateFlow<ApiResponseList>(ApiResponseList.Neutral)
    private val _retrievedRecipeState =
        MutableStateFlow<ApiResponseRecipe>(ApiResponseRecipe.Neutral)
    private val _instructionsState =
        MutableStateFlow<ApiResponseInstructions>(ApiResponseInstructions.Loading)
    val searchState = _searchState.asStateFlow()
    val retrievedRecipeState = _retrievedRecipeState.asStateFlow()
    val instructions = _instructionsState.asStateFlow()

    suspend fun getRecipe(id: Int) {
        viewModelScope.launch {
            _retrievedRecipeState.value = ApiResponseRecipe.Loading
            try {
                val recipe = repository.getRecipe(id = id)
                _retrievedRecipeState.value = ApiResponseRecipe.Success(recipe = recipe)
            } catch (e: Exception) {
                _retrievedRecipeState.value = ApiResponseRecipe.Error(exception = e)
            }
        }
    }

    fun getSearchResults(query: String) {
        viewModelScope.launch {
            _searchState.value = ApiResponseList.Loading
            try {
                val searchResults = repository.getSearchResults(query = query)
                _searchState.value = ApiResponseList.Success(searchResults.results)
            } catch (e: Exception) {
                _searchState.value = ApiResponseList.Error(exception = e)
            }
        }
    }

    fun getInstructions(id: Int) {
        viewModelScope.launch {
            _instructionsState.value = ApiResponseInstructions.Loading
            try {
                val instructions = repository.getInstructions(id = id)
                _instructionsState.value = ApiResponseInstructions.Success(instructions = instructions)
            } catch (e: Exception) {
                _instructionsState.value = ApiResponseInstructions.Error(exception = e)
            }
        }
    }

    fun insertMeal(meal: Meal) = viewModelScope.launch { repository.insertMeal(meal) }

    fun insertPair(crossRef: MealFoodMap) =
        viewModelScope.launch { repository.insertPair(crossRef) }

    fun deletePair(crossRef: MealFoodMap) =
        viewModelScope.launch { repository.deletePair(crossRef) }

    fun deleteMeal(meal: Meal) = viewModelScope.launch { repository.deleteMeal(meal) }
}