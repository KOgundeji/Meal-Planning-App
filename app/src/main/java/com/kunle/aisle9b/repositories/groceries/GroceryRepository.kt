package com.kunle.aisle9b.repositories.groceries

import com.kunle.aisle9b.models.Food
import com.kunle.aisle9b.models.Grocery
import kotlinx.coroutines.flow.Flow

interface GroceryRepository {

    //CRUD
    suspend fun insertFood(food: Food): Long
    suspend fun insertGrocery(grocery: Grocery)
    suspend fun upsertFood(food: Food)
    suspend fun upsertGrocery(grocery: Grocery)
    suspend fun deleteFood(food: Food)
    suspend fun deleteGrocery(grocery: Grocery)
    suspend fun deleteGroceryByName(name: String)
    suspend fun updateGlobalFoodCategories(name: String, category: String)

    //Get all
    fun getAllGroceries(): Flow<List<Grocery>>
    fun getAllFoodNames(): Flow<List<String>>
}