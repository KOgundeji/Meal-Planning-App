package com.kunle.aisle9b.screens.recipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kunle.aisle9b.models.Meal
import com.kunle.aisle9b.models.MealFoodMap
import com.kunle.aisle9b.models.apiModels.DataOrException
import com.kunle.aisle9b.models.apiModels.searchedRecipeModels.SearchedRawAPIData
import com.kunle.aisle9b.models.apiModels.trendingRecipeModels.TrendingRawAPIData
import com.kunle.aisle9b.repository.ShoppingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesVM @Inject constructor(private val repository: ShoppingRepository): ViewModel()  {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getTrendingRecipes(vegetarian = false)
        }
    }
    suspend fun getSearchedRecipes(tags: String, query: String): DataOrException<SearchedRawAPIData, Boolean, Exception> {
        return repository.getSearchedRecipes(tags = tags, query = query)
    }

    suspend fun getTrendingRecipes(vegetarian: Boolean): DataOrException<TrendingRawAPIData, Boolean, Exception> {
        return repository.getTrendingRecipes(vegetarian = vegetarian)
    }

    fun insertMeal(meal: Meal) = viewModelScope.launch { repository.insertMeal(meal) }

    fun insertPair(crossRef: MealFoodMap) =
        viewModelScope.launch { repository.insertPair(crossRef) }

    fun deletePair(crossRef: MealFoodMap) =
        viewModelScope.launch { repository.deletePair(crossRef) }

    fun deleteMeal(meal: Meal) = viewModelScope.launch { repository.deleteMeal(meal) }
}