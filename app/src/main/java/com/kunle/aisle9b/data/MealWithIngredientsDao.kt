package com.kunle.aisle9b.data

import androidx.room.*
import com.kunle.aisle9b.models.MealFoodMap
import com.kunle.aisle9b.models.MealWithIngredients
import kotlinx.coroutines.flow.Flow

@Dao
interface MealWithIngredientsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPair(crossRef: MealFoodMap)

    @Delete
    suspend fun deletePair(crossRef: MealFoodMap)

    @Query("DELETE FROM MealFoodMap WHERE mealId = :mealId")
    suspend fun deleteSpecificMealWithIngredients(mealId: Long)

    @Transaction
    @Query("SELECT * FROM meal_table")
    fun getAllMealsWithIngredients(): Flow<List<MealWithIngredients>>
}