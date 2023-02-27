package com.kunle.aisle9b.data

import androidx.room.*
import com.kunle.aisle9b.models.Food
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFood(food: Food)

    @Delete
    suspend fun deleteFood(food: Food)

    @Update
    suspend fun updateFood(food: Food)

    @Query("DELETE FROM food_table")
    suspend fun deleteAllFood()

    @Query("SELECT * FROM food_table WHERE name = :name LIMIT 1")
    suspend fun getFood(name: String): Flow<Food>

    @Query("SELECT * FROM food_table WHERE grocery_list")
    fun getAllGroceries(): Flow<List<Food>>

    @Query(value = "SELECT * FROM food_table")
    fun getAllFood(): Flow<List<Food>>

}