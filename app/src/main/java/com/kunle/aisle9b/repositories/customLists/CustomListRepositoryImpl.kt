package com.kunle.aisle9b.repositories.customLists

import com.kunle.aisle9b.data.CustomListDao
import com.kunle.aisle9b.data.GroceryDao
import com.kunle.aisle9b.data.ListWithGroceriesDao
import com.kunle.aisle9b.models.*
import com.kunle.aisle9b.repositories.BasicRepositoryFunctions
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CustomListRepositoryImpl @Inject constructor(
    private val customListDao: CustomListDao,
    private val listWithGroceriesDao: ListWithGroceriesDao,
    private val groceryDao: GroceryDao
) : CustomListRepository, BasicRepositoryFunctions {

    //CRUD
    override suspend fun insertList(list: GroceryList): Long = customListDao.insertList(list)
    override suspend fun deleteList(list: GroceryList) = customListDao.deleteList(list)
    override suspend fun updateList(list: GroceryList) = customListDao.updateList(list)
    override suspend fun deleteAllInvisibleLists() = customListDao.deleteAllInvisibleLists()

    override suspend fun insertPair(crossRef: ListFoodMap) =
        listWithGroceriesDao.insertPair(crossRef)

    override suspend fun deletePair(crossRef: ListFoodMap) =
        listWithGroceriesDao.deletePair(crossRef)

    override suspend fun deleteSpecificListWithGroceries(listId: Long) =
        listWithGroceriesDao.deleteSpecificListWithGroceries(listId)

    override suspend fun insertFood(food: Food): Long = groceryDao.insertFood(food)
    override suspend fun insertGrocery(grocery: Grocery) = groceryDao.insertGrocery(grocery)
    override suspend fun upsertFood(food: Food) = groceryDao.upsertFood(food)
    override suspend fun deleteFood(food: Food) = groceryDao.deleteFood(food)
    override suspend fun deleteGrocery(grocery: Grocery) = groceryDao.deleteGrocery(grocery)

    override suspend fun deleteGroceryByName(name: String) = groceryDao.deleteGroceryByName(name)

    //Partial Room entries
    override suspend fun updateName(obj: GroceryListNameUpdate) = customListDao.updateName(obj)
    override suspend fun updateVisibility(obj: GroceryListVisibilityUpdate) = customListDao.updateVisibility(obj)

    //Get all
    override fun getAllLists(): Flow<List<GroceryList>> = customListDao.getAllLists()
    override fun getAllListWithGroceries(): Flow<List<ListWithGroceries>> =
        listWithGroceriesDao.getAllListWithGroceries()

    override fun getAllVisibleLists(): Flow<List<GroceryList>> = customListDao.getAllVisibleLists()
}