package com.kunle.aisle9b.repository

import com.kunle.aisle9b.data.FoodDao
import com.kunle.aisle9b.data.MealDao
import com.kunle.aisle9b.data.MealWithIngredientsDao
import com.kunle.aisle9b.data.SettingsDao
import com.kunle.aisle9b.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import java.util.*
import javax.inject.Inject


class ShoppingRepository @Inject constructor(
    private val foodDao: FoodDao,
    private val mealDao: MealDao,
    private val settingsDao: SettingsDao,
    private val mealWithIngredientsDao: MealWithIngredientsDao
) {
    suspend fun insertFood(food: Food) = foodDao.insertFood(food)
    suspend fun deleteFood(food: Food) = foodDao.deleteFood(food)
    suspend fun updateFood(food: Food) = foodDao.updateFood(food)
    suspend fun deleteAllFood() = foodDao.deleteAllFood()
    suspend fun getFood(name: String) = foodDao.getFood(name)
    fun getAllGroceries(): Flow<List<Food>> =
        foodDao.getAllGroceries().flowOn(Dispatchers.IO).conflate()
    fun getAllFood(): Flow<List<Food>> = foodDao.getAllFood().flowOn(Dispatchers.IO).conflate()

    suspend fun insertMeal(meal: Meal) = mealDao.insertMeal(meal)
    suspend fun deleteMeal(meal: Meal) = mealDao.deleteMeal(meal)
    suspend fun updateMeal(meal: Meal) = mealDao.updateMeal(meal)
    suspend fun deleteAllMeals() = mealDao.deleteAllMeals()
    suspend fun getMeal(name: String) = mealDao.getMeal(name)
    fun getAllMeals(): Flow<List<Meal>> = mealDao.getAllMeals().flowOn(Dispatchers.IO).conflate()

    suspend fun insertSettings(settings: Settings) = settingsDao.insertSettings(settings)
    suspend fun deleteSettings(settings: Settings) = settingsDao.deleteSettings(settings)
    suspend fun updateSettings(settings: Settings) = settingsDao.updateSettings(settings)
    suspend fun deleteAllSettings() = settingsDao.deleteAllSettings()
    suspend fun checkSetting(name: String): Int = settingsDao.checkSetting(name)
    fun getAllSettings(): Flow<List<Settings>> =
        settingsDao.getAllSettings().flowOn(Dispatchers.IO).conflate()

    suspend fun insertPair(crossRef: MealFoodMap) = mealWithIngredientsDao.insertPair(crossRef)
    suspend fun deletePair(crossRef: MealFoodMap) = mealWithIngredientsDao.deletePair(crossRef)
    suspend fun updatePair(crossRef: MealFoodMap) = mealWithIngredientsDao.updatePair(crossRef)
    suspend fun deleteSpecificMealIngredients(mealId: UUID) =
        mealWithIngredientsDao.deleteSpecificMealIngredients(mealId)
    suspend fun deleteAllMealWithIngredients() =
        mealWithIngredientsDao.deleteAllMealWithIngredients()
    suspend fun getSpecificMealWithIngredients(mealId: Long): MealWithIngredients =
        mealWithIngredientsDao.getSpecificMealWithIngredients(mealId)
    fun getAllMealsWithIngredients(): Flow<List<MealWithIngredients>> =
        mealWithIngredientsDao.getAllMealsWithIngredients().flowOn(Dispatchers.IO).conflate()

}