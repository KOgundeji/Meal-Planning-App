package com.kunle.aisle9b.screens.customLists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kunle.aisle9b.models.*
import com.kunle.aisle9b.repositories.BasicRepositoryFunctions
import com.kunle.aisle9b.repositories.customLists.CustomListRepository
import com.kunle.aisle9b.util.CustomListGateState
import com.kunle.aisle9b.util.IngredientResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class CustomListVM @Inject constructor(private val repository: CustomListRepository) : ViewModel(),
    BasicRepositoryFunctions {

    private val _allGroceryLists = MutableStateFlow<List<GroceryList>>(emptyList())
    private val _visibleGroceryLists = MutableStateFlow<List<GroceryList>>(emptyList())
    private val _groceriesOfCustomLists = MutableStateFlow<List<ListWithGroceries>>(emptyList())
    private val _gateState = MutableStateFlow<CustomListGateState>(CustomListGateState.Loading)
    private val _ingredientState =
        MutableStateFlow<IngredientResponse>(IngredientResponse.Neutral)

    val allGroceryLists = _allGroceryLists.asStateFlow()
    val visibleGroceryLists = _visibleGroceryLists.asStateFlow()
    val groceriesOfCustomLists = _groceriesOfCustomLists.asStateFlow()
    val gateState = _gateState.asStateFlow()
    val ingredientState = _ingredientState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllLists().distinctUntilChanged().collect { lists ->
                _allGroceryLists.value = lists
                _visibleGroceryLists.value = lists.filter { it.visible }
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllListWithGroceries().distinctUntilChanged().collect {
                _groceriesOfCustomLists.value = it
            }
        }
    }

    suspend fun getBrandNewGroceryList() {
        viewModelScope.launch {
            try {
                val listId = repository.insertList(GroceryList.createBlank())
                _gateState.value =
                    CustomListGateState.Success(groceryList = GroceryList.createBlank(listId))
            } catch (e: Exception) {
                _gateState.value = CustomListGateState.Error(exception = e)
            }
        }
    }

    fun setGateStateToSuccess(groceryList: GroceryList) {
        _gateState.value = CustomListGateState.Success(groceryList = groceryList)
    }

    suspend fun updateFoodList(food: Food?, listId: Long) {
        if (food != null) {
            _ingredientState.value = IngredientResponse.Loading
            viewModelScope.launch {
                try {
                    val foodId = repository.insertFood(food)
                    repository.insertPair(ListFoodMap(listId, foodId))
                    _ingredientState.value = IngredientResponse.Success(foodId = foodId)
                } catch (e: Exception) {
                    _ingredientState.value = IngredientResponse.Error(exception = e)
                }
            }
        }
    }

    fun insertList(list: GroceryList) {
        viewModelScope.launch { repository.insertList(list) }
    }

    fun deleteList(list: GroceryList) = viewModelScope.launch { repository.deleteList(list) }

    fun insertPair(crossRef: ListFoodMap) =
        viewModelScope.launch { repository.insertPair(crossRef) }

    fun deletePair(crossRef: ListFoodMap) =
        viewModelScope.launch { repository.deletePair(crossRef) }

    fun deleteSpecificListWithGroceries(listId: Long) =
        viewModelScope.launch { repository.deleteSpecificListWithGroceries(listId) }

    fun updateName(obj: GroceryListNameUpdate) =
        viewModelScope.launch { repository.updateName(obj) }

    fun updateVisibility(obj: GroceryListVisibilityUpdate) =
        viewModelScope.launch { repository.updateVisibility(obj) }

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

    override suspend fun deleteGrocery(grocery: Grocery) {
        viewModelScope.launch { repository.deleteGrocery(grocery) }
    }

    override suspend fun insertGrocery(grocery: Grocery) {
        viewModelScope.launch { repository.insertGrocery(grocery) }
    }

    override suspend fun deleteGroceryByName(name: String) {
        viewModelScope.launch { repository.deleteGroceryByName(name) }
    }

    enum class GroceryListScreens {
        Add,
        Edit
    }
}