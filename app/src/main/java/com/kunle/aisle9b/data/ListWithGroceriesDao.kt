package com.kunle.aisle9b.data

import androidx.room.*
import com.kunle.aisle9b.models.ListFoodMap
import com.kunle.aisle9b.models.ListWithGroceries
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface ListWithGroceriesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPair(crossRef: ListFoodMap)

    @Delete
    suspend fun deletePair(crossRef: ListFoodMap)

    @Update
    suspend fun updatePair(crossRef: ListFoodMap)

    @Query("DELETE FROM ListFoodMap WHERE listId = :listId")
    suspend fun deleteSpecificGroceryList(listId: UUID)

    @Query("DELETE FROM ListFoodMap")
    suspend fun deleteAllGroceryLists()

    @Transaction
    @Query("SELECT * FROM list_table WHERE listId = :listId")
    suspend fun getSpecificListWithGroceries(listId: Long): ListWithGroceries

    @Transaction
    @Query("SELECT * FROM list_table")
    fun getAllListWithGroceries(): Flow<List<ListWithGroceries>>
}