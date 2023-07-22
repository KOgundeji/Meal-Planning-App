package com.kunle.aisle9b.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.kunle.aisle9b.models.Food
import com.kunle.aisle9b.models.Grocery
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

class GroceryDaoShould {

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
    fun getAllGroceries_GroceryDao() {
        runTest {
            val listOfGroceries = listOf(
                Grocery(name = "apples", quantity = "5", category = "Fruit"),
                Grocery(name = "bananas", quantity = "3", category = "Fruit"),
                Grocery(name = "spinach", quantity = "10 oz", category = "Vegetables"),
                Grocery(name = "bananas", quantity = "2", category = "Toiletries")
            )

            listOfGroceries.forEach {
                sutDB.groceryDao().insertGrocery(it)
            }

            val groceryList = sutDB.groceryDao().getAllGroceries().first()

            assertThat(groceryList.size).isEqualTo(4)
        }
    }

    @Test
    fun getAllFoods_GroceryDao() {
        runTest {
            val listOfFoods = listOf(
                Food(name = "apples", quantity = "5", category = "Fruit"),
                Food(name = "bananas", quantity = "3", category = "Fruit"),
                Food(name = "spinach", quantity = "10 oz", category = "Vegetables"),
                Food(name = "bananas", quantity = "2", category = "Toiletries")
            )

            listOfFoods.forEach {
                sutDB.groceryDao().insertFood(it)
            }

            val foodList = sutDB.groceryDao().getAllFoods().first()

            assertThat(foodList.size).isEqualTo(4)
        }
    }

    @Test
    fun getAllFoodNames_GroceryDao() {
        runTest {
            val listOfFoods = listOf(
                Food(name = "apples", quantity = "5", category = "Fruit"),
                Food(name = "bananas", quantity = "3", category = "Fruit"),
                Food(name = "spinach", quantity = "10 oz", category = "Vegetables"),
                Food(name = "bananas", quantity = "2", category = "Toiletries")
            )

            listOfFoods.forEach {
                sutDB.groceryDao().insertFood(it)
            }

            val foodNameList = sutDB.groceryDao().getAllFoodNames().first()

            assertThat(foodNameList.size).isEqualTo(4)
        }
    }

    @Test
    fun insertFood_GroceryDao() {
        runTest {
            val food = Food(name = "apple", quantity = "3", category = "Produce")
            sutDB.groceryDao().insertFood(food)

            val foodList = sutDB.groceryDao().getAllFoodNames().first()

            val testFoods = foodList[0]
            assertThat(testFoods).isEqualTo("apple")
        }
    }

    @Test
    fun insertFood_GroceryDao_returnsCorrectId() {
        runTest {
            val food = Food(foodId = 2, name = "apple", quantity = "3", category = "Produce")
            val returnedId = sutDB.groceryDao().insertFood(food)

            assertThat(returnedId).isEqualTo(2L)
        }
    }

    @Test
    fun insertFood_GroceryDao_conflictIgnore() {
        runTest {
            val food = Food(foodId = 2, name = "apple", quantity = "3", category = "Produce")
            sutDB.groceryDao().insertFood(food)

            val conflictFood =
                Food(foodId = 2, name = "banana", quantity = "3", category = "Produce")
            sutDB.groceryDao().insertFood(conflictFood)

            val foodList = sutDB.groceryDao().getAllFoodNames().first()

            assertThat(foodList.size).isEqualTo(1)
            assertThat(foodList[0]).isEqualTo("apple")
        }
    }

    @Test
    fun insertFood_GroceryDao_noConflict_automaticallyNumbersLists() {
        runTest {
            val listOfFoods = listOf(
                Food(name = "apples", quantity = "5"),
                Food(name = "bananas", quantity = "3"),
                Food(name = "cherries", quantity = "1 carton"),
            )

            listOfFoods.forEach {
                sutDB.groceryDao().insertFood(it)
            }

            val foodIdList = sutDB.groceryDao().getAllFoods().first().map { it.foodId }

            assertThat(foodIdList).isEqualTo(listOf(1L, 2L, 3L))
        }
    }

    @Test
    fun upsertFood_GroceryDao_insertNewInstance() {
        runTest {
            val food = Food(foodId = 2, name = "apple", quantity = "3", category = "Produce")
            sutDB.groceryDao().insertFood(food)

            val foodList = sutDB.groceryDao().getAllFoods().first()

            assertThat(foodList.size).isEqualTo(1)

            val foodNew =
                Food(foodId = 4, name = "banana", quantity = "2 bunches", category = "Produce")
            sutDB.groceryDao().upsertFood(foodNew)

            val foodListAfterNewInsert = sutDB.groceryDao().getAllFoods().first()

            assertThat(foodListAfterNewInsert.size).isEqualTo(2)
        }
    }

