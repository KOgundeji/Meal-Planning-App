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
    suspend fun updatePic(obj: PicUpdate)
    suspend fun updateServingSize(obj: ServingSizeUpdate)
    suspend fun updateNotes(obj: NotesUpdate)

    //CRUD
    suspend fun insertMeal(meal: Meal): Long
    suspend fun upsertMeal(meal: Meal)
    suspend fun deleteMeal(meal: Meal)
    suspend fun insertGrocery(grocery: Grocery)
    suspend fun deleteGroceryByName(name: String)
    suspend fun insertFood(food: Food) : Long
    suspend fun upsertFood(food: Food)
    suspend fun deleteFood(food: Food)
    suspend fun insertPair(crossRef: MealFoodMap)
    suspend fun deletePair(crossRef: MealFoodMap)
    suspend fun updatePair(crossRef: MealFoodMap)

    suspend fun deleteSpecificMealWithIngredients(mealId: Long)

    suspend fun upsertInstruction(instruction: Instruction)
    suspend fun deleteInstruction(instruction: Instruction)

    //Get All
    fun getAllMeals(): Flow<List<Meal>>
    fun getAllInstructions(): Flow<List<Instruction>>
    fun getAllMealsWithIngredients(): Flow<List<MealWithIngredients>>
}