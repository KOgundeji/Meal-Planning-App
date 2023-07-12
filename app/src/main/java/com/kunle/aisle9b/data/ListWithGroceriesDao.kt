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
    suspend fun deleteSpecificListWithGroceries(listId: Long)

    @Transaction
    @Query("SELECT * FROM list_table")
    fun getAllListWithGroceries(): Flow<List<ListWithGroceries>>
}