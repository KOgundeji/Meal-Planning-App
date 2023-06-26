package com.kunle.aisle9b.repositories.groceries

import com.kunle.aisle9b.models.Grocery
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeGroceryRepository : GroceryRepository {
    private val groceryList = mutableListOf<Grocery>()
    private val foodNameList = mutableListOf<String>()
    private val observableGroceryList = MutableStateFlow<List<Grocery>>(groceryList)
    private val observableFoodNameList = MutableStateFlow<List<String>>(foodNameList)

    init {
        groceryList.add(Grocery(3,"strawberries", quantity = "four"))
        groceryList.add(Grocery(4,"bananas", quantity = "8"))
        groceryList.add(Grocery(13,"potatoes", quantity = "3"))
        groceryList.add(Grocery(99,"kidney beans", quantity = "2 cans"))
        groceryList.add(Grocery(1,"almonds", quantity = "2 lbs"))
    }

    override suspend fun deleteGrocery(grocery: Grocery) {
        groceryList.remove(grocery)
    }

    override fun getAllGroceries(): Flow<List<Grocery>> {
        return observableGroceryList
    }

    override fun getAllFoodNames(): Flow<List<String>> {
        return observableFoodNameList
    }
}