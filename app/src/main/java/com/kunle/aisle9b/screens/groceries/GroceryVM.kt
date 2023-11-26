package com.kunle.aisle9b.screens.groceries

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kunle.aisle9b.models.Food
import com.kunle.aisle9b.models.Grocery
import com.kunle.aisle9b.repositories.BasicRepositoryFunctions
import com.kunle.aisle9b.repositories.groceries.GroceryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroceryVM @Inject constructor(private val repository: GroceryRepository) : ViewModel(),
    BasicRepositoryFunctions {

    private val _groceryList = MutableStateFlow<List<Grocery>>(emptyList())
    private val _groupedGroceryList = MutableStateFlow<Map<String, List<Grocery>>>(emptyMap())
    private val _namesOfAllFoods = MutableStateFlow<List<String>>(emptyList())
    val groceryList = _groceryList.asStateFlow()
    val groupedGroceryList = _groupedGroceryList.asStateFlow()
    val namesOfAllFoods = _namesOfAllFoods.asStateFlow()

    init {
        Log.i("Test", "groceryVM instantiated")
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllGroceries().distinctUntilChanged().collect { groceryItems ->
                _groceryList.value = groceryItems
                _groupedGroceryList.value = groceryItems.groupBy { it.category }
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllFoodNames().distinctUntilChanged().collect { foodItems ->
                _namesOfAllFoods.value = foodItems
            }
        }
    }

    fun updateCategories(foodGroceryName: String, newCategory: String) {
        viewModelScope.launch {
            repository.updateGlobalFoodCategories(
                foodName = foodGroceryName,
                newCategory = newCategory
            )
            repository.updateGlobalGroceryCategories(
                groceryName = foodGroceryName,
                newCategory = newCategory
            )
        }
    }

    override suspend fun insertGrocery(grocery: Grocery) {
        viewModelScope.launch { repository.insertGrocery(grocery) }
    }

    suspend fun upsertGrocery(grocery: Grocery) {
        viewModelScope.launch { repository.upsertGrocery(grocery) }
    }

    override suspend fun deleteGrocery(grocery: Grocery) {
        viewModelScope.launch { repository.deleteGrocery(grocery) }
    }

    override suspend fun insertFood(food: Food): Long {
        var foodId = -1L
        viewModelScope.launch {
            foodId = async { repository.insertFood(food) }.await()
        }
        return foodId
    }

    override suspend fun upsertFood(food: Food) {
        viewModelScope.launch { repository.upsertFood(food) }
    }

    override suspend fun deleteFood(food: Food) {
        viewModelScope.launch { repository.deleteFood(food) }
    }

    override suspend fun deleteGroceryByName(name: String) {
        viewModelScope.launch { repository.deleteGroceryByName(name) }
    }

}