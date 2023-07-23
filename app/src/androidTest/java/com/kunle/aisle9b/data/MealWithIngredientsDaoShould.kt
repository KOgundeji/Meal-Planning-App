package com.kunle.aisle9b.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import com.kunle.aisle9b.models.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

class MealWithIngredientsDaoShould {

    private lateinit var sutDB: ShoppingRoomDB

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        sutDB = Room.inMemoryDatabaseBuilder(
            context, ShoppingRoomDB::class.java
        ).build()
    }

    @After
    @Throws(IOException::class)
    fun closeDB() {
        sutDB.close()
    }

    @Test
    fun getAllMealsWithIngredients_MWIDao() {
        runTest {
            sutDB.mealDao().insertMeal(Meal(mealId = 1, name = "Famous 5-Alarm Chili", servingSize = "5", notes = "Be careful!"))
            sutDB.mealDao().insertMeal(Meal(mealId = 2, name = "Risotto", servingSize = "4", notes = ""))
            sutDB.mealDao().insertMeal(Meal(mealId = 3, name = "Baked Ziti", servingSize = "8 people", notes = "No notes"))

            sutDB.groceryDao().insertFood(Food(foodId = 42, name = "apples", quantity = "5"))
            sutDB.groceryDao().insertFood(Food(foodId = 21, name = "bananas", quantity = "3"))
            sutDB.groceryDao().insertFood(Food(foodId = 7, name = "cherries", quantity = "1 carton"))

            val listOfMealFoodMap = listOf(
                MealFoodMap(1, 42),
                MealFoodMap(2, 21),
                MealFoodMap(3, 7)
            )

            listOfMealFoodMap.forEach {
                sutDB.mealWithIngredientsDao().insertPair(it)
            }

            val mealWithIngredients = sutDB.mealWithIngredientsDao().getAllMealsWithIngredients().first()
            assertThat(mealWithIngredients.size).isEqualTo(3)
        }
    }

    @Test
    fun insertPair_noConflict_MWIDao() {
        runTest {
            sutDB.mealDao().insertMeal(Meal(mealId = 2, name = "Risotto", servingSize = "4", notes = ""))
            sutDB.groceryDao().insertFood(Food(foodId = 42, name = "apples", quantity = "5"))
            val mealFood = MealFoodMap(2, 42)
            sutDB.mealWithIngredientsDao().insertPair(mealFood)

            val mealWithIngredients = sutDB.mealWithIngredientsDao().getAllMealsWithIngredients().first()

            assertThat(mealWithIngredients.size).isEqualTo(1)
        }
    }

    @Test
    fun insertPair_conflictIgnore_MWIDao() {
        runTest {
            sutDB.mealDao().insertMeal(Meal(mealId = 2, name = "Risotto", servingSize = "4", notes = ""))
            sutDB.groceryDao().insertFood(Food(foodId = 42, name = "apples", quantity = "5"))
            val mealFood = MealFoodMap(2, 42)
            sutDB.mealWithIngredientsDao().insertPair(mealFood)

            val mealWithIngredients = sutDB.mealWithIngredientsDao().getAllMealsWithIngredients().first()
            assertThat(mealWithIngredients.size).isEqualTo(1)

            sutDB.mealWithIngredientsDao().insertPair(mealFood)

            val mealWithIngredientsAfterInsert = sutDB.mealWithIngredientsDao().getAllMealsWithIngredients().first()
            assertThat(mealWithIngredientsAfterInsert.size).isEqualTo(1)
        }
    }

    @Test
    fun deletePair_MWIDao() {
        runTest {
            sutDB.mealDao().insertMeal(Meal(mealId = 2, name = "Risotto", servingSize = "4", notes = ""))
            sutDB.groceryDao().insertFood(Food(foodId = 42, name = "apples", quantity = "5"))
            val mealFood = MealFoodMap(2, 42)
            sutDB.mealWithIngredientsDao().insertPair(mealFood)

            val mealWithIngredients = sutDB.mealWithIngredientsDao().getAllMealsWithIngredients().first()
            assertThat(mealWithIngredients.size).isEqualTo(1)

            sutDB.mealWithIngredientsDao().deletePair(mealFood)

            val mealWithIngredientsAfterInsert = sutDB.mealWithIngredientsDao().getAllMealsWithIngredients().first()
            assertThat(mealWithIngredientsAfterInsert.size).isEqualTo(0)
        }
    }

    @Test
    fun deleteSpecificListWithIngredients_LWIDao() {
        runTest {
            sutDB.mealDao().insertMeal(Meal(mealId = 1, name = "Famous 5-Alarm Chili", servingSize = "5", notes = "Be careful!"))
            sutDB.mealDao().insertMeal(Meal(mealId = 2, name = "Risotto", servingSize = "4", notes = ""))
            sutDB.mealDao().insertMeal(Meal(mealId = 3, name = "Baked Ziti", servingSize = "8 people", notes = "No notes"))

            sutDB.groceryDao().insertFood(Food(foodId = 42, name = "apples", quantity = "5"))
            sutDB.groceryDao().insertFood(Food(foodId = 21, name = "bananas", quantity = "3"))
            sutDB.groceryDao().insertFood(Food(foodId = 7, name = "cherries", quantity = "1 carton"))

            val listOfMealFoodMap = listOf(
                MealFoodMap(1, 42),
                MealFoodMap(2, 21),
                MealFoodMap(3, 7)
            )

            listOfMealFoodMap.forEach {
                sutDB.mealWithIngredientsDao().insertPair(it)
            }

            val mealWithIngredients = sutDB.mealWithIngredientsDao().getAllMealsWithIngredients().first()
            assertThat(mealWithIngredients.size).isEqualTo(3)

            sutDB.mealWithIngredientsDao().deleteSpecificMealWithIngredients(2L)

            val mealWithIngredientsAfterDeletion = sutDB.mealWithIngredientsDao().getAllMealsWithIngredients().first()
            assertThat(mealWithIngredientsAfterDeletion.size).isEqualTo(2)
        }
    }
}