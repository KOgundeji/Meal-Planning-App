package com.kunle.aisle9b.screens.recipes

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kunle.aisle9b.models.Meal
import com.kunle.aisle9b.models.MealFoodMap
import com.kunle.aisle9b.models.apiModels.DataOrException
import com.kunle.aisle9b.models.apiModels.recipeModels.Recipe
import com.kunle.aisle9b.models.apiModels.recipeModels.RecipeRawAPIData
import com.kunle.aisle9b.repository.ShoppingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesVM @Inject constructor(private val repository: ShoppingRepository) : ViewModel() {

    val recipeList = mutableStateOf(emptyList<Recipe>())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val rawDataHolder = getRandomRecipes(tags = "")
            if (rawDataHolder.data != null) {
                recipeList.value = buildList {
                    rawDataHolder.data!!.recipes.forEach {
                        add(it)
                    }
                }
            }
        }
    }

    suspend fun getRecipe(id: Int): DataOrException<Recipe, Boolean, Exception> {
        return repository.getRecipes(id = id)
    }

    suspend fun getRandomRecipes(tags: String): DataOrException<RecipeRawAPIData, Boolean, Exception> {
        return repository.getRandomRecipes(tags = tags)
    }

    fun insertMeal(meal: Meal) = viewModelScope.launch { repository.insertMeal(meal) }

    fun insertPair(crossRef: MealFoodMap) =
        viewModelScope.launch { repository.insertPair(crossRef) }

    fun deletePair(crossRef: MealFoodMap) =
        viewModelScope.launch { repository.deletePair(crossRef) }

    fun deleteMeal(meal: Meal) = viewModelScope.launch { repository.deleteMeal(meal) }
}