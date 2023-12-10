package com.kunle.aisle9b.screens

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kunle.aisle9b.TopBarOptions
import com.kunle.aisle9b.models.AppSettings
import com.kunle.aisle9b.models.Food
import com.kunle.aisle9b.models.Grocery
import com.kunle.aisle9b.models.Meal
import com.kunle.aisle9b.models.SettingsEnum
import com.kunle.aisle9b.navigation.GroceryScreens
import com.kunle.aisle9b.repositories.general.GeneralRepository
import com.kunle.aisle9b.screens.meals.MealListOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GeneralVM @Inject constructor(private val repository: GeneralRepository) :
    ViewModel() {

    private var _topBar = MutableStateFlow(TopBarOptions.Default)
    val topBar = _topBar.asStateFlow()

    private var _source = MutableStateFlow(GroceryScreens.GroceryListScreen)
    val source = _source.asStateFlow()

    private var _mealViewSetting = MutableStateFlow<MealListOptions?>(null)
    val mealViewSetting = _mealViewSetting.asStateFlow()

    var darkModeSetting: Boolean? by mutableStateOf(false)
        private set
    var categoriesSetting by mutableStateOf(true)
        private set
    var screenOnSetting by mutableStateOf(false)
        private set


    private var newMealToBeSaved = mutableStateOf<Meal?>(null)
    var apiMealToBeSaved = mutableStateOf<Meal?>(null)

    private var _groceryList = MutableStateFlow<List<Grocery>>(emptyList())
    private val _groceryBadgeCount = MutableStateFlow(0) //not sure where this comes from
    private val _numOfMeals = MutableStateFlow(0)
    private val _allMeals = MutableStateFlow<List<Meal>>(emptyList())
    val groceryList = _groceryList.asStateFlow()
    val groceryBadgeCount = _groceryBadgeCount.asStateFlow()
    val numOfMeals = _numOfMeals.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllMeals().distinctUntilChanged().collect { listOfMeals ->
                _allMeals.value = listOfMeals
                _numOfMeals.value = listOfMeals.filter { it.visible }.size
            }
        }

        viewModelScope.launch {
            repository.getAllGroceries().distinctUntilChanged().collect { listOfGroceries ->
                _groceryList.value = listOfGroceries
                _groceryBadgeCount.value = listOfGroceries.size
            }
        }

        viewModelScope.launch {
            repository.getAllSettings().distinctUntilChanged().collect { listOfSettings ->
                getDarkModeSetting(listOfSettings)
                getCategoriesOn(listOfSettings)
                getScreenPermOn(listOfSettings)
                checkMealViewSetting(listOfSettings)
            }
        }
    }

    fun upsertFood(food: Food) = viewModelScope.launch { repository.upsertFood(food) }
    fun deleteFood(food: Food) = viewModelScope.launch { repository.deleteFood(food) }
    fun insertGrocery(grocery: Grocery) =
        viewModelScope.launch {
            repository.insertGrocery(grocery)
        }

    fun upsertGrocery(grocery: Grocery) =
        viewModelScope.launch { repository.upsertGrocery(grocery) }

    private fun upsertSettings(settings: AppSettings) =
        viewModelScope.launch { repository.upsertSettings(settings) }


    fun turnNewMealVisible() {
        if (newMealToBeSaved.value != null) {
            viewModelScope.launch {
                repository.upsertMeal(
                    Meal.makeNewMealVisible(newMealToBeSaved.value!!)
                )
            }
        }
    }

    fun cleanListsInDatabase() {
        viewModelScope.launch {
            _allMeals.value
                .filter { !it.visible }
                .forEach { dirtyMeal ->
                    repository.deleteMeal(dirtyMeal)
                }
        }
    }

    fun setTopBarOption(value: TopBarOptions) {
        _topBar.value = value
    }

    fun setClickSource(value: GroceryScreens) {
        _source.value = value
    }

    private fun getDarkModeSetting(settingsList: List<AppSettings>) {
        darkModeSetting = settingsList.firstOrNull {
            it.settingsName == SettingsEnum.DarkMode.name
        }?.value
    }

    fun setDarkModeSetting(value: Boolean): Boolean {
        upsertSettings(AppSettings(SettingsEnum.DarkMode.name, value))
        return value
    }

    private fun getCategoriesOn(settingsList: List<AppSettings>) {
        val categories = settingsList.firstOrNull {
            it.settingsName == SettingsEnum.Categories.name
        }?.value

        categoriesSetting = if (categories == null) {
            upsertSettings(AppSettings(SettingsEnum.Categories.name, true))
            true
        } else {
            categories
        }
    }

    fun setCategoriesOnSetting(value: Boolean) {
        upsertSettings(AppSettings(SettingsEnum.Categories.name, value))
    }

    private fun getScreenPermOn(settingsList: List<AppSettings>) {
        val screen = settingsList.firstOrNull {
            it.settingsName == SettingsEnum.ScreenPermOn.name
        }?.value

        screenOnSetting = if (screen == null) {
            upsertSettings(AppSettings(SettingsEnum.ScreenPermOn.name, false))
            false
        } else {
            screen
        }
    }

    fun setScreenPermOnSetting(value: Boolean) {
        upsertSettings(AppSettings(SettingsEnum.ScreenPermOn.name, value))
    }

    private fun checkMealViewSetting(settingsList: List<AppSettings>) {
        val mealViewSetting =
            settingsList.find { it.settingsName == SettingsEnum.ViewMealsAsLists.name }

        if (mealViewSetting == null) {
            _mealViewSetting.value = null
        } else if (mealViewSetting.value) {
            _mealViewSetting.value = MealListOptions.List
        } else {
            _mealViewSetting.value = MealListOptions.Images
        }

    }

    fun saveMealViewSettings(viewOptions: MealListOptions) {
        if (viewOptions == MealListOptions.List) {
            upsertSettings(AppSettings("ViewMealsAsLists", true))
        } else {
            upsertSettings(AppSettings(SettingsEnum.ViewMealsAsLists.name, false))
        }
    }

    fun saveAPIMealonFABClick() {
        if (apiMealToBeSaved.value != null) {
            viewModelScope.launch {
                repository.upsertMeal(apiMealToBeSaved.value!!)
            }
        }
    }

    fun filterForReconciliation(listToAdd: List<Food>): Map<String, List<Grocery>> {
        val ingredientMap: MutableMap<String, MutableList<Grocery>> = mutableMapOf()
        val filteredIngredientMap: MutableMap<String, MutableList<Grocery>> = mutableMapOf()

        for (grocery in _groceryList.value) {
            ingredientMap[grocery.name] = mutableListOf(grocery)
        }

        for (food in listToAdd) {
            if (ingredientMap.containsKey(food.name)) {
                ingredientMap[food.name]!!.add(food.foodToGrocery())
            } else {
                ingredientMap[food.name] = mutableListOf(food.foodToGrocery())
            }
        }

        ingredientMap.forEach { (key, value) ->
            if (value.size > 1) {
                filteredIngredientMap[key] = value
            } else if (!_groceryList.value.contains(value[0])) {
                viewModelScope.launch { insertGrocery(value[0]) }
            }
        }
        return filteredIngredientMap
    }
}