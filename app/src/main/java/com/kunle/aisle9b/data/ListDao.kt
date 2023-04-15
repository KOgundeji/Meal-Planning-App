package com.kunle.aisle9b.data

import androidx.room.*
import com.kunle.aisle9b.models.GroceryList
import kotlinx.coroutines.flow.Flow

@Dao
interface ListDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertList(list: GroceryList)

    @Delete
    suspend fun deleteList(list: GroceryList)

    @Update
    suspend fun updateList(list: GroceryList)

    @Query("DELETE FROM list_table")
    suspend fun deleteAllLists()

    @Query("SELECT * FROM list_table WHERE name = :name")
    suspend fun getList(name: String): GroceryList

    @Query("SELECT * FROM list_table")
    fun getAllLists(): Flow<List<GroceryList>>
}