package com.kunle.aisle9b.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.kunle.aisle9b.models.GroceryList
import com.kunle.aisle9b.models.GroceryListNameUpdate
import com.kunle.aisle9b.models.GroceryListVisibilityUpdate
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

class CustomListDaoShould {

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
    fun getAllVisibleLists_ListDao() {
        runTest {
            val listOfGroceryLists = listOf(
                GroceryList(listName = "Grocery List A", visible = true),
                GroceryList(listName = "Grocery List B", visible = false),
                GroceryList(listName = "Grocery List C", visible = true),
                GroceryList(listName = "Grocery List D", visible = false)
            )

            listOfGroceryLists.forEach {
                sutDB.listDao().insertList(it)
            }

            val groceryList = sutDB.listDao().getAllLists().first()

            assertThat(groceryList.size).isEqualTo(4)

            val allVisibleLists = sutDB.listDao().getAllVisibleLists().first()

            assertThat(allVisibleLists.size).isEqualTo(2)
            assertThat(allVisibleLists[0].listName).isEqualTo("Grocery List A")
            assertThat(allVisibleLists[1].listName).isEqualTo("Grocery List C")
        }
    }

    @Test
    fun getAllLists_ListDao() {
        runTest {
            val listOfGroceryLists = listOf(
                GroceryList(listName = "Grocery List A", visible = true),
                GroceryList(listName = "Grocery List B", visible = false),
                GroceryList(listName = "Grocery List C", visible = true),
                GroceryList(listName = "Grocery List D", visible = false)
            )

            listOfGroceryLists.forEach {
                sutDB.listDao().insertList(it)
            }

            val groceryList = sutDB.listDao().getAllLists().first()

            assertThat(groceryList.size).isEqualTo(4)
        }
    }

    @Test
    fun insertList_ListDao_noConflict() {
        runTest {
            val listClass = GroceryList(listName = "Birthday Party List")
            sutDB.listDao().insertList(listClass)

            val groceryList = sutDB.listDao().getAllLists().first()

            val actualEntry = groceryList[0]
            assertThat(actualEntry.listName).isEqualTo("Birthday Party List")
        }
    }

    @Test
    fun insertList_ListDao_conflictIgnore() {
        runTest {
            val listClass = GroceryList(listId = 1, listName = "Birthday Party List")
            sutDB.listDao().insertList(listClass)

            val conflictListClass = GroceryList(listId = 1, listName = "Basic List")
            sutDB.listDao().insertList(conflictListClass)

            val groceryList = sutDB.listDao().getAllLists().first()

            assertThat(groceryList.size).isEqualTo(1)
            assertThat(groceryList[0].listName).isEqualTo("Birthday Party List")
        }
    }

    @Test
    fun insertList_ListDao_noConflict_automaticallyNumbersLists() {
        runTest {
            val listOfGroceryLists = listOf(
                GroceryList(listName = "Birthday Party List"),
                GroceryList(listName = "Base Grocery List"),
                GroceryList(listName = "Kid's Lunch List")
            )

            listOfGroceryLists.forEach {
                sutDB.listDao().insertList(it)
            }

            val groceryList = sutDB.listDao().getAllLists().first()

            val listOfTestListIds = groceryList.map { it.listId }
            assertThat(listOfTestListIds).isEqualTo(listOf(1L, 2L, 3L))
        }
    }

    @Test
    fun deleteList_ListDao() {
        runTest {
            val listClass = GroceryList(listId = 1, listName = "Birthday Party List")
            sutDB.listDao().insertList(listClass)

            val groceryList = sutDB.listDao().getAllLists().first()
            val actualEntry = groceryList[0]

            assertThat(actualEntry.listName).isEqualTo("Birthday Party List")
            assertThat(groceryList.size).isEqualTo(1)

            sutDB.listDao().deleteList(listClass)
            val groceryListAfterDeleted = sutDB.listDao().getAllLists().first()

            assertThat(groceryListAfterDeleted.size).isEqualTo(0)
        }
    }

    @Test
    fun updateName_ListDao() {
        runTest {
            val listClass = GroceryList(listName = "Birthday Party List")
            sutDB.listDao().insertList(listClass)

            val groceryList = sutDB.listDao().getAllLists().first()
            val actualEntry = groceryList[0]

            assertThat(actualEntry.listName).isEqualTo("Birthday Party List")
            assertThat(groceryList.size).isEqualTo(1)
            assertThat(actualEntry.listId).isEqualTo(1L)

            val nameUpdate = GroceryListNameUpdate(1, "Another List Name")
            sutDB.listDao().updateName(nameUpdate)

            val groceryListAfterUpdated = sutDB.listDao().getAllLists().first()
            val updatedEntry = groceryListAfterUpdated[0]

            assertThat(updatedEntry.listName).isEqualTo("Another List Name")
            assertThat(updatedEntry.listId).isEqualTo(1)
            assertThat(groceryListAfterUpdated.size).isEqualTo(1)
        }
    }

    @Test
    fun updateVisibility_ListDao() {
        runTest {
            val listClass = GroceryList(listName = "Birthday Party List", visible = true)
            sutDB.listDao().insertList(listClass)

            val groceryList = sutDB.listDao().getAllLists().first()
            val actualEntry = groceryList[0]

            assertThat(actualEntry.listName).isEqualTo("Birthday Party List")
            assertThat(groceryList.size).isEqualTo(1)
            assertThat(actualEntry.listId).isEqualTo(1L)
            assertThat(actualEntry.visible).isTrue()

            val visibilityUpdate = GroceryListVisibilityUpdate(1, false)
            sutDB.listDao().updateVisibility(visibilityUpdate)

            val groceryListAfterUpdated = sutDB.listDao().getAllLists().first()
            val updatedEntry = groceryListAfterUpdated[0]

            assertThat(updatedEntry.visible).isFalse()
            assertThat(updatedEntry.listId).isEqualTo(1)
            assertThat(groceryListAfterUpdated.size).isEqualTo(1)
        }
    }

    @Test
    fun deleteAllInvisibleLists_ListDao() {
        runTest {
            val listOfGroceryLists = listOf(
                GroceryList(listName = "Grocery List A", visible = true),
                GroceryList(listName = "Grocery List B", visible = false),
                GroceryList(listName = "Grocery List C", visible = true),
                GroceryList(listName = "Grocery List D", visible = false)
            )

            listOfGroceryLists.forEach {
                sutDB.listDao().insertList(it)
            }

            val groceryList = sutDB.listDao().getAllLists().first()

            assertThat(groceryList.size).isEqualTo(4)

            sutDB.listDao().deleteAllInvisibleLists()

            val groceryListAfterDeletingInvisible = sutDB.listDao().getAllLists().first()

            assertThat(groceryListAfterDeletingInvisible.size).isEqualTo(2)
            assertThat(groceryListAfterDeletingInvisible[0].listName).isEqualTo("Grocery List A")
            assertThat(groceryListAfterDeletingInvisible[1].listName).isEqualTo("Grocery List C")
        }
    }
}