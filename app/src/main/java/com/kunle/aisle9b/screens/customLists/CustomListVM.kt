package com.kunle.aisle9b.screens.customLists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kunle.aisle9b.models.GroceryList
import com.kunle.aisle9b.models.ListFoodMap
import com.kunle.aisle9b.models.ListWithGroceries
import com.kunle.aisle9b.repository.ShoppingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CustomListVM @Inject constructor(private val repository: ShoppingRepository) : ViewModel() {

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

    fun insertList(list: GroceryList) = viewModelScope.launch { repository.insertList(list) }
    fun deleteList(list: GroceryList) = viewModelScope.launch { repository.deleteList(list) }
    fun updateList(list: GroceryList) = viewModelScope.launch { repository.updateList(list) }
    fun deleteAllLists() = viewModelScope.launch { repository.deleteAllLists() }
    suspend fun getLists(name: String): GroceryList {
        return withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
            repository.getLists(name)
        }
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
        return withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
            repository.getSpecificListWithGroceries(listId)
        }
    }

}