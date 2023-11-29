package com.kunle.aisle9b.repositories.customLists

import com.kunle.aisle9b.models.*
import kotlinx.coroutines.flow.Flow

interface CustomListRepository {
    //CRUD
    suspend fun insertList(list: GroceryList): Long
    suspend fun deleteList(list: GroceryList)
    suspend fun deleteAllInvisibleLists()

    suspend fun insertPair(crossRef: ListFoodMap)
    suspend fun deletePair(crossRef: ListFoodMap)
    suspend fun deleteSpecificListWithGroceries(listId: Long)

    suspend fun insertFood(food: Food) : Long
    suspend fun insertGrocery(grocery: Grocery)
    suspend fun upsertFood(food: Food)
    suspend fun upsertGrocery(grocery: Grocery)
    suspend fun deleteFood(food: Food)
    suspend fun deleteGrocery(grocery: Grocery)
    suspend fun deleteGroceryByName(name: String)

    //Partial Room entries
    suspend fun updateName(obj: GroceryListNameUpdate)
    suspend fun updateVisibility(obj: GroceryListVisibilityUpdate)

    //Get all
    fun getAllLists(): Flow<List<GroceryList>>
    fun getAllListWithGroceries(): Flow<List<ListWithGroceries>>
    fun getAllVisibleLists(): Flow<List<GroceryList>>
}