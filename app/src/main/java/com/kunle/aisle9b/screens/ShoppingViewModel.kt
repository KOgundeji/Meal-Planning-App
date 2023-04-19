package com.kunle.aisle9b.screens

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
import java.util.*
import javax.inject.Inject
@HiltViewModel
class ShoppingViewModel @Inject constructor(private val repository: ShoppingRepository) :
    ViewModel() {

    val mealDeleteList: MutableList<Meal> = mutableListOf()
    val groceryListDeleteList: MutableList<GroceryList> = mutableListOf()

    var filteredList = mutableStateOf<List<GroceryList>>(emptyList())

    val tempIngredientList = mutableStateListOf<Food>()
    val tempGroceryList = mutableStateListOf<Food>()
    var darkModeSetting = mutableStateOf(false)
    var keepScreenOn = mutableStateOf(false)
    var categoriesOn = mutableStateOf(false)
    val groceryBadgeCount = mutableStateOf(0)

    private var _foodList = MutableStateFlow<List<Food>>(emptyList())
    private var _groceryList = MutableStateFlow<List<Food>>(emptyList())
    private var _premadeLists = MutableStateFlow<List<GroceryList>>(emptyList())
    private var _mealList = MutableStateFlow<List<Meal>>(emptyList())
    private var _settingsList = MutableStateFlow<List<AppSettings>>(emptyList())
    private var _listWithGroceriesList = MutableStateFlow<List<ListWithGroceries>>(emptyList())
    private var _mealWithIngredientsList = MutableStateFlow<List<MealWithIngredients>>(emptyList())
    val foodList = _foodList.asStateFlow()
    val groceryList = _groceryList.asStateFlow()
    val premadeLists = _premadeLists.asStateFlow()
    val mealList = _mealList.asStateFlow()
    val settingsList = _settingsList.asStateFlow()
    val listsWithGroceries = _listWithGroceriesList.asStateFlow()
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
            repository.getAllLists().distinctUntilChanged().collect { premadeLists ->
                if (premadeLists.isNotEmpty()) {
                    _premadeLists.value = premadeLists
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
            repository.getAllListWithGroceries().distinctUntilChanged().collect { listOfLWG ->
                if (listOfLWG.isNotEmpty()) {
                    _listWithGroceriesList.value = listOfLWG
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

    fun insertList(list: GroceryList) = viewModelScope.launch { repository.insertList(list) }
    fun deleteList(list: GroceryList) = viewModelScope.launch { repository.deleteList(list) }
    fun updateList(list: GroceryList) = viewModelScope.launch { repository.updateList(list) }
    fun deleteAllLists() = viewModelScope.launch { repository.deleteAllLists() }
    suspend fun getLists(name: String): GroceryList {
        return viewModelScope.async {
            repository.getLists(name)
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

    fun insertSettings(settings: AppSettings) =
        viewModelScope.launch { repository.insertSettings(settings) }

    fun deleteSettings(settings: AppSettings) =
        viewModelScope.launch { repository.deleteSettings(settings) }

    fun updateSettings(settings: AppSettings) =
        viewModelScope.launch { repository.updateSettings(settings) }

    fun deleteAllSettings() = viewModelScope.launch { repository.deleteAllSettings() }
    suspend fun checkSetting(name: String): Int {
        return viewModelScope.async {
            repository.checkSetting(name)
        }.await()
    }

    fun insertPair(crossRef: ListFoodMap) =
        viewModelScope.launch { repository.insertPair(crossRef) }

    fun deletePair(crossRef: ListFoodMap) =
        viewModelScope.launch { repository.deletePair(crossRef) }

    fun updatePair(crossRef: ListFoodMap) =
        viewModelScope.launch { repository.updatePair(crossRef) }

    fun deleteSpecificListWithGroceries(listId: UUID) =
        viewModelScope.launch { repository.deleteSpecificGroceryList(listId) }

    fun deleteAllListWithGroceries() =
        viewModelScope.launch { repository.deleteAllListWithGroceries() }

    suspend fun getSpecificListWithGroceries(listId: Long): ListWithGroceries {
        return viewModelScope.async {
            repository.getSpecificListWithGroceries(listId)
        }.await()
    }

    fun insertPair(crossRef: MealFoodMap) =
        viewModelScope.launch { repository.insertPair(crossRef) }

    fun deletePair(crossRef: MealFoodMap) =
        viewModelScope.launch { repository.deletePair(crossRef) }

    fun updatePair(crossRef: MealFoodMap) =
        viewModelScope.launch { repository.updatePair(crossRef) }

    fun deleteSpecificMealIngredients(mealId: UUID) =
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
            icon = Icons.Filled.Checklist,
            badgeCount = groceryBadgeCount.value
        ),
        BottomNavItem(
            name = "Pre-made Lists",
            route = GroceryScreens.PremadeListScreen.name,
            icon = Icons.Filled.PlaylistAdd
        ),
        BottomNavItem(
            name = "Meals",
            route = GroceryScreens.MealScreen.name,
            icon = Icons.Filled.DinnerDining
        )
    )

}