package com.kunle.aisle9b.repositories.groceries

import com.kunle.aisle9b.models.Food
import com.kunle.aisle9b.models.Grocery
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeGroceryRepository : GroceryRepository {
    private val groceryList = mutableListOf<Grocery>()
    private val foodNameList = mutableListOf<String>()
    private val observableGroceryList = MutableStateFlow<List<Grocery>>(groceryList)
    private val observableFoodNameList = MutableStateFlow<List<String>>(foodNameList)

    override suspend fun insertFood(food: Food): Long {
        return 1L
    }

    override suspend fun insertGrocery(grocery: Grocery) {
        groceryList.add(grocery)
    }

    override suspend fun upsertFood(food: Food) {
    }

    override suspend fun upsertGrocery(grocery: Grocery) {
        val existingGrocery = groceryList.find { it.groceryId == grocery.groceryId }
        if (groceryList.contains(existingGrocery)) {
            groceryList.remove(existingGrocery)
        }
        groceryList.add(grocery)
    }

    override suspend fun deleteFood(food: Food) {
    }

    override suspend fun deleteGrocery(grocery: Grocery) {
        groceryList.remove(grocery)
    }

    override suspend fun deleteGroceryByName(name: String) {
        val groceryToRemove = groceryList.find { it.name == name }
        if (groceryToRemove != null) {
            groceryList.remove(groceryToRemove)
        }
    }

    override suspend fun updateGlobalFoodCategories(name: String, category: String) {
    }

    override fun getAllGroceries(): Flow<List<Grocery>> {
        return observableGroceryList
    }

    override fun getAllFoodNames(): Flow<List<String>> {
        return observableFoodNameList
    }

}