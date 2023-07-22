package com.kunle.aisle9b.data

import android.content.Context
import android.net.Uri
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import org.junit.After
import org.junit.Before
import java.io.IOException
import com.google.common.truth.Truth.assertThat
import com.kunle.aisle9b.models.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test

class MealDaoShould {

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
    fun getAllMeals_MealDao() {
        runTest {
            val listOfMeals = listOf(
                Meal(name = "Famous 5-Alarm Chili", servingSize = "5", notes = "Be careful!"),
                Meal(name = "Risotto", servingSize = "4", notes = ""),
                Meal(name = "Baked Ziti", servingSize = "8 people", notes = "No notes")
            )

            listOfMeals.forEach {
                sutDB.mealDao().insertMeal(it)
            }

            val mealList = sutDB.mealDao().getAllMeals().first()
            assertThat(mealList.size).isEqualTo(3)
        }
    }

    @Test
    fun getVisibleMeals_MealDao() {
        runTest {
            val listOfMeals = listOf(
                Meal(
                    name = "Famous 5-Alarm Chili",
                    servingSize = "5",
                    notes = "Be careful!",
                    visible = true
                ),
                Meal(name = "Risotto", servingSize = "4", notes = "", visible = false),
                Meal(
                    name = "Baked Ziti",
                    servingSize = "8 people",
                    notes = "No notes",
                    visible = false
                )
            )

            listOfMeals.forEach {
                sutDB.mealDao().insertMeal(it)
            }

            val mealList = sutDB.mealDao().getVisibleMeals().first()
            assertThat(mealList.size).isEqualTo(1)
        }
    }

    @Test
    fun insertMeal_noConflict_MealDao() {
        runTest {
            sutDB.mealDao().insertMeal(
                Meal(name = "Famous 5-Alarm Chili", servingSize = "5", notes = "Be careful!")
            )

            val mealList = sutDB.mealDao().getAllMeals().first()

            assertThat(mealList.size).isEqualTo(1)
            assertThat(mealList[0].name).isEqualTo("Famous 5-Alarm Chili")
        }
    }

    @Test
    fun insertMeal_MealDao_returnsCorrectId() {
        runTest {
            val meal = Meal(
                mealId = 2,
                name = "Famous 5-Alarm Chili",
                servingSize = "5",
                notes = "Be careful!"
            )
            val returnedId = sutDB.mealDao().insertMeal(meal)

            assertThat(returnedId).isEqualTo(2L)
        }
    }

    @Test
    fun insertMeal_MealDao_conflictIgnore() {
        runTest {
            val meal = Meal(
                mealId = 1,
                name = "Famous 5-Alarm Chili",
                servingSize = "5",
                notes = "Be careful!"
            )
            sutDB.mealDao().insertMeal(meal)

            val conflictMeal =
                Meal(
                    mealId = 1,
                    name = "Lava Cake",
                    servingSize = "2",
                    notes = "Its hot!"
                )
            sutDB.mealDao().insertMeal(conflictMeal)

            val mealList = sutDB.mealDao().getAllMeals().first()

            assertThat(mealList.size).isEqualTo(1)
            assertThat(mealList[0].name).isEqualTo("Famous 5-Alarm Chili")
        }
    }

    @Test
    fun insertMeal_MealDao_noConflict_automaticallyNumbersLists() {
        runTest {
            val listOfMeals = listOf(
                Meal(name = "Famous 5-Alarm Chili", servingSize = "5", notes = "Be careful!"),
                Meal(name = "Risotto", servingSize = "4", notes = ""),
                Meal(name = "Baked Ziti", servingSize = "8 people", notes = "No notes")
            )

            listOfMeals.forEach {
                sutDB.mealDao().insertMeal(it)
            }

            val mealIdList = sutDB.mealDao().getAllMeals().first().map { it.mealId }

            assertThat(mealIdList).isEqualTo(listOf(1L, 2L, 3L))
        }
    }

