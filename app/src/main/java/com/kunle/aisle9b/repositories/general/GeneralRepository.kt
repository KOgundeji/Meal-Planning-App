package com.kunle.aisle9b.repositories.general

import com.kunle.aisle9b.models.*
import kotlinx.coroutines.flow.Flow

interface GeneralRepository {
    //CRUD
    suspend fun upsertFood(food: Food)
    suspend fun deleteFood(food: Food)

    suspend fun insertGrocery(grocery: Grocery)
    suspend fun upsertGrocery(grocery: Grocery)

    suspend fun insertPair(crossRef: MealFoodMap)

    suspend fun upsertInstruction(instruction: Instruction)

    suspend fun upsertMeal(meal: Meal)
    suspend fun deleteMeal(meal: Meal)

    suspend fun upsertSettings(settings: AppSettings)

    //Get all
    fun getAllMeals(): Flow<List<Meal>>
    fun getAllSettings(): Flow<List<AppSettings>>
    fun getAllGroceries(): Flow<List<Grocery>>
}