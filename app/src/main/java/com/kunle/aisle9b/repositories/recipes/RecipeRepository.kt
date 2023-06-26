package com.kunle.aisle9b.repositories.recipes

import com.kunle.aisle9b.api.apiModels.recipeModels.Recipe
import com.kunle.aisle9b.models.Meal
import com.kunle.aisle9b.models.MealFoodMap
import com.kunle.aisle9b.models.apiModels.instructionModels.Instructions
import com.kunle.aisle9b.models.apiModels.queryModels.RawJSON

interface RecipeRepository {
    //CRUD
    suspend fun upsertMeal(meal: Meal)
    suspend fun deleteMeal(meal: Meal)
    suspend fun insertPair(crossRef: MealFoodMap)
    suspend fun deletePair(crossRef: MealFoodMap)

    //Get
    suspend fun getRecipe(id: Int): Recipe
    suspend fun getSearchResults(query: String): RawJSON
    suspend fun getInstructions(id: Int): Instructions
}