    @Test
    fun upsertMeal_MealDao_insertNewInstance() {
        runTest {
            val meal = Meal(
                mealId = 1,
                name = "Famous 5-Alarm Chili",
                servingSize = "5",
                notes = "Be careful!"
            )
            sutDB.mealDao().insertMeal(meal)

            val mealList = sutDB.mealDao().getAllMeals().first()

            assertThat(mealList.size).isEqualTo(1)

            val mealNew = Meal(name = "Risotto", servingSize = "4", notes = "", visible = false)
            sutDB.mealDao().upsertMeal(mealNew)

            val mealListAfterNewInsert = sutDB.mealDao().getAllMeals().first()

            assertThat(mealListAfterNewInsert.size).isEqualTo(2)
        }
    }

    @Test
    fun upsertMeal_MealDao_insertSameInstance() {
        runTest {
            val meal = Meal(
                mealId = 1,
                name = "Famous 5-Alarm Chili",
                servingSize = "5",
                notes = "Be careful!"
            )
            sutDB.mealDao().insertMeal(meal)

            val mealList = sutDB.mealDao().getAllMeals().first()

            assertThat(mealList.size).isEqualTo(1)

            sutDB.mealDao().upsertMeal(meal)

            val mealListAfterNewInsert = sutDB.mealDao().getAllMeals().first()

            assertThat(mealListAfterNewInsert.size).isEqualTo(1)
        }
    }

    @Test
    fun upsertFood_GroceryDao_updateInstance() {
        runTest {
            val meal = Meal(
                mealId = 1,
                name = "Famous 5-Alarm Chili",
                servingSize = "5",
                notes = "Be careful!"
            )
            sutDB.mealDao().insertMeal(meal)

            val mealList = sutDB.mealDao().getAllMeals().first()

            assertThat(mealList.size).isEqualTo(1)

            val mealUpdated = Meal(
                mealId = 1,
                name = "Famous 3-Alarm Chili",
                servingSize = "3",
                notes = "Its only 3 alarm now!"
            )

            sutDB.mealDao().upsertMeal(mealUpdated)

            val mealListAfterUpdate = sutDB.mealDao().getAllMeals().first()

            assertThat(mealListAfterUpdate.size).isEqualTo(1)
            assertThat(mealListAfterUpdate[0].name).isEqualTo("Famous 3-Alarm Chili")
        }
    }

    @Test
    fun deleteMeal_MealDao() {
        runTest {
            val meal = Meal(mealId = 2, name = "Risotto", servingSize = "4", notes = "")
            sutDB.mealDao().insertMeal(meal)

            val mealList = sutDB.mealDao().getAllMeals().first()

            assertThat(mealList.size).isEqualTo(1)

            sutDB.mealDao().deleteMeal(meal)

            val mealListAfterDelete = sutDB.mealDao().getAllMeals().first()

            assertThat(mealListAfterDelete.size).isEqualTo(0)
        }
    }

    @Test
    fun updateName_MealDao() {
        runTest {
            val meal = Meal(name = "Risotto", servingSize = "4", notes = "")
            sutDB.mealDao().insertMeal(meal)

            val mealList = sutDB.mealDao().getAllMeals().first()

            assertThat(mealList.size).isEqualTo(1)
            assertThat(mealList[0].name).isEqualTo("Risotto")
            assertThat(mealList[0].mealId).isEqualTo(1L)

            val nameUpdate = MealNameUpdate(mealId = 1L, name = "Grilled Chicken")
            sutDB.mealDao().updateName(nameUpdate)

            val mealListAfterUpdate = sutDB.mealDao().getAllMeals().first()

            assertThat(mealListAfterUpdate.size).isEqualTo(1)
            assertThat(mealListAfterUpdate[0].name).isEqualTo("Grilled Chicken")
            assertThat(mealListAfterUpdate[0].mealId).isEqualTo(1L)
        }
    }

