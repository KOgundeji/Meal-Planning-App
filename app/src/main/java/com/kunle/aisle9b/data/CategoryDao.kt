package com.kunle.aisle9b.data

import androidx.room.*
import com.kunle.aisle9b.models.Category
import com.kunle.aisle9b.models.Food
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Upsert
    suspend fun upsertCategory(category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)

    @Query("DELETE FROM category_table")
    suspend fun deleteAllCategories()

    @Query("SELECT * FROM category_table")
    fun getAllCategories(): Flow<List<Category>>

}