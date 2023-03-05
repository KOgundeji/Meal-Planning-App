package com.kunle.aisle9b.data

import androidx.room.*
import com.kunle.aisle9b.models.Meal
import kotlinx.coroutines.flow.Flow

@Dao
interface MealDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMeal(meal: Meal)

    @Delete
    suspend fun deleteMeal(meal: Meal)

    @Update
    suspend fun updateMeal(meal: Meal)

    @Query("DELETE FROM meal_table")
    suspend fun deleteAllMeals()

    @Query("SELECT * FROM meal_table WHERE name = :name")
    suspend fun getMeal(name: String): Meal

    @Query("SELECT * FROM meal_table")
    fun getAllMeals(): Flow<List<Meal>>
}