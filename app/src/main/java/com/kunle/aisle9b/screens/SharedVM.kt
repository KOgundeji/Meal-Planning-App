package com.kunle.aisle9b.screens

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kunle.aisle9b.models.*
import com.kunle.aisle9b.navigation.BottomNavItem
import com.kunle.aisle9b.navigation.GroceryScreens
import com.kunle.aisle9b.repository.ShoppingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedVM @Inject constructor(private val repository: ShoppingRepository) :
    ViewModel() {

    val mealDeleteList: MutableList<Meal> = mutableListOf()
    val groceryListDeleteList: MutableList<GroceryList> = mutableListOf()

    val mealStartAsTransfer = mutableStateOf(false)
    val customListStartAsTransfer = mutableStateOf(false)
    val tempIngredientList = mutableStateListOf<Food>()
    val tempGroceryList = mutableStateListOf<Food>()
    var darkModeSetting = mutableStateOf(false)
    var keepScreenOn = mutableStateOf(false)
    var categoriesOn = mutableStateOf(true)

    private var _groceryList = MutableStateFlow<List<Food>>(emptyList())
    val groceryList = _groceryList.asStateFlow()

    private val _groceryBadgeCount = MutableStateFlow(0)
    private val _numOfMeals = MutableStateFlow(0)
    val groceryBadgeCount = mutableStateOf(_groceryBadgeCount.value)
    val numOfMeals = mutableStateOf(_numOfMeals.value)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllGroceries().distinctUntilChanged().collect { listOfGroceries ->
                _groceryList.value = listOfGroceries
                _groceryBadgeCount.value = listOfGroceries.size
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllMeals().distinctUntilChanged().collect { listOfMeals ->
                _numOfMeals.value = listOfMeals.size
            }
        }
    }


    fun insertFood(food: Food) = viewModelScope.launch { repository.insertFood(food) }
    fun deleteFood(food: Food) = viewModelScope.launch { repository.deleteFood(food) }
    fun updateFood(food: Food) = viewModelScope.launch { repository.updateFood(food) }
    fun deleteAllFood() = viewModelScope.launch { repository.deleteAllFood() }
    suspend fun getFood(name: String): Food {
        return viewModelScope.async {
            repository.getFood(name)
        }.await()
    }




}