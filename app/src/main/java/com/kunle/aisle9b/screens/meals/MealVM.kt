package com.kunle.aisle9b.screens.meals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kunle.aisle9b.models.Meal
import com.kunle.aisle9b.models.MealFoodMap
import com.kunle.aisle9b.models.MealWithIngredients
import com.kunle.aisle9b.repository.ShoppingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MealVM @Inject constructor(private val repository: ShoppingRepository) : ViewModel() {

    private val _mealsList = MutableStateFlow<List<Meal>>(emptyList())
    private val _mealsWithIngredients = MutableStateFlow<List<MealWithIngredients>>(emptyList())
    val mealsList = _mealsList.asStateFlow()
    val mealsWithIngredients = _mealsWithIngredients.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllMeals().distinctUntilChanged().collect {
                _mealsList.value = it
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllMealsWithIngredients().distinctUntilChanged().collect {
                _mealsWithIngredients.value = it
            }
        }
    }

    fun insertMeal(meal: Meal) = viewModelScope.launch { repository.insertMeal(meal) }
    fun deleteMeal(meal: Meal) = viewModelScope.launch { repository.deleteMeal(meal) }
    fun updateMeal(meal: Meal) = viewModelScope.launch { repository.updateMeal(meal) }
    fun deleteAllMeals() = viewModelScope.launch { repository.deleteAllMeals() }
    suspend fun getMeal(name: String): Meal {
        return viewModelScope.async {
            repository.getMeal(name)
        }.await()
    }

    fun insertPair(crossRef: MealFoodMap) =
        viewModelScope.launch { repository.insertPair(crossRef) }

    fun deletePair(crossRef: MealFoodMap) =
        viewModelScope.launch { repository.deletePair(crossRef) }

    fun updatePair(crossRef: MealFoodMap) =
        viewModelScope.launch { repository.updatePair(crossRef) }

    fun deleteSpecificMealIngredients(mealId: UUID) =
        viewModelScope.launch { repository.deleteSpecificMealIngredients(mealId) }

    fun deleteAllMealWithIngredients() =
        viewModelScope.launch { repository.deleteAllMealWithIngredients() }

    suspend fun getSpecificMealWithIngredients(mealId: Long): MealWithIngredients {
        return viewModelScope.async {
            repository.getSpecificMealWithIngredients(mealId)
        }.await()
    }


}