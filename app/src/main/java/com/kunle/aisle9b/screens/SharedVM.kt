package com.kunle.aisle9b.screens

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kunle.aisle9b.models.*
import com.kunle.aisle9b.repository.ShoppingRepository
import com.kunle.aisle9b.screens.customLists.CustomListButtonBar
import com.kunle.aisle9b.screens.meals.MealButtonBar
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

    val tempIngredientList = mutableStateListOf<Food>()
    val tempGroceryList = mutableStateListOf<Food>()

    var darkModeSetting = mutableStateOf(false)
    var keepScreenOnSetting = mutableStateOf(false)
    var categoriesOnSetting = mutableStateOf(true)

    var mealButtonBar = mutableStateOf(MealButtonBar.Default)
    var customListButtonBar = mutableStateOf(CustomListButtonBar.Default)

    private var _groceryList = MutableStateFlow<List<Food>>(emptyList())
    private val _settings = MutableStateFlow<List<AppSettings>>(emptyList())
    private val _groceryBadgeCount = MutableStateFlow(0)
    private val _numOfMeals = MutableStateFlow(0)
    val groceryList = _groceryList.asStateFlow()
    val settingsList = _settings.asStateFlow()
    val groceryBadgeCount = _groceryBadgeCount.asStateFlow()
    val numOfMeals = _numOfMeals.asStateFlow()

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
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllSettings().distinctUntilChanged().collect { listOfSettings ->
                _settings.value = listOfSettings
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

    fun insertSettings(settings: AppSettings) =
        viewModelScope.launch { repository.insertSettings(settings) }

    fun updateSettings(settings: AppSettings) =
        viewModelScope.launch { repository.updateSettings(settings) }

    suspend fun checkSetting(name: String): Int {
        return viewModelScope.async(Dispatchers.Default) {
            repository.checkSetting(name)
        }.await()
    }


}