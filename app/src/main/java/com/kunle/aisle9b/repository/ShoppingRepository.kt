package com.kunle.aisle9b.repository

import com.kunle.aisle9b.api.RandomRecipeAPI
import com.kunle.aisle9b.api.RecipeAPI
import com.kunle.aisle9b.data.*
import com.kunle.aisle9b.models.*
import com.kunle.aisle9b.models.apiModels.DataOrException
import com.kunle.aisle9b.models.apiModels.recipeModels.Recipe
import com.kunle.aisle9b.models.apiModels.recipeModels.RecipeRawAPIData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import java.util.*
import javax.inject.Inject


class ShoppingRepository @Inject constructor(
    private val foodDao: FoodDao,
    private val listDao: ListDao,
    private val mealDao: MealDao,
    private val settingsDao: SettingsDao,
    private val listWithGroceriesDao: ListWithGroceriesDao,
    private val mealWithIngredientsDao: MealWithIngredientsDao,
    private val recipeAPI: RecipeAPI,
    private val randomRecipeAPI: RandomRecipeAPI
) {

    suspend fun getRecipes(id: Int): DataOrException<Recipe, Boolean, Exception> {
        val response =
            try {
                recipeAPI.getRecipes(id = id)
            } catch (e: Exception) {
                return DataOrException(e = e)
            }
        return DataOrException(data = response)
    }

    suspend fun getRandomRecipes(tags: String): DataOrException<RecipeRawAPIData, Boolean, Exception> {
        val response =
            try {
                randomRecipeAPI.getRandomRecipes(tags = tags)
            } catch (e: Exception) {
                return DataOrException(e = e)
            }
        return DataOrException(data = response)
    }

    suspend fun insertFood(food: Food) = foodDao.insertFood(food)
    suspend fun deleteFood(food: Food) = foodDao.deleteFood(food)
    suspend fun updateFood(food: Food) = foodDao.updateFood(food)
    suspend fun deleteAllFood() = foodDao.deleteAllFood()
    suspend fun getFood(name: String) = foodDao.getFood(name)
    fun getAllGroceries(): Flow<List<Food>> =
        foodDao.getAllGroceries().flowOn(Dispatchers.IO).conflate()

    fun getAllFood(): Flow<List<Food>> = foodDao.getAllFood().flowOn(Dispatchers.IO).conflate()

    suspend fun insertList(list: GroceryList) = listDao.insertList(list)
    suspend fun deleteList(list: GroceryList) = listDao.deleteList(list)
    suspend fun updateList(list: GroceryList) = listDao.updateList(list)
    suspend fun deleteAllLists() = listDao.deleteAllLists()
    suspend fun getLists(name: String) = listDao.getList(name)
    fun getAllLists(): Flow<List<GroceryList>> =
        listDao.getAllLists().flowOn(Dispatchers.IO).conflate()

    suspend fun insertMeal(meal: Meal) = mealDao.insertMeal(meal)
    suspend fun deleteMeal(meal: Meal) = mealDao.deleteMeal(meal)
    suspend fun updateMeal(meal: Meal) = mealDao.updateMeal(meal)
    suspend fun deleteAllMeals() = mealDao.deleteAllMeals()
    suspend fun getMeal(name: String) = mealDao.getMeal(name)
    fun getAllMeals(): Flow<List<Meal>> = mealDao.getAllMeals().flowOn(Dispatchers.IO).conflate()

    suspend fun insertSettings(settings: AppSettings) = settingsDao.insertSettings(settings)
    suspend fun deleteSettings(settings: AppSettings) = settingsDao.deleteSettings(settings)
    suspend fun updateSettings(settings: AppSettings) = settingsDao.updateSettings(settings)
    suspend fun deleteAllSettings() = settingsDao.deleteAllSettings()
    suspend fun checkSetting(name: String): Int = settingsDao.checkSetting(name)
    fun getAllSettings(): Flow<List<AppSettings>> =
        settingsDao.getAllSettings().flowOn(Dispatchers.IO).conflate()

    suspend fun insertPair(crossRef: ListFoodMap) = listWithGroceriesDao.insertPair(crossRef)
    suspend fun deletePair(crossRef: ListFoodMap) = listWithGroceriesDao.deletePair(crossRef)
    suspend fun updatePair(crossRef: ListFoodMap) = listWithGroceriesDao.updatePair(crossRef)
    suspend fun deleteSpecificGroceryList(listId: UUID) =
        listWithGroceriesDao.deleteSpecificGroceryList(listId)

    suspend fun deleteAllListWithGroceries() =
        listWithGroceriesDao.deleteAllGroceryLists()

    suspend fun getSpecificListWithGroceries(listId: Long): ListWithGroceries =
        listWithGroceriesDao.getSpecificListWithGroceries(listId)

    fun getAllListWithGroceries(): Flow<List<ListWithGroceries>> =
        listWithGroceriesDao.getAllListWithGroceries().flowOn(Dispatchers.IO).conflate()

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