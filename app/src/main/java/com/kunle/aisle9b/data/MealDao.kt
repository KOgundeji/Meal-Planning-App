package com.kunle.aisle9b.data

import androidx.room.*
import com.kunle.aisle9b.models.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MealDao {
    @Insert
    suspend fun insertMeal(meal: Meal): Long

    @Upsert
    suspend fun upsertMeal(meal: Meal)

    @Delete
    suspend fun deleteMeal(meal: Meal)

    @Update(entity = Meal::class)
    suspend fun updateName(obj: MealNameUpdate)

    @Update(entity = Meal::class)
    suspend fun updatePic(obj: PicUpdate)

    @Update(entity = Meal::class)
    suspend fun updateServingSize(obj: ServingSizeUpdate)

    @Update(entity = Meal::class)
    suspend fun updateNotes(obj: NotesUpdate)

    @Query("SELECT * FROM meal_table WHERE visible")
    fun getVisibleMeals(): Flow<List<Meal>>

    @Query("SELECT * FROM meal_table")
    fun getAllMeals(): Flow<List<Meal>>
}