    @Test
    fun updatePic_MealDao() {
        runTest {
            val meal = Meal(name = "Risotto", servingSize = "4", notes = "", mealPic = Uri.EMPTY)
            sutDB.mealDao().insertMeal(meal)

            val mealList = sutDB.mealDao().getAllMeals().first()

            assertThat(mealList.size).isEqualTo(1)
            assertThat(mealList[0].mealPic).isEqualTo(Uri.EMPTY)
            assertThat(mealList[0].mealId).isEqualTo(1L)

            val picUpdate = MealPicUpdate(mealId = 1L, mealPic = Uri.parse("Test"))
            sutDB.mealDao().updatePic(picUpdate)

            val mealListAfterUpdate = sutDB.mealDao().getAllMeals().first()

            assertThat(mealListAfterUpdate.size).isEqualTo(1)
            assertThat(mealListAfterUpdate[0].mealPic).isEqualTo(Uri.parse("Test"))
            assertThat(mealListAfterUpdate[0].mealId).isEqualTo(1L)
        }
    }

    @Test
    fun updateServingSize_MealDao() {
        runTest {
            val meal = Meal(name = "Risotto", servingSize = "4", notes = "")
            sutDB.mealDao().insertMeal(meal)

            val mealList = sutDB.mealDao().getAllMeals().first()

            assertThat(mealList.size).isEqualTo(1)
            assertThat(mealList[0].servingSize).isEqualTo("4")
            assertThat(mealList[0].mealId).isEqualTo(1L)

            val servingSizeUpdate = MealServingSizeUpdate(mealId = 1L, servingSize = "1")
            sutDB.mealDao().updateServingSize(servingSizeUpdate)

            val mealListAfterUpdate = sutDB.mealDao().getAllMeals().first()

            assertThat(mealListAfterUpdate.size).isEqualTo(1)
            assertThat(mealListAfterUpdate[0].servingSize).isEqualTo("1")
            assertThat(mealListAfterUpdate[0].mealId).isEqualTo(1L)
        }
    }

    @Test
    fun updateNotes_MealDao() {
        runTest {
            val meal = Meal(name = "Risotto", servingSize = "4", notes = "")
            sutDB.mealDao().insertMeal(meal)

            val mealList = sutDB.mealDao().getAllMeals().first()

            assertThat(mealList.size).isEqualTo(1)
            assertThat(mealList[0].notes).isEqualTo("")
            assertThat(mealList[0].mealId).isEqualTo(1L)

            val notesUpdate = MealNotesUpdate(mealId = 1L, notes = "It taste delicious!")
            sutDB.mealDao().updateNotes(notesUpdate)

            val mealListAfterUpdate = sutDB.mealDao().getAllMeals().first()

            assertThat(mealListAfterUpdate.size).isEqualTo(1)
            assertThat(mealListAfterUpdate[0].notes).isEqualTo("It taste delicious!")
            assertThat(mealListAfterUpdate[0].mealId).isEqualTo(1L)
        }
    }

    @Test
    fun updateVisibility_MealDao() {
        runTest {
            val meal = Meal(name = "Risotto", servingSize = "4", notes = "", visible = true)
            sutDB.mealDao().insertMeal(meal)

            val mealList = sutDB.mealDao().getAllMeals().first()

            assertThat(mealList.size).isEqualTo(1)
            assertThat(mealList[0].visible).isTrue()
            assertThat(mealList[0].mealId).isEqualTo(1L)

            val visibilityUpdate = MealVisibilityUpdate(mealId = 1L, visible = false)
            sutDB.mealDao().updateVisibility(visibilityUpdate)

            val mealListAfterUpdate = sutDB.mealDao().getAllMeals().first()

            assertThat(mealListAfterUpdate.size).isEqualTo(1)
            assertThat(mealListAfterUpdate[0].visible).isFalse()
            assertThat(mealListAfterUpdate[0].mealId).isEqualTo(1L)
        }
    }
}