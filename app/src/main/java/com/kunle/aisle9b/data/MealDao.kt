package com.kunle.aisle9b.data

import androidx.room.*
import com.kunle.aisle9b.models.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MealDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMeal(meal: Meal): Long

    @Upsert
    suspend fun upsertMeal(meal: Meal)

    @Delete
    suspend fun deleteMeal(meal: Meal)

    @Update(entity = Meal::class)
    suspend fun updateName(obj: MealNameUpdate)

    @Update(entity = Meal::class)
    suspend fun updatePic(obj: MealPicUpdate)

    @Update(entity = Meal::class)
    suspend fun updateServingSize(obj: MealServingSizeUpdate)

    @Update(entity = Meal::class)
    suspend fun updateNotes(obj: MealNotesUpdate)

    @Update(entity = Meal::class)
    suspend fun updateVisibility(obj: MealVisibilityUpdate)

    @Query("SELECT * FROM meal_table WHERE visible")
    fun getVisibleMeals(): Flow<List<Meal>>

    @Query("SELECT * FROM meal_table")
    fun getAllMeals(): Flow<List<Meal>>
}