    @Test
    fun upsertFood_GroceryDao_insertSameInstance() {
        runTest {
            val food = Food(foodId = 2, name = "apple", quantity = "3", category = "Produce")
            sutDB.groceryDao().insertFood(food)

            val foodList = sutDB.groceryDao().getAllFoods().first()

            assertThat(foodList.size).isEqualTo(1)

            sutDB.groceryDao().upsertFood(food)

            val foodListAfterSameInsert = sutDB.groceryDao().getAllFoods().first()

            assertThat(foodListAfterSameInsert.size).isEqualTo(1)
        }
    }

    @Test
    fun upsertFood_GroceryDao_updateInstance() {
        runTest {
            val food = Food(foodId = 2, name = "apple", quantity = "3", category = "Produce")
            sutDB.groceryDao().upsertFood(food)

            val foodList = sutDB.groceryDao().getAllFoods().first()

            assertThat(foodList.size).isEqualTo(1)

            val foodUpdated =
                Food(foodId = 2, name = "banana", quantity = "3", category = "Produce")
            sutDB.groceryDao().upsertFood(foodUpdated)

            val foodListAfterUpdate = sutDB.groceryDao().getAllFoods().first()

            assertThat(foodListAfterUpdate.size).isEqualTo(1)
            assertThat(foodListAfterUpdate[0].name).isEqualTo("banana")
        }
    }

    @Test
    fun deleteFood_GroceryDao() {
        runTest {
            val food = Food(foodId = 2, name = "apple", quantity = "3", category = "Produce")
            sutDB.groceryDao().insertFood(food)

            val foodList = sutDB.groceryDao().getAllFoods().first()

            assertThat(foodList.size).isEqualTo(1)

            sutDB.groceryDao().deleteFood(food)

            val foodListAfterUpdate = sutDB.groceryDao().getAllFoods().first()

            assertThat(foodListAfterUpdate.size).isEqualTo(0)
        }
    }

    @Test
    fun insertGrocery_GroceryDao() {
        runTest {
            val grocery = Grocery(name = "apple", quantity = "3", category = "Produce")
            sutDB.groceryDao().insertGrocery(grocery)

            val groceryList = sutDB.groceryDao().getAllGroceries().first()

            val testGrocery = groceryList[0]
            assertThat(testGrocery.name).isEqualTo("apple")
        }
    }

    @Test
    fun insertGrocery_GroceryDao_conflictIgnore() {
        runTest {
            val grocery =
                Grocery(groceryId = 3, name = "apple", quantity = "3", category = "Produce")
            sutDB.groceryDao().insertGrocery(grocery)

            val conflictGrocery =
                Grocery(groceryId = 3, name = "banana", quantity = "3", category = "Produce")
            sutDB.groceryDao().insertGrocery(conflictGrocery)

            val groceryList = sutDB.groceryDao().getAllGroceries().first()

            assertThat(groceryList.size).isEqualTo(1)
            assertThat(groceryList[0].name).isEqualTo("apple")
        }
    }

    @Test
    fun insertGrocery_GroceryDao_noConflict_automaticallyNumbersLists() {
        runTest {
            val listOfGrocery = listOf(
                Grocery(name = "apples", quantity = "5"),
                Grocery(name = "bananas", quantity = "3"),
                Grocery(name = "cherries", quantity = "1 carton"),
            )

            listOfGrocery.forEach {
                sutDB.groceryDao().insertGrocery(it)
            }

            val groceryIdList = sutDB.groceryDao().getAllGroceries().first().map { it.groceryId }

            assertThat(groceryIdList).isEqualTo(listOf(1L, 2L, 3L))
        }
    }

    @Test
    fun upsertGrocery_GroceryDao_insertNewInstance() {
        runTest {
            val grocery =
                Grocery(groceryId = 2, name = "apple", quantity = "3", category = "Produce")
            sutDB.groceryDao().insertGrocery(grocery)

            val groceryList = sutDB.groceryDao().getAllGroceries().first()

            assertThat(groceryList.size).isEqualTo(1)

            val groceryNew =
                Grocery(
                    groceryId = 4,
                    name = "banana",
                    quantity = "2 bunches",
                    category = "Produce"
                )
            sutDB.groceryDao().upsertGrocery(groceryNew)

            val groceryListAfterSameInsert = sutDB.groceryDao().getAllGroceries().first()

            assertThat(groceryListAfterSameInsert.size).isEqualTo(2)
        }
    }

    @Test
    fun upsertGrocery_GroceryDao_insertSameInstance() {
        runTest {
            val grocery =
                Grocery(groceryId = 2, name = "apple", quantity = "3", category = "Produce")
            sutDB.groceryDao().insertGrocery(grocery)

            val groceryList = sutDB.groceryDao().getAllGroceries().first()

            assertThat(groceryList.size).isEqualTo(1)

            sutDB.groceryDao().upsertGrocery(grocery)

            val groceryListAfterSameInsert = sutDB.groceryDao().getAllGroceries().first()

            assertThat(groceryListAfterSameInsert.size).isEqualTo(1)
        }
    }

