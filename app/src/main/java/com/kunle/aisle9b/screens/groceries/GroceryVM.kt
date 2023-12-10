package com.kunle.aisle9b.screens.groceries

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

    private val _namesOfAllFoods = MutableStateFlow<Set<String>>(emptySet())
    private val _foodCategoryMap = MutableStateFlow<Map<String, String>>(emptyMap())

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
            repository.getAllFoods().distinctUntilChanged().collect {
                _foodCategoryMap.value = it.associate { food ->
                    Pair(food.name, food.category)
                }
                _namesOfAllFoods.value = _foodCategoryMap.value.keys
            }
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

    fun addGrocery(name: String, quantity: String) {
        val category = _foodCategoryMap.value[name]
        viewModelScope.launch {
            if (category != null) {
                insertGrocery(
                    Grocery(name = name, quantity = quantity, category = category)
                )
            } else {
                insertGrocery(
                    Grocery(name = name, quantity = quantity)
                )
            }
        }
    }

    fun updateGrocery(grocery: Grocery) {
        val originalCategory = _foodCategoryMap.value[grocery.name]
        if (originalCategory != grocery.category) {
            updateCategories(grocery.name,grocery.category)
        }
        viewModelScope.launch {
            upsertGrocery(grocery)
        }
    }

    private fun updateCategories(foodGroceryName: String, newCategory: String) {
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