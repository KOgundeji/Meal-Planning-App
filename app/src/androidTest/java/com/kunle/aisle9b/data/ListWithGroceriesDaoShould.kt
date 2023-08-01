package com.kunle.aisle9b.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.kunle.aisle9b.models.Food
import com.kunle.aisle9b.models.GroceryList
import com.kunle.aisle9b.models.ListFoodMap
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

class ListWithGroceriesDaoShould {

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
    fun getAllListWithGroceries_LWGDao() {
        runTest {
            sutDB.listDao().insertList(GroceryList(listId = 1, listName = "Grocery List A"))
            sutDB.listDao().insertList(GroceryList(listId = 2, listName = "Grocery List B"))
            sutDB.listDao().insertList(GroceryList(listId = 3, listName = "Grocery List C"))

            sutDB.groceryDao().insertFood(Food(foodId = 42, name = "apples", quantity = "5"))
            sutDB.groceryDao().insertFood(Food(foodId = 21, name = "bananas", quantity = "3"))
            sutDB.groceryDao().insertFood(Food(foodId = 7, name = "cherries", quantity = "1 carton"))

            val listOfListFoodMap = listOf(
                ListFoodMap(1, 42),
                ListFoodMap(2, 21),
                ListFoodMap(3, 7)
            )

            listOfListFoodMap.forEach {
                sutDB.listWithGroceriesDao().insertPair(it)
            }

            val listWithGroceries = sutDB.listWithGroceriesDao().getAllListWithGroceries().first()

            assertThat(listWithGroceries.size).isEqualTo(3)
        }
    }

    @Test
    fun getAllListWithGroceries_EmptyList_LWGDao() {
        runTest {
            val listWithGroceries = sutDB.listWithGroceriesDao().getAllListWithGroceries().first()

            assertThat(listWithGroceries.size).isEqualTo(0)
        }
    }

    @Test
    fun insertPair_LWGDao() {
        runTest {
            sutDB.listDao().insertList(GroceryList(listId = 1, listName = "Grocery List A"))
            sutDB.groceryDao().insertFood(Food(foodId = 42, name = "apples", quantity = "5"))
            val listFood = ListFoodMap(1, 42)

            sutDB.listWithGroceriesDao().insertPair(listFood)

            val listWithGroceries = sutDB.listWithGroceriesDao().getAllListWithGroceries().first()

            assertThat(listWithGroceries.size).isEqualTo(1)
        }
    }

    @Test
    fun deletePair_LWGDao() {
        runTest {
            sutDB.listDao().insertList(GroceryList(listId = 1, listName = "Grocery List A"))
            sutDB.groceryDao().insertFood(Food(foodId = 42, name = "apples", quantity = "5"))

            val listFood = ListFoodMap(1, 42)

            sutDB.listWithGroceriesDao().insertPair(listFood)

            val listWithGroceries = sutDB.listWithGroceriesDao().getAllListWithGroceries().first()

            assertThat(listWithGroceries.size).isEqualTo(1)

            sutDB.listWithGroceriesDao().deletePair(listFood)

            val lwgAfterDeletion = sutDB.listWithGroceriesDao().getAllListWithGroceries().first()

            assertThat(lwgAfterDeletion.size).isEqualTo(0)
        }
    }

    @Test
    fun deleteSpecificListWithIngredients_LWGDao() {
        runTest {
            sutDB.listDao().insertList(GroceryList(listId = 1, listName = "Grocery List A"))
            sutDB.listDao().insertList(GroceryList(listId = 2, listName = "Grocery List B"))
            sutDB.listDao().insertList(GroceryList(listId = 3, listName = "Grocery List C"))

            sutDB.groceryDao().insertFood(Food(foodId = 42, name = "apples", quantity = "5"))
            sutDB.groceryDao().insertFood(Food(foodId = 21, name = "bananas", quantity = "3"))
            sutDB.groceryDao().insertFood(Food(foodId = 7, name = "cherries", quantity = "1 carton"))

            val listOfListFoodMap = listOf(
                ListFoodMap(1, 42),
                ListFoodMap(2, 21),
                ListFoodMap(3, 7)
            )

            listOfListFoodMap.forEach {
                sutDB.listWithGroceriesDao().insertPair(it)
            }

            val listWithGroceries = sutDB.listWithGroceriesDao().getAllListWithGroceries().first()
            assertThat(listWithGroceries.size).isEqualTo(3)

            sutDB.listWithGroceriesDao().deleteSpecificListWithGroceries(2L)

            val listWithGroceriesAfterDeletion =
                sutDB.listWithGroceriesDao().getAllListWithGroceries().first()
            assertThat(listWithGroceriesAfterDeletion.size).isEqualTo(2)
        }
    }
}