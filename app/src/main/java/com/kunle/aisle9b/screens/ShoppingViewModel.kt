package com.kunle.aisle9b.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.mutableStateOf
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
class ShoppingViewModel @Inject constructor(private val repository: ShoppingRepository) :
    ViewModel() {
    var mealDeleteEnabled = mutableStateOf(false)
    val tempFoodList = mutableListOf<Food>()

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
    suspend fun getFood(name: String): Food {
        return viewModelScope.async {
            repository.getFood(name)
        }.await()
    }

    fun insertMeal(meal: Meal) = viewModelScope.launch { repository.insertMeal(meal) }
    fun deleteMeal(meal: Meal) = viewModelScope.launch { repository.deleteMeal(meal) }
    fun updateMeal(meal: Meal) = viewModelScope.launch { repository.updateMeal(meal) }
    fun deleteAllMeals() = viewModelScope.launch { repository.deleteAllMeals() }
    suspend fun getMeal(name: String): Meal {
        return viewModelScope.async {
            repository.getMeal(name)
        }.await()
    }

    fun insertSettings(settings: Settings) =
        viewModelScope.launch { repository.insertSettings(settings) }

    fun deleteSettings(settings: Settings) =
        viewModelScope.launch { repository.deleteSettings(settings) }

    fun updateSettings(settings: Settings) =
        viewModelScope.launch { repository.updateSettings(settings) }

    fun deleteAllSettings() = viewModelScope.launch { repository.deleteAllSettings() }
    suspend fun checkSetting(name: String): Int {
        return viewModelScope.async {
            repository.checkSetting(name)
        }.await()
    }

    fun insertPair(crossRef: MealFoodMap) =
        viewModelScope.launch { repository.insertPair(crossRef) }

    fun deletePair(crossRef: MealFoodMap) =
        viewModelScope.launch { repository.deletePair(crossRef) }

    fun updatePair(crossRef: MealFoodMap) =
        viewModelScope.launch { repository.updatePair(crossRef) }

    fun deleteSpecificMealIngredients(mealId: Long) =
        viewModelScope.launch { repository.deleteSpecificMealIngredients(mealId) }

    fun deleteAllMealWithIngredients() =
        viewModelScope.launch { repository.deleteAllMealWithIngredients() }

    suspend fun getSpecificMealWithIngredients(mealId: Long): MealWithIngredients {
        return viewModelScope.async {
            repository.getSpecificMealWithIngredients(mealId)
        }.await()
    }


    val screenList = listOf(
        BottomNavItem(
            name = "Grocery List",
            route = GroceryScreens.ListScreen.name,
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