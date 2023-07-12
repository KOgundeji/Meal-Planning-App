package com.kunle.aisle9b.repositories.general

import com.kunle.aisle9b.models.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeGeneralRepository : GeneralRepository {
    private val groceryList = mutableListOf<Meal>()
    private val foodNameList = mutableListOf<AppSettings>()
    private val observableGroceryList = MutableStateFlow<List<Meal>>(groceryList)
    private val observableFoodNameList = MutableStateFlow<List<AppSettings>>(foodNameList)

    override suspend fun upsertFood(food: Food) {

    }

    override suspend fun deleteFood(food: Food) {

    }

    override suspend fun insertGrocery(grocery: Grocery) {

    }

    override suspend fun insertPair(crossRef: MealFoodMap) {

    }

    override suspend fun upsertInstruction(instruction: Instruction) {

    }

    override suspend fun upsertMeal(meal: Meal) {

    }

    override suspend fun deleteMeal(meal: Meal) {

    }

    override suspend fun upsertSettings(settings: AppSettings) {

    }

    override fun getAllMeals(): Flow<List<Meal>> {
        return observableGroceryList
    }

    override fun getAllSettings(): Flow<List<AppSettings>> {
        return observableFoodNameList
    }
}