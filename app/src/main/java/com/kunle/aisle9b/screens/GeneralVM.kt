package com.kunle.aisle9b.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kunle.aisle9b.TopBarOptions
import com.kunle.aisle9b.models.AppSettings
import com.kunle.aisle9b.models.Food
import com.kunle.aisle9b.models.Grocery
import com.kunle.aisle9b.models.Instruction
import com.kunle.aisle9b.models.Meal
import com.kunle.aisle9b.models.MealFoodMap
import com.kunle.aisle9b.models.SettingsEnum
import com.kunle.aisle9b.navigation.GroceryScreens
import com.kunle.aisle9b.repositories.general.GeneralRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GeneralVM @Inject constructor(private val repository: GeneralRepository) :
    ViewModel() {

    var topBar by mutableStateOf(TopBarOptions.Default)
        private set
    var source by mutableStateOf(GroceryScreens.GroceryListScreen)
        private set

    var darkModeSetting: Boolean? by mutableStateOf(false)
        private set
    var categoriesSetting by mutableStateOf(true)
        private set
    var screenOnSetting by mutableStateOf(false)
        private set

    private var mealToBeSaved = mutableStateOf<Meal?>(null)
    var apiMealToBeSaved = mutableStateOf<Meal?>(null)

    private var _groceryList = MutableStateFlow<List<Grocery>>(emptyList())
    private val _groceryBadgeCount = MutableStateFlow(0) //not sure where this comes from
    private val _numOfMeals = MutableStateFlow(0)
    val groceryList = _groceryList.asStateFlow()
    val groceryBadgeCount = _groceryBadgeCount.asStateFlow()
    val numOfMeals = _numOfMeals.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllMeals().distinctUntilChanged().collect { listOfMeals ->
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
            }
        }
    }

    fun upsertFood(food: Food) = viewModelScope.launch { repository.upsertFood(food) }
    fun deleteFood(food: Food) = viewModelScope.launch { repository.deleteFood(food) }
    fun insertGrocery(grocery: Grocery) =
        viewModelScope.launch {
            repository.insertGrocery(grocery)
        }

    private fun upsertSettings(settings: AppSettings) =
        viewModelScope.launch { repository.upsertSettings(settings) }

    fun setMealToBeSaved(meal: Meal) {
        mealToBeSaved.value = meal
    }
    fun turnMealVisible() {
        if (mealToBeSaved != null) {

        }

    }

    fun cleanMealList() {

    }

    fun setTopBarOption(value: TopBarOptions) {
        topBar = value
    }

    fun setClickSource(value: GroceryScreens) {
        source = value
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


    fun saveAPIMealonFABClick() {
        if (apiMealToBeSaved.value != null) {
            viewModelScope.launch {
                repository.upsertMeal(apiMealToBeSaved.value!!)
            }
        }
    }

    fun filterForReconciliation(listToAdd: List<Food>): Map<String, List<Food>> {
        val ingredientMap: MutableMap<String, MutableList<Food>> = mutableMapOf()
        val filteredIngredientMap: MutableMap<String, MutableList<Food>> = mutableMapOf()

        for (grocery in _groceryList.value) {
            if (ingredientMap.containsKey(grocery.name)) {
                ingredientMap[grocery.name]!!.add(grocery.groceryToFood())
            } else {
                ingredientMap[grocery.name] = mutableListOf(grocery.groceryToFood())
            }
        }

        for (food in listToAdd) {
            if (ingredientMap.containsKey(food.name)) {
                ingredientMap[food.name]!!.add(food)
            } else {
                ingredientMap[food.name] = mutableListOf(food)
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