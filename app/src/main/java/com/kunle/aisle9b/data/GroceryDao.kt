package com.kunle.aisle9b.data

import androidx.room.*
import com.kunle.aisle9b.models.Food
import com.kunle.aisle9b.models.Grocery
import kotlinx.coroutines.flow.Flow

@Dao
interface GroceryDao {
    @Insert
    suspend fun insertFood(food: Food): Long

    @Upsert
    suspend fun upsertFood(food: Food)

    @Delete
    suspend fun deleteFood(food: Food)

    @Insert
    suspend fun insertGrocery(grocery: Grocery)

    @Upsert
    suspend fun upsertGrocery(grocery: Grocery)

    @Delete
    suspend fun deleteGrocery(grocery: Grocery)

    @Query("DELETE FROM grocery_table WHERE name = :name")
    suspend fun deleteGroceryByName(name: String)

    @Query("UPDATE food_table SET category = :category WHERE name = :name")
    suspend fun updateGlobalFoodCategories(category: String, name: String)

    @Query(value = "SELECT * FROM grocery_table")
    fun getAllGroceries(): Flow<List<Grocery>>

    @Query(value = "SELECT name FROM food_table")
    fun getAllFoodNames(): Flow<List<String>>

}