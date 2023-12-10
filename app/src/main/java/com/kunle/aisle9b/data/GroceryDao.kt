package com.kunle.aisle9b.data

import androidx.room.*
import com.kunle.aisle9b.models.Food
import com.kunle.aisle9b.models.Grocery
import kotlinx.coroutines.flow.Flow

@Dao
interface GroceryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFood(food: Food): Long

    @Upsert
    suspend fun upsertFood(food: Food)

    @Delete
    suspend fun deleteFood(food: Food)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertGrocery(grocery: Grocery)

    @Upsert
    suspend fun upsertGrocery(grocery: Grocery)

    @Delete
    suspend fun deleteGrocery(grocery: Grocery)

    @Query("DELETE FROM grocery_table WHERE name = :name")
    suspend fun deleteGroceryByName(name: String)

    @Query("UPDATE food_table SET category = :newCategory WHERE name = :foodName")
    suspend fun updateGlobalFoodCategories(foodName: String, newCategory: String)

    @Query("UPDATE grocery_table SET category = :newCategory WHERE name = :groceryName")
    suspend fun updateGlobalGroceryCategories(groceryName: String, newCategory: String)

    @Query(value = "SELECT * FROM grocery_table")
    fun getAllGroceries(): Flow<List<Grocery>>

    @Query(value = "SELECT * FROM food_table")
    fun getAllFoods(): Flow<List<Food>> //used only for testing purposes

    @Query(value = "SELECT name FROM food_table")
    fun getAllFoodNames(): Flow<List<String>>
}