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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class CustomListVM @Inject constructor(private val repository: CustomListRepository) : ViewModel(),
    BasicRepositoryFunctions {

    private val _allGroceryLists = MutableStateFlow<List<GroceryList>>(emptyList())
    val allGroceryLists = _allGroceryLists.asStateFlow()

    private val _visibleGroceryLists = MutableStateFlow<List<GroceryList>>(emptyList())

    private val _groceriesOfCustomLists = MutableStateFlow<List<ListWithGroceries>>(emptyList())
    val groceriesOfCustomLists = _groceriesOfCustomLists.asStateFlow()

    private val _gateState = MutableStateFlow<CustomListGateState>(CustomListGateState.Loading)
    val gateState = _gateState.asStateFlow()

    private val _ingredientState =
        MutableStateFlow<IngredientResponse>(IngredientResponse.Neutral)
    val ingredientState = _ingredientState.asStateFlow()

    private val _filteredCustomLists = MutableStateFlow<List<GroceryList>>(emptyList())
    val filteredCustomList = _filteredCustomLists.asStateFlow()

    private val _searchWord = MutableStateFlow("")
    val searchWord = _searchWord.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllLists().distinctUntilChanged().collect { lists ->
                _allGroceryLists.value = lists
                _visibleGroceryLists.value = lists.filter { it.visible }
                _filteredCustomLists.value = _visibleGroceryLists.value
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

    fun setSearchWord(searchWord: String) {
        _searchWord.update { _ -> searchWord }
        searchForCustomList(searchWord)
    }

    private fun searchForCustomList(searchWord: String) {
        _filteredCustomLists.update {
            if (searchWord != "") {
                _visibleGroceryLists.value.filter {
                    it.listName.contains(
                        searchWord,
                        ignoreCase = true
                    )
                }
            } else {
                _visibleGroceryLists.value
            }
        }
    }

    suspend fun updateFoodList(food: Food?, listId: Long) {
        if (food != null) {
            _ingredientState.value = IngredientResponse.Loading
            viewModelScope.launch {
                try {
                    updateCategories(food.name, food.category)
                    val foodId = repository.insertFood(food)
                    repository.insertPair(ListFoodMap(listId, foodId))
                    _ingredientState.value = IngredientResponse.Success(foodId = foodId)
                } catch (e: Exception) {
                    _ingredientState.value = IngredientResponse.Error(exception = e)
                }
            }
        }
    }

    fun updateFood(food: Food) {
        viewModelScope.launch {
            updateCategories(food.name, food.category)
            upsertFood(food)
        }
    }

    private fun updateCategories(foodGroceryName: String, newCategory: String) {
        viewModelScope.launch {
            repository.updateGlobalFoodCategories(
                foodName = foodGroceryName,
                newCategory = newCategory
            )
            repository.updateGlobalGroceryCategories(
                groceryName = foodGroceryName,
                newCategory = newCategory
            )
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

    override suspend fun upsertGrocery(grocery: Grocery) {
        viewModelScope.launch { repository.upsertGrocery(grocery) }
    }

    override suspend fun deleteGroceryByName(name: String) {
        viewModelScope.launch { repository.deleteGroceryByName(name) }
    }

    enum class GroceryListScreens {
        Add,
        Edit
    }
}