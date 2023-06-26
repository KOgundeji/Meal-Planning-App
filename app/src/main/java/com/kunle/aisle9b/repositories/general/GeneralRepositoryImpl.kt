package com.kunle.aisle9b.repositories.general

import com.kunle.aisle9b.data.*
import com.kunle.aisle9b.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class GeneralRepositoryImpl @Inject constructor(
    private val groceryDao: GroceryDao,
    private val mealWithIngredientsDao: MealWithIngredientsDao,
    private val mealDao: MealDao,
    private val instructionDao: InstructionDao,
    private val settingsDao: SettingsDao
) : GeneralRepository {

    //CRUD
    override suspend fun upsertFood(food: Food) = groceryDao.upsertFood(food)
    override suspend fun deleteFood(food: Food) = groceryDao.deleteFood(food)
    override suspend fun insertGrocery(grocery: Grocery) = groceryDao.insertGrocery(grocery)
    override suspend fun insertPair(crossRef: MealFoodMap) =
        mealWithIngredientsDao.insertPair(crossRef)

    override suspend fun upsertInstruction(instruction: Instruction) =
        instructionDao.upsertInstruction(instruction)

    override suspend fun upsertMeal(meal: Meal) = mealDao.upsertMeal(meal)
    override suspend fun deleteMeal(meal: Meal) = mealDao.deleteMeal(meal)
    override fun getAllMeals(): Flow<List<Meal>> =
        mealDao.getAllMeals().flowOn(Dispatchers.IO).conflate()

    override suspend fun upsertSettings(settings: AppSettings) =
        settingsDao.upsertSettings(settings)

    //Get all
    override fun getAllSettings(): Flow<List<AppSettings>> =
        settingsDao.getAllSettings().flowOn(Dispatchers.IO).conflate()
}