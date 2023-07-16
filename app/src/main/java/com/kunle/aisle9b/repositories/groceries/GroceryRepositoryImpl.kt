package com.kunle.aisle9b.repositories.groceries

import com.kunle.aisle9b.data.GroceryDao
import com.kunle.aisle9b.models.Food
import com.kunle.aisle9b.models.Grocery
import com.kunle.aisle9b.repositories.BasicRepositoryFunctions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GroceryRepositoryImpl @Inject constructor(
    private val groceryDao: GroceryDao
) : GroceryRepository, BasicRepositoryFunctions {

    override suspend fun deleteGrocery(grocery: Grocery) = groceryDao.deleteGrocery(grocery)

    override suspend fun updateGlobalFoodCategories(foodName: String, newCategory: String) =
        groceryDao.updateGlobalFoodCategories(foodName, newCategory)

    override suspend fun updateGlobalGroceryCategories(groceryName: String, newCategory: String)  =
        groceryDao.updateGlobalGroceryCategories(groceryName, newCategory)

    override fun getAllGroceries(): Flow<List<Grocery>> =
        groceryDao.getAllGroceries().flowOn(Dispatchers.IO).conflate()

    override fun getAllFoodNames(): Flow<List<String>> =
        groceryDao.getAllFoodNames().flowOn(Dispatchers.IO).conflate()

    override suspend fun insertFood(food: Food): Long = groceryDao.insertFood(food)
    override suspend fun insertGrocery(grocery: Grocery) = groceryDao.insertGrocery(grocery)
    override suspend fun upsertGrocery(grocery: Grocery) = groceryDao.upsertGrocery(grocery)
    override suspend fun upsertFood(food: Food) = groceryDao.upsertFood(food)
    override suspend fun deleteFood(food: Food) = groceryDao.deleteFood(food)
    override suspend fun deleteGroceryByName(name: String) = groceryDao.deleteGroceryByName(name)
}