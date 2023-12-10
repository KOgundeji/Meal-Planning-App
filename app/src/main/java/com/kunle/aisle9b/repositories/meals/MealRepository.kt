package com.kunle.aisle9b.repositories.meals

import com.kunle.aisle9b.api.apiModels.recipeModels.Recipe
import com.kunle.aisle9b.models.*
import com.kunle.aisle9b.models.apiModels.instructionModels.Instructions
import kotlinx.coroutines.flow.Flow

interface MealRepository {
    //API
    suspend fun getRecipe(id: Int): Recipe
    suspend fun getInstructions(id: Int): Instructions

    //Partial Room entries
    suspend fun updateName(obj: MealNameUpdate)
    suspend fun updatePic(obj: MealPicUpdate)
    suspend fun updateServingSize(obj: MealServingSizeUpdate)
    suspend fun updateNotes(obj: MealNotesUpdate)
    suspend fun updateVisibility(obj: MealVisibilityUpdate)

    //CRUD
    suspend fun insertMeal(meal: Meal): Long
    suspend fun upsertMeal(meal: Meal)
    suspend fun deleteMeal(meal: Meal)
    suspend fun insertGrocery(grocery: Grocery)
    suspend fun upsertGrocery(grocery: Grocery)
    suspend fun deleteGrocery(grocery: Grocery)
    suspend fun deleteGroceryByName(name: String)
    suspend fun insertFood(food: Food): Long
    suspend fun upsertFood(food: Food)
    suspend fun deleteFood(food: Food)
    suspend fun insertPair(crossRef: MealFoodMap)
    suspend fun deletePair(crossRef: MealFoodMap)

    suspend fun deleteSpecificMealWithIngredients(mealId: Long)

    suspend fun upsertInstruction(instruction: Instruction)
    suspend fun deleteInstruction(instruction: Instruction)

    suspend fun updateGlobalFoodCategories(foodName: String, newCategory: String)
    suspend fun updateGlobalGroceryCategories(groceryName: String, newCategory: String)

    //Get All
    fun getAllMeals(): Flow<List<Meal>>
    fun getAllInstructions(): Flow<List<Instruction>>
    fun getAllMealsWithIngredients(): Flow<List<MealWithIngredients>>
}