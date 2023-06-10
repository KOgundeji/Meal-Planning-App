package com.kunle.aisle9b.repository

import com.kunle.aisle9b.api.RecipeAPI
import com.kunle.aisle9b.data.*
import com.kunle.aisle9b.models.*
import com.kunle.aisle9b.models.apiModels.instructionModels.Instructions
import com.kunle.aisle9b.models.apiModels.recipeModels.BatchRecipes
import com.kunle.aisle9b.api.apiModels.recipeModels.Recipe
import com.kunle.aisle9b.api.apiModels.recipeModels.RecipeRawAPIData
import com.kunle.aisle9b.models.apiModels.queryModels.RawJSON
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
    private val categoryDao: CategoryDao,
    private val settingsDao: SettingsDao,
    private val instructionDao: InstructionDao,
    private val listWithGroceriesDao: ListWithGroceriesDao,
    private val mealWithIngredientsDao: MealWithIngredientsDao,
    private val recipeAPI: RecipeAPI,
) {

    suspend fun getRecipe(id: Int): Recipe =
        recipeAPI.getRecipe(id = id)

    suspend fun getRandomRecipes(tags: String): RecipeRawAPIData =
        recipeAPI.getRandomRecipes(tags = tags)

    suspend fun getBatchRecipes(ids: String): BatchRecipes =
        recipeAPI.getBatchRecipes(ids = ids)

    suspend fun getSearchResults(query: String): RawJSON =
        recipeAPI.getSearchResults(query = query)

    suspend fun getInstructions(id: Int): Instructions =
        recipeAPI.getInstructions(id = id)


    suspend fun upsertFood(food: Food) = foodDao.upsertFood(food)
    suspend fun deleteFood(food: Food) = foodDao.deleteFood(food)
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

    suspend fun upsertMeal(meal: Meal) = mealDao.upsertMeal(meal)
    suspend fun deleteMeal(meal: Meal) = mealDao.deleteMeal(meal)
    suspend fun updateName(obj: MealNameUpdate) = mealDao.updateName(obj)
    suspend fun updatePic(obj: PicUpdate) = mealDao.updatePic(obj)
    suspend fun updateServingSize(obj: ServingSizeUpdate) = mealDao.updateServingSize(obj)
    suspend fun updateNotes(obj: NotesUpdate) = mealDao.updateNotes(obj)
    suspend fun deleteAllMeals() = mealDao.deleteAllMeals()
    suspend fun getMeal(name: String) = mealDao.getMeal(name)
    fun getAllMeals(): Flow<List<Meal>> = mealDao.getAllMeals().flowOn(Dispatchers.IO).conflate()

    suspend fun upsertCategory(category: Category) = categoryDao.upsertCategory(category)
    suspend fun deleteCategory(category: Category) = categoryDao.deleteCategory(category)
    suspend fun deleteAllCategories() = categoryDao.deleteAllCategories()
    fun getAllCategories(): Flow<List<Category>> =
        categoryDao.getAllCategories().flowOn(Dispatchers.IO).conflate()

    suspend fun insertSettings(settings: AppSettings) = settingsDao.insertSettings(settings)
    suspend fun deleteSettings(settings: AppSettings) = settingsDao.deleteSettings(settings)
    suspend fun updateSettings(settings: AppSettings) = settingsDao.updateSettings(settings)
    suspend fun deleteAllSettings() = settingsDao.deleteAllSettings()
    suspend fun checkSetting(name: String): Int = settingsDao.checkSetting(name)
    fun getAllSettings(): Flow<List<AppSettings>> =
        settingsDao.getAllSettings().flowOn(Dispatchers.IO).conflate()

    suspend fun upsertInstruction(instruction: Instruction) = instructionDao.upsertInstruction(instruction)
    suspend fun deleteInstruction(instruction: Instruction) = instructionDao.deleteInstruction(instruction)
    fun getAllInstructionsForMeal(mealId: UUID): Flow<List<Instruction>> =
        instructionDao.getAllInstructionsForMeal(mealId).flowOn(Dispatchers.IO).conflate()
    fun getAllInstructions(): Flow<List<Instruction>> =
        instructionDao.getAllInstructions().flowOn(Dispatchers.IO).conflate()


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