package com.kunle.aisle9b.repositories.meals

import com.kunle.aisle9b.api.apiModels.recipeModels.Recipe
import com.kunle.aisle9b.models.*
import com.kunle.aisle9b.models.apiModels.instructionModels.Instructions
import kotlinx.coroutines.flow.Flow

class FakeMealRepository: MealRepository {
    override suspend fun getRecipe(id: Int): Recipe {
        TODO("Not yet implemented")
    }

    override suspend fun getInstructions(id: Int): Instructions {
        TODO("Not yet implemented")
    }

    override suspend fun upsertMeal(meal: Meal) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteMeal(meal: Meal) {
        TODO("Not yet implemented")
    }

    override suspend fun insertGrocery(grocery: Grocery) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteGrocery(grocery: Grocery) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteGroceryByName(name: String) {
        TODO("Not yet implemented")
    }

    override suspend fun insertFood(food: Food): Long {
        TODO("Not yet implemented")
    }

    override suspend fun upsertFood(food: Food) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFood(food: Food) {
        TODO("Not yet implemented")
    }

    override suspend fun updateName(obj: MealNameUpdate) {
        TODO("Not yet implemented")
    }

    override suspend fun updatePic(obj: PicUpdate) {
        TODO("Not yet implemented")
    }

    override suspend fun updateServingSize(obj: ServingSizeUpdate) {
        TODO("Not yet implemented")
    }

    override suspend fun updateNotes(obj: NotesUpdate) {
        TODO("Not yet implemented")
    }

    override suspend fun insertMeal(meal: Meal): Long {
        TODO("Not yet implemented")
    }

    override suspend fun insertPair(crossRef: MealFoodMap) {
        TODO("Not yet implemented")
    }

    override suspend fun deletePair(crossRef: MealFoodMap) {
        TODO("Not yet implemented")
    }

    override suspend fun updatePair(crossRef: MealFoodMap) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteSpecificMealWithIngredients(mealId: Long) {
        TODO("Not yet implemented")
    }

    suspend fun deleteSpecificMealWithIngredients(mealId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun upsertInstruction(instruction: Instruction) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteInstruction(instruction: Instruction) {
        TODO("Not yet implemented")
    }

    override fun getAllMeals(): Flow<List<Meal>> {
        TODO("Not yet implemented")
    }

    override fun getAllInstructions(): Flow<List<Instruction>> {
        TODO("Not yet implemented")
    }

    override fun getAllMealsWithIngredients(): Flow<List<MealWithIngredients>> {
        TODO("Not yet implemented")
    }
}