package com.kunle.aisle9b.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kunle.aisle9b.models.*
import com.kunle.aisle9b.navigation.BottomNavItem
import com.kunle.aisle9b.navigation.GroceryScreens
import com.kunle.aisle9b.repository.ShoppingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShoppingViewModel @Inject constructor(private val repository: ShoppingRepository) :
    ViewModel() {

    private var _foodList = MutableStateFlow<List<Food>>(emptyList())
    private var _groceryList = MutableStateFlow<List<Food>>(emptyList())
    private var _mealList = MutableStateFlow<List<Meal>>(emptyList())
    private var _settingsList = MutableStateFlow<List<Settings>>(emptyList())
    private var _mealWithIngredientsList = MutableStateFlow<List<MealWithIngredients>>(emptyList())
    val foodList = _foodList.asStateFlow()
    val groceryList = _groceryList.asStateFlow()
    val mealList = _mealList.asStateFlow()
    val settingsList = _settingsList.asStateFlow()
    val mealWithIngredientsList = _mealWithIngredientsList.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllFood().distinctUntilChanged().collect { listOfFood ->
                if (listOfFood.isNotEmpty()) {
                    _foodList.value = listOfFood
                }
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllGroceries().distinctUntilChanged().collect { listOfGroceries ->
                if (listOfGroceries.isNotEmpty()) {
                    _groceryList.value = listOfGroceries
                }
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllMeals().distinctUntilChanged().collect { listOfMeals ->
                if (listOfMeals.isNotEmpty()) {
                    _mealList.value = listOfMeals
                }
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllSettings().distinctUntilChanged().collect { listOfSettings ->
                if (listOfSettings.isNotEmpty()) {
                    _settingsList.value = listOfSettings
                }
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllMealsWithIngredients().distinctUntilChanged().collect { listOfMWL ->
                if (listOfMWL.isNotEmpty()) {
                    _mealWithIngredientsList.value = listOfMWL
                }
            }
        }
    }

    fun insertFood(food: Food) = viewModelScope.launch { repository.insertFood(food) }
    fun deleteFood(food: Food) = viewModelScope.launch { repository.deleteFood(food) }
    fun updateFood(food: Food) = viewModelScope.launch { repository.updateFood(food) }
    fun deleteAllFood() = viewModelScope.launch { repository.deleteAllFood() }
    fun getFood(name: String): Flow<Food> {
        return flow<Food> { viewModelScope.launch { repository.getFood(name).collect() } }
    }

    suspend fun insertMeal(meal: Meal) = viewModelScope.launch { repository.insertMeal(meal) }
    suspend fun deleteMeal(meal: Meal) = viewModelScope.launch { repository.deleteMeal(meal) }
    suspend fun updateMeal(meal: Meal) = viewModelScope.launch { repository.updateMeal(meal) }
    suspend fun deleteAllMeals() = viewModelScope.launch { repository.deleteAllMeals() }
    suspend fun getMeal(name: String): Flow<Meal> {
        return flow { viewModelScope.launch { repository.getMeal(name) } }
    }

    suspend fun insertSettings(settings: Settings) =
        viewModelScope.launch { repository.insertSettings(settings) }

    suspend fun deleteSettings(settings: Settings) =
        viewModelScope.launch { repository.deleteSettings(settings) }

    suspend fun updateSettings(settings: Settings) =
        viewModelScope.launch { repository.updateSettings(settings) }

    suspend fun deleteAllSettings() = viewModelScope.launch { repository.deleteAllSettings() }
    suspend fun checkSetting(name: String): Flow<Int> {
        return flow { viewModelScope.launch { repository.checkSetting(name) } }
    }

    suspend fun insertPair(crossRef: MealFoodMap) =
        viewModelScope.launch { repository.insertPair(crossRef) }

    suspend fun deletePair(crossRef: MealFoodMap) =
        viewModelScope.launch { repository.deletePair(crossRef) }

    suspend fun updatePair(crossRef: MealFoodMap) =
        viewModelScope.launch { repository.updatePair(crossRef) }

    suspend fun deleteSpecificMealIngredients(mealId: Long) =
        viewModelScope.launch { repository.deleteSpecificMealIngredients(mealId) }

    suspend fun deleteAllMealWithIngredients() =
        viewModelScope.launch { repository.deleteAllMealWithIngredients() }

    suspend fun getSpecificMealWithIngredients(mealId: Long): Flow<MealWithIngredients> {
        return flow {
            viewModelScope.launch { repository.getSpecificMealWithIngredients(mealId) }
        }
    }


    val screenList = listOf(
        BottomNavItem(
            name = "Grocery List",
            route = GroceryScreens.GroceryScreen.name,
            icon = Icons.Filled.List
        ),
        BottomNavItem(
            name = "Meals",
            route = GroceryScreens.MealScreen.name,
            icon = Icons.Filled.Delete
        ),
        BottomNavItem(
            name = "Settings",
            route = GroceryScreens.SettingsScreen.name,
            icon = Icons.Filled.Settings
        )
    )

}