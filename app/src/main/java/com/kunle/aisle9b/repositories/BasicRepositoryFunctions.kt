package com.kunle.aisle9b.repositories

import com.kunle.aisle9b.models.Food
import com.kunle.aisle9b.models.Grocery

interface BasicRepositoryFunctions {
    suspend fun insertFood(food: Food): Long
    suspend fun insertGrocery(grocery: Grocery)
    suspend fun upsertGrocery(grocery: Grocery)
    suspend fun upsertFood(food: Food)
    suspend fun deleteFood(food: Food)
    suspend fun deleteGrocery(grocery: Grocery)
    suspend fun deleteGroceryByName(name: String)

}