package com.kunle.aisle9b.repositories.meals

import com.kunle.aisle9b.api.RecipeAPI
import com.kunle.aisle9b.api.apiModels.recipeModels.Recipe
import com.kunle.aisle9b.data.GroceryDao
import com.kunle.aisle9b.data.InstructionDao
import com.kunle.aisle9b.data.MealDao
import com.kunle.aisle9b.data.MealWithIngredientsDao
import com.kunle.aisle9b.models.*
import com.kunle.aisle9b.models.apiModels.instructionModels.Instructions
import com.kunle.aisle9b.repositories.BasicRepositoryFunctions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class MealRepositoryImpl @Inject constructor(
    private val groceryDao: GroceryDao,
    private val mealDao: MealDao,
    private val instructionDao: InstructionDao,
    private val mealWithIngredientsDao: MealWithIngredientsDao,
    private val recipeAPI: RecipeAPI
) : MealRepository, BasicRepositoryFunctions {

    //API
    override suspend fun getRecipe(id: Int): Recipe = recipeAPI.getRecipe(id = id)
    override suspend fun getInstructions(id: Int): Instructions = recipeAPI.getInstructions(id = id)

    //Partial Room entries
    override suspend fun updateName(obj: MealNameUpdate) = mealDao.updateName(obj)
    override suspend fun updatePic(obj: MealPicUpdate) = mealDao.updatePic(obj)
    override suspend fun updateServingSize(obj: MealServingSizeUpdate) = mealDao.updateServingSize(obj)
    override suspend fun updateNotes(obj: MealNotesUpdate) = mealDao.updateNotes(obj)
    override suspend fun updateVisibility(obj: MealVisibilityUpdate) = mealDao.updateVisibility(obj)

    //CRUD
    override suspend fun insertMeal(meal: Meal): Long = mealDao.insertMeal(meal)
    override suspend fun upsertMeal(meal: Meal) = mealDao.upsertMeal(meal)
    override suspend fun deleteMeal(meal: Meal) = mealDao.deleteMeal(meal)
    override suspend fun insertGrocery(grocery: Grocery) = groceryDao.insertGrocery(grocery)
    override suspend fun deleteGroceryByName(name: String) = groceryDao.deleteGroceryByName(name)
    override suspend fun insertFood(food: Food): Long = groceryDao.insertFood(food)
    override suspend fun upsertFood(food: Food) = groceryDao.upsertFood(food)
    override suspend fun deleteFood(food: Food) = groceryDao.deleteFood(food)
    override suspend fun deleteGrocery(grocery: Grocery) = groceryDao.deleteGrocery(grocery)

    override suspend fun insertPair(crossRef: MealFoodMap) =
        mealWithIngredientsDao.insertPair(crossRef)

    override suspend fun deletePair(crossRef: MealFoodMap) =
        mealWithIngredientsDao.deletePair(crossRef)

    override suspend fun deleteSpecificMealWithIngredients(mealId: Long) =
        mealWithIngredientsDao.deleteSpecificMealWithIngredients(mealId)

    override suspend fun upsertInstruction(instruction: Instruction) =
        instructionDao.upsertInstruction(instruction)

    override suspend fun deleteInstruction(instruction: Instruction) =
        instructionDao.deleteInstruction(instruction)


    //Get All
    override fun getAllMeals(): Flow<List<Meal>> =
        mealDao.getAllMeals().flowOn(Dispatchers.IO).conflate()

    override fun getAllInstructions(): Flow<List<Instruction>> =
        instructionDao.getAllInstructions().flowOn(Dispatchers.IO).conflate()

    override fun getAllMealsWithIngredients(): Flow<List<MealWithIngredients>> =
        mealWithIngredientsDao.getAllMealsWithIngredients().flowOn(Dispatchers.IO).conflate()

}