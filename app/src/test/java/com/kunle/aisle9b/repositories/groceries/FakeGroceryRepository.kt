package com.kunle.aisle9b.repositories.groceries

import com.kunle.aisle9b.models.Food
import com.kunle.aisle9b.models.Grocery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn

class FakeGroceryRepository : GroceryRepository {
    private val groceryList = mutableListOf<Grocery>()
    private val foodNameList = mutableListOf<String>()
    private val observableGroceryList = MutableStateFlow<List<Grocery>>(groceryList)
    private val observableFoodNameList = MutableStateFlow<List<String>>(foodNameList)

    init {
        groceryList.add(Grocery(3, "strawberries", "four"))
        groceryList.add(Grocery(4, "bananas", "8"))
        groceryList.add(Grocery(13, "potatoes", "3"))
        groceryList.add(Grocery(99, "kidney beans", "2 cans"))
        groceryList.add(Grocery(1, "almonds", "2 lbs"))

        foodNameList.add("Strawberries")
        foodNameList.add("Cherries")
        foodNameList.add("Plantain")
    }

    override suspend fun insertFood(food: Food): Long {
        return 1L
    }

    override suspend fun insertGrocery(grocery: Grocery) {
        groceryList.add(grocery)
    }

    override suspend fun upsertFood(food: Food) {
    }

    override suspend fun upsertGrocery(grocery: Grocery) {
        if (!groceryList.contains(grocery)) {
            groceryList.add(grocery)
        }
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