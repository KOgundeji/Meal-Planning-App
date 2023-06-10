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

    var mealToBeSaved: Meal? = null
    var foodListToBeSaved: List<Food>? = null
    var instructionsToBeSaved: List<Instruction>? = null
    var apiMealToBeSaved: Meal? = null

    private var _groceryList = MutableStateFlow<List<Food>>(emptyList())
    private var _categoryMap = MutableStateFlow<Map<String,String>>(emptyMap())
    private val _settings = MutableStateFlow<List<AppSettings>>(emptyList())
    private val _groceryBadgeCount = MutableStateFlow(0)
    private val _numOfMeals = MutableStateFlow(0)
    val groceryList = _groceryList.asStateFlow()
    val categoryMap = _categoryMap.asStateFlow()
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
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllCategories().distinctUntilChanged().collect { listOfCategories ->
                val mutableMap = mutableMapOf<String,String>()
                listOfCategories.forEach {entity ->
                    mutableMap[entity.foodName] = entity.categoryName
                }
                _categoryMap.value = mutableMap.toMap()
            }
        }

    }

    fun saveCreatedMealonFABClick() {
        if (mealToBeSaved != null) {
            viewModelScope.launch {
                repository.upsertMeal(mealToBeSaved!!)
                if (foodListToBeSaved != null) {
                    foodListToBeSaved!!.forEach { food ->
                        repository.upsertFood(food)
                        repository.insertPair(
                            MealFoodMap(
                                mealId = mealToBeSaved!!.mealId,
                                foodId = food.foodId
                            )
                        )
                    }
                }
            }
            if (instructionsToBeSaved != null) {
                viewModelScope.launch {
                    instructionsToBeSaved!!.forEach {
                        repository.upsertInstruction(it)
                    }
                }
            }
        }
    }

    fun saveAPIMealonFABClick() {
        if (apiMealToBeSaved != null) {
            viewModelScope.launch {
                repository.upsertMeal(apiMealToBeSaved!!)
            }
        }
    }

    fun upsertFood(food: Food) = viewModelScope.launch { repository.upsertFood(food) }
    fun deleteFood(food: Food) = viewModelScope.launch { repository.deleteFood(food) }
    fun deleteAllFood() = viewModelScope.launch { repository.deleteAllFood() }

    fun upsertCategory(category: Category) = viewModelScope.launch { repository.upsertCategory(category) }
    fun deleteCategory(category: Category) = viewModelScope.launch { repository.deleteCategory(category) }
    fun deleteAllCategories() = viewModelScope.launch { repository.deleteAllCategories() }

    fun insertSettings(settings: AppSettings) =
        viewModelScope.launch { repository.insertSettings(settings) }

    fun updateSettings(settings: AppSettings) =
        viewModelScope.launch { repository.updateSettings(settings) }
}