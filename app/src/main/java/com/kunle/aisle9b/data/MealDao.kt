package com.kunle.aisle9b.data

import androidx.room.*
import com.kunle.aisle9b.models.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MealDao {

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

    @Query("DELETE FROM meal_table")
    suspend fun deleteAllMeals()

    @Query("SELECT * FROM meal_table WHERE name = :name")
    suspend fun getMeal(name: String): Meal

    @Query("SELECT * FROM meal_table")
    fun getAllMeals(): Flow<List<Meal>>
}