package com.kunle.aisle9b.screens.groceries

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kunle.aisle9b.models.Food
import com.kunle.aisle9b.repository.ShoppingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroceryVM @Inject constructor(private val repository: ShoppingRepository) : ViewModel() {

    private val _groceryList = MutableStateFlow<List<Food>>(emptyList())
    private val _foodList = MutableStateFlow<List<String>>(emptyList())
    val groceryList = _groceryList.asStateFlow()
    val foodList = _foodList.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllGroceries().distinctUntilChanged().collect { groceries ->
                _groceryList.value = groceries
            }
        }
        viewModelScope.launch {
            repository.getAllFood().distinctUntilChanged().collect { food ->
                _foodList.value = food.map { it.name }.distinct()
            }
        }
    }


}