    @Test
    fun upsertGrocery_GroceryDao_updateInstance() {
        runTest {
            val grocery =
                Grocery(groceryId = 2, name = "apple", quantity = "3", category = "Produce")
            sutDB.groceryDao().insertGrocery(grocery)

            val groceryList = sutDB.groceryDao().getAllGroceries().first()

            assertThat(groceryList.size).isEqualTo(1)

            val groceryUpdated =
                Grocery(groceryId = 2, name = "banana", quantity = "3", category = "Produce")
            sutDB.groceryDao().upsertGrocery(groceryUpdated)

            val groceryListAfterUpdate = sutDB.groceryDao().getAllGroceries().first()

            assertThat(groceryListAfterUpdate.size).isEqualTo(1)
            assertThat(groceryListAfterUpdate[0].name).isEqualTo("banana")
        }
    }

    @Test
    fun deleteGrocery_GroceryDao() {
        runTest {
            val grocery =
                Grocery(groceryId = 2, name = "apple", quantity = "3", category = "Produce")
            sutDB.groceryDao().insertGrocery(grocery)

            val groceryList = sutDB.groceryDao().getAllGroceries().first()

            assertThat(groceryList.size).isEqualTo(1)

            sutDB.groceryDao().deleteGrocery(grocery)

            val groceryListAfterUpdate = sutDB.groceryDao().getAllGroceries().first()

            assertThat(groceryListAfterUpdate.size).isEqualTo(0)
        }
    }

    @Test
    fun deleteGroceryByName_GroceryDao() {
        runTest {
            val listOfGrocery = listOf(
                Grocery(name = "apples", quantity = "5"),
                Grocery(name = "bananas", quantity = "3"),
                Grocery(name = "cherries", quantity = "1 carton"),
            )

            listOfGrocery.forEach {
                sutDB.groceryDao().insertGrocery(it)
            }

            val groceryList = sutDB.groceryDao().getAllGroceries().first()

            assertThat(groceryList.size).isEqualTo(3)

            sutDB.groceryDao().deleteGroceryByName("bananas")

            val groceryListAfterUpdate = sutDB.groceryDao().getAllGroceries().first()

            assertThat(groceryListAfterUpdate.size).isEqualTo(2)
            assertThat(groceryListAfterUpdate[0].name).isEqualTo("apples")
            assertThat(groceryListAfterUpdate[1].name).isEqualTo("cherries")
        }
    }

    @Test
    fun updateGlobalFoodCategories_GroceryDao() {
        runTest {
            val listOfFoods = listOf(
                Food(name = "apples", quantity = "5", category = "Fruit"),
                Food(name = "bananas", quantity = "3", category = "Fruit"),
                Food(name = "spinach", quantity = "10 oz", category = "Vegetables"),
                Food(name = "bananas", quantity = "2", category = "Toiletries")
            )

            listOfFoods.forEach {
                sutDB.groceryDao().insertFood(it)
            }

            sutDB.groceryDao().updateGlobalFoodCategories("bananas", "Produce")

            val foodListCategories = sutDB.groceryDao().getAllFoods().first()
            val apple = foodListCategories.filter { it.name == "apples" }
            val spinach = foodListCategories.filter { it.name == "spinach" }
            val bananaFoods = foodListCategories.filter { it.name == "bananas" }

            assertThat(bananaFoods.all { it.category == "Produce" }).isTrue()
            assertThat(apple[0].category).isEqualTo("Fruit")
            assertThat(spinach[0].category).isEqualTo("Vegetables")
        }
    }

    @Test
    fun updateGlobalGroceryCategories_GroceryDao() {
        runTest {
            val listOfGroceries = listOf(
                Grocery(name = "apples", quantity = "5", category = "Fruit"),
                Grocery(name = "bananas", quantity = "3", category = "Fruit"),
                Grocery(name = "spinach", quantity = "10 oz", category = "Vegetables"),
                Grocery(name = "bananas", quantity = "2", category = "Toiletries")
            )

            listOfGroceries.forEach {
                sutDB.groceryDao().insertGrocery(it)
            }

            sutDB.groceryDao().updateGlobalGroceryCategories("bananas", "Produce")

            val groceryListCategories = sutDB.groceryDao().getAllGroceries().first()
            val apple = groceryListCategories.filter { it.name == "apples" }
            val spinach = groceryListCategories.filter { it.name == "spinach" }
            val bananaFoods = groceryListCategories.filter { it.name == "bananas" }

            assertThat(bananaFoods.all { it.category == "Produce" }).isTrue()
            assertThat(apple[0].category).isEqualTo("Fruit")
            assertThat(spinach[0].category).isEqualTo("Vegetables")
        }
    }
}