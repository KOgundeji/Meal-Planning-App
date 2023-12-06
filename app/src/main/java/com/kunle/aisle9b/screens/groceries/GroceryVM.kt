package com.kunle.aisle9b.screens.groceries

import android.util.Log
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroceryVM @Inject constructor(private val repository: GroceryRepository) : ViewModel(),
    BasicRepositoryFunctions {

    private val _groceryList = MutableStateFlow<List<Grocery>>(emptyList())
    val groceryList = _groceryList.asStateFlow()
    private val _groupedGroceryList = MutableStateFlow<Map<String, List<Grocery>>>(emptyMap())
    val groupedGroceryList = _groupedGroceryList.asStateFlow()
    private val _namesOfAllFoods = MutableStateFlow<List<String>>(emptyList())
    val namesOfAllFoods = _namesOfAllFoods.asStateFlow()
    private val _suggestions = MutableStateFlow<List<String>>(emptyList())
    val suggestions = _suggestions.asStateFlow()

    init {
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

    fun updateAutoComplete(text: String) {
            _suggestions.update { _ ->
                if (text != "") {
                    _namesOfAllFoods.value.filter { string ->
                        string.contains(text, ignoreCase = true) && text != string
                    }
                        .take(3)
                } else {
                    emptyList()
                }
            }
    }

    override suspend fun insertGrocery(grocery: Grocery) {
        viewModelScope.launch { repository.insertGrocery(grocery) }
    }

    override suspend fun upsertGrocery(grocery: Grocery) {
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