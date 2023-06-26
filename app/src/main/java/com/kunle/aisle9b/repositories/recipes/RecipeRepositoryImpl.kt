package com.kunle.aisle9b.repositories.recipes

import com.kunle.aisle9b.api.RecipeAPI
import com.kunle.aisle9b.api.apiModels.recipeModels.Recipe
import com.kunle.aisle9b.data.GroceryDao
import com.kunle.aisle9b.data.MealDao
import com.kunle.aisle9b.data.MealWithIngredientsDao
import com.kunle.aisle9b.models.Food
import com.kunle.aisle9b.models.Meal
import com.kunle.aisle9b.models.MealFoodMap
import com.kunle.aisle9b.models.apiModels.instructionModels.Instructions
import com.kunle.aisle9b.models.apiModels.queryModels.RawJSON
import javax.inject.Inject

class RecipeRepositoryImpl @Inject
constructor(
    private val groceryDao: GroceryDao,
    private val mealDao: MealDao,
    private val recipeAPI: RecipeAPI,
    private val mealWithIngredientsDao: MealWithIngredientsDao
) : RecipeRepository {

    override suspend fun upsertMeal(meal: Meal) = mealDao.upsertMeal(meal)
    override suspend fun deleteMeal(meal: Meal) = mealDao.deleteMeal(meal)

    override suspend fun insertPair(crossRef: MealFoodMap) =
        mealWithIngredientsDao.insertPair(crossRef)

    override suspend fun deletePair(crossRef: MealFoodMap) =
        mealWithIngredientsDao.deletePair(crossRef)

    override suspend fun getRecipe(id: Int): Recipe = recipeAPI.getRecipe(id = id)

    override suspend fun getSearchResults(query: String): RawJSON =
        recipeAPI.getSearchResults(query = query)

    override suspend fun getInstructions(id: Int): Instructions =
        recipeAPI.getInstructions(id = id)
}