package com.kunle.aisle9b.screens.customLists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kunle.aisle9b.models.*
import com.kunle.aisle9b.repositories.BasicRepositoryFunctions
import com.kunle.aisle9b.repositories.customLists.CustomListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomListVM @Inject constructor(private val repository: CustomListRepository) : ViewModel(),
    BasicRepositoryFunctions {

    private val _customLists = MutableStateFlow<List<GroceryList>>(emptyList())
    private val _groceriesOfCustomLists = MutableStateFlow<List<ListWithGroceries>>(emptyList())
    val customLists = _customLists.asStateFlow()
    val groceriesOfCustomLists = _groceriesOfCustomLists.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllLists().distinctUntilChanged().collect {
                _customLists.value = it
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllListWithGroceries().distinctUntilChanged().collect {
                _groceriesOfCustomLists.value = it
            }
        }
    }

    fun insertList(list: GroceryList): Long {
        var listId = -1L
        viewModelScope.launch {
            listId = async { repository.insertList(list) }.await()
        }
        return listId
    }

    fun deleteList(list: GroceryList) = viewModelScope.launch { repository.deleteList(list) }
    fun updateList(list: GroceryList) = viewModelScope.launch { repository.updateList(list) }

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

}