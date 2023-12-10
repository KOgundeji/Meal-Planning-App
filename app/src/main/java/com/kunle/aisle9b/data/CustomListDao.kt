package com.kunle.aisle9b.data

import androidx.room.*
import com.kunle.aisle9b.models.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomListDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertList(list: GroceryList): Long

    @Delete
    suspend fun deleteList(list: GroceryList)

    @Update(entity = GroceryList::class)
    suspend fun updateName(obj: GroceryListNameUpdate)

    @Update(entity = GroceryList::class)
    suspend fun updateVisibility(obj: GroceryListVisibilityUpdate)

    @Query("DELETE FROM list_table WHERE visible = false")
    suspend fun deleteAllInvisibleLists()

    @Query("SELECT * FROM list_table WHERE visible")
    fun getAllVisibleLists(): Flow<List<GroceryList>>

    @Query("SELECT * FROM list_table")
    fun getAllLists(): Flow<List<GroceryList>>

    @Query("UPDATE food_table SET category = :newCategory WHERE name = :foodName")
    suspend fun updateGlobalFoodCategories(foodName: String, newCategory: String)

    @Query("UPDATE grocery_table SET category = :newCategory WHERE name = :groceryName")
    suspend fun updateGlobalGroceryCategories(groceryName: String, newCategory: String)
}