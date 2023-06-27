package com.kunle.aisle9b.repositories

import com.kunle.aisle9b.models.*
import com.kunle.aisle9b.repositories.general.GeneralRepository
import kotlinx.coroutines.flow.Flow

class FakeShoppingRepository: GeneralRepository {
    override suspend fun upsertFood(food: Food) {

    }

    override suspend fun deleteFood(food: Food) {
        TODO("Not yet implemented")
    }

    override suspend fun insertGrocery(grocery: Grocery) {
        TODO("Not yet implemented")
    }

    override suspend fun insertPair(crossRef: MealFoodMap) {
        TODO("Not yet implemented")
    }

    override suspend fun upsertInstruction(instruction: Instruction) {

    }

    override suspend fun upsertMeal(meal: Meal) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteMeal(meal: Meal) {
        TODO("Not yet implemented")
    }

    override suspend fun upsertSettings(settings: AppSettings) {
        TODO("Not yet implemented")
    }

    override fun getAllMeals(): Flow<List<Meal>> {
        TODO("Not yet implemented")
    }

    override fun getAllSettings(): Flow<List<AppSettings>> {
        TODO("Not yet implemented")
    }


}