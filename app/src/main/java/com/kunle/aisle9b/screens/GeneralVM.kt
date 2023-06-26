package com.kunle.aisle9b.screens

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kunle.aisle9b.models.*
import com.kunle.aisle9b.repositories.general.GeneralRepository
import com.kunle.aisle9b.screens.customLists.CustomListButtonBar
import com.kunle.aisle9b.screens.meals.MealButtonBar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GeneralVM @Inject constructor(private val repository: GeneralRepository) :
    ViewModel() {

    val mealDeleteList: MutableList<Meal> = mutableListOf()
    val groceryListDeleteList: MutableList<GroceryList> = mutableListOf()

//    val tempIngredientList = mutableStateListOf<Food>()
//    val tempGroceryList = mutableStateListOf<Food>()

    var darkModeSetting = mutableStateOf(false)
    var keepScreenOnSetting = mutableStateOf(false)
    var categoriesOnSetting = mutableStateOf(true)

    var mealButtonBar = mutableStateOf(MealButtonBar.Default)
    var customListButtonBar = mutableStateOf(CustomListButtonBar.Default)

    var mealToBeSaved: Meal? = null
    var ingredientListToBeSaved: List<Food>? = null
    var instructionsToBeSaved: List<Instruction>? = null
    var apiMealToBeSaved: Meal? = null

    private var _groceryList = MutableStateFlow<List<Food>>(emptyList())
    private val _settings = MutableStateFlow<List<AppSettings>>(emptyList())
    private val _groceryBadgeCount = MutableStateFlow(0)
    private val _numOfMeals = MutableStateFlow(0)
    val groceryList = _groceryList.asStateFlow()
    val settingsList = _settings.asStateFlow()
    val groceryBadgeCount = _groceryBadgeCount.asStateFlow()
    val numOfMeals = _numOfMeals.asStateFlow()

    init {
        upsertSettings(AppSettings(SettingsEnum.DarkMode.name, false))
        upsertSettings(AppSettings(SettingsEnum.Categories.name, true))
        upsertSettings(AppSettings(SettingsEnum.ScreenPermOn.name, false))

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

    fun upsertFood(food: Food) = viewModelScope.launch { repository.upsertFood(food) }
    fun deleteFood(food: Food) = viewModelScope.launch { repository.deleteFood(food) }
    fun insertGrocery(grocery: Grocery) =
        viewModelScope.launch { repository.insertGrocery(grocery) }

    fun upsertSettings(settings: AppSettings) =
        viewModelScope.launch { repository.upsertSettings(settings) }

    fun saveCreatedMealonFABClick() {
        if (mealToBeSaved != null) {
            viewModelScope.launch {
                repository.upsertMeal(mealToBeSaved!!)
                if (ingredientListToBeSaved != null) {
                    ingredientListToBeSaved!!.forEach { food ->
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

    fun filterForReconciliation(lists: List<List<Food>>):
            Map<String, List<Food>> {

        val ingredientMap: MutableMap<String, MutableList<Food>> = mutableMapOf()
        val filteredIngredientMap: MutableMap<String, MutableList<Food>> = mutableMapOf()

        for (list in lists) {
            for (food in list) {
                if (ingredientMap.containsKey(food.name)) {
                    ingredientMap[food.name]!!.add(food)
                } else {
                    ingredientMap[food.name] = mutableListOf(food)
                }
            }
        }

        ingredientMap.forEach { (key, value) ->
            if (value.size == 1) {
                val addToGroceryList = Grocery(
                    name = key,
                    quantity = value[0].quantity,
                    category = value[0].category
                )
                viewModelScope.launch { insertGrocery(addToGroceryList) }
            } else {
                filteredIngredientMap[key] = value
            }
        }
        return filteredIngredientMap
    }
}