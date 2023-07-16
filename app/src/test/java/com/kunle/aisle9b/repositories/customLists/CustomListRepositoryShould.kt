package com.kunle.aisle9b.repositories.customLists

import com.google.common.truth.Truth.assertThat
import com.kunle.aisle9b.data.CustomListDao
import com.kunle.aisle9b.data.GroceryDao
import com.kunle.aisle9b.data.ListWithGroceriesDao
import com.kunle.aisle9b.models.*
import io.mockk.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import kotlin.random.Random

class CustomListRepositoryShould {

    private val mCustomListDao: CustomListDao = mockk()
    private val mGroceryDao: GroceryDao = mockk()
    private val mListWithGroceriesDao: ListWithGroceriesDao = mockk()
    private lateinit var sutRepository: CustomListRepository

    @Before
    fun setUp() {
        sutRepository = CustomListRepositoryImpl(mCustomListDao, mListWithGroceriesDao, mGroceryDao)
    }

    @Test
    fun getAllLists_callsCorrectDaoMethod() {
        runTest {
            val expectedGroceryListClass: List<GroceryList> = listOf(mockk())
            every { mCustomListDao.getAllLists() } returns flow { emit(expectedGroceryListClass) }

            sutRepository.getAllLists()

            verify { mCustomListDao.getAllLists() }
        }
    }

    @Test
    fun getAllLists_returnsCorrectList() {
        runTest {
            val expectedGroceryListClass: List<GroceryList> = listOf(mockk())
            every { mCustomListDao.getAllLists() } returns flow { emit(expectedGroceryListClass) }

            val actualLists = sutRepository.getAllLists().first()

            assertThat(actualLists).isEqualTo(expectedGroceryListClass)
        }
    }

    @Test
    fun getAllLists_errorFromDao() {
        runTest {
            every { mCustomListDao.getAllLists() } throws RuntimeException("Something went wrong")
            var exception = false

            try {
                sutRepository.getAllLists()
            } catch (e: Exception) {
                assertThat(e.message).isEqualTo("Something went wrong")
                exception = true
            }

            assertTrue(exception)
        }
    }

    @Test
    fun getAllListWithGroceries_callsCorrectDaoMethod() {
        runTest {
            val expectedListWithGroceries: List<ListWithGroceries> = listOf(mockk())
            every { mListWithGroceriesDao.getAllListWithGroceries() } returns flow { emit(expectedListWithGroceries) }

            sutRepository.getAllListWithGroceries()

            verify { mListWithGroceriesDao.getAllListWithGroceries() }
        }
    }

    @Test
    fun getAllListWithGroceries_returnsCorrectList() {
        runTest {
            val expectedListWithGroceries: List<ListWithGroceries> = listOf(mockk())
            every { mListWithGroceriesDao.getAllListWithGroceries() } returns flow { emit(expectedListWithGroceries) }

            val actualVisibleList = sutRepository.getAllListWithGroceries().first()

            assertThat(actualVisibleList).isEqualTo(expectedListWithGroceries)
        }
    }

    @Test
    fun getAllListWithGroceries_errorFromDao() {
        runTest {
            every { mListWithGroceriesDao.getAllListWithGroceries() } throws RuntimeException("Something went wrong")
            var exception = false

            try {
                sutRepository.getAllListWithGroceries()
            } catch (e: Exception) {
                assertThat(e.message).isEqualTo("Something went wrong")
                exception = true
            }

            assertTrue(exception)
        }
    }

    @Test
    fun getAllVisibleLists_callsCorrectDaoMethod() {
        runTest {
            val expectedGroceryListClass: List<GroceryList> = listOf(mockk())
            every { mCustomListDao.getAllVisibleLists() } returns flow { emit(expectedGroceryListClass) }

            sutRepository.getAllVisibleLists()

            verify { mCustomListDao.getAllVisibleLists() }
        }
    }

    @Test
    fun getAllVisibleLists_returnsCorrectList() {
        runTest {
            val expectedGroceryListClass: List<GroceryList> = listOf(mockk())
            every { mCustomListDao.getAllVisibleLists() } returns flow { emit(expectedGroceryListClass) }

            val actualVisibleList = sutRepository.getAllVisibleLists().first()

            assertThat(actualVisibleList).isEqualTo(expectedGroceryListClass)
        }
    }

    @Test
    fun getAllVisibleLists_errorFromDao() {
        runTest {
            every { mCustomListDao.getAllVisibleLists() } throws RuntimeException("Something went wrong")
            var exception = false

            try {
                sutRepository.getAllVisibleLists()
            } catch (e: Exception) {
                assertThat(e.message).isEqualTo("Something went wrong")
                exception = true
            }

            assertTrue(exception)
        }
    }

    @Test
    fun insertList_callsCorrectDaoMethod() {
        runTest {
            val groceryListClass: GroceryList = mockk()
            val returnId: Long = Random(42L).nextLong()
            coEvery { mCustomListDao.insertList(any()) } returns returnId

            sutRepository.insertList(groceryListClass)
            coVerify { mCustomListDao.insertList(any()) }
        }
    }

    @Test
    fun insertList_returnsCorrectListId() {
        runTest {
            val groceryListClass: GroceryList = mockk()
            val returnId: Long = Random(42L).nextLong()
            coEvery { mCustomListDao.insertList(any()) } returns returnId

            val actualId = sutRepository.insertList(groceryListClass)
            assertThat(actualId).isEqualTo(returnId)
        }
    }

    @Test
    fun deleteList_callsCorrectDaoMethod() {
        runTest {
            val groceryListClass: GroceryList = mockk()
            coEvery { mCustomListDao.deleteList(any()) } returns Unit

            sutRepository.deleteList(groceryListClass)
            coVerify { mCustomListDao.deleteList(any()) }
        }
    }

    @Test
    fun deleteInvisibleLists_callsCorrectDaoMethod() {
        runTest {
            coEvery { mCustomListDao.deleteAllInvisibleLists() } returns Unit

            sutRepository.deleteAllInvisibleLists()
            coVerify { mCustomListDao.deleteAllInvisibleLists() }
        }
    }


    @Test
    fun insertPair_callsCorrectDaoMethod() {
        runTest {
            val listFoodMap: ListFoodMap = mockk()
            coEvery { mListWithGroceriesDao.insertPair(any()) } returns Unit

            sutRepository.insertPair(listFoodMap)
            coVerify { mListWithGroceriesDao.insertPair(any()) }
        }
    }

    @Test
    fun deletePair_callsCorrectDaoMethod() {
        runTest {
            val listFoodMap: ListFoodMap = mockk()
            coEvery { mListWithGroceriesDao.deletePair(any()) } returns Unit

            sutRepository.deletePair(listFoodMap)
            coVerify { mListWithGroceriesDao.deletePair(any()) }
        }
    }

    @Test
    fun deleteSpecificListWithGroceries_callsCorrectDaoMethod() {
        runTest {
            val mLong = Random(42L).nextLong()
            coEvery { mListWithGroceriesDao.deleteSpecificListWithGroceries(any()) } returns Unit

            sutRepository.deleteSpecificListWithGroceries(mLong)
            coVerify { mListWithGroceriesDao.deleteSpecificListWithGroceries(any()) }
        }
    }

    @Test
    fun insertFood_callsCorrectDaoMethod() {
        runTest {
            val food: Food = mockk()
            val returnId = Random(42L).nextLong()
            coEvery { mGroceryDao.insertFood(any()) } returns returnId

            sutRepository.insertFood(food)

            coVerify { mGroceryDao.insertFood(any()) }
        }
    }

    @Test
    fun insertFood_returnsFoodId() {
        runTest {
            val food: Food = mockk()
            val returnId = Random(42L).nextLong()
            coEvery { mGroceryDao.insertFood(any()) } returns returnId

            val actualId = sutRepository.insertFood(food)

            assertThat(actualId).isEqualTo(returnId)
        }
    }

    @Test
    fun insertGrocery_callsCorrectDaoMethod() {
        runTest {
            val grocery: Grocery = mockk()
            coEvery { mGroceryDao.insertGrocery(any()) } returns Unit

            sutRepository.insertGrocery(grocery)

            coVerify { mGroceryDao.insertGrocery(any()) }
        }
    }

    @Test
    fun upsertFood_callsCorrectDaoMethod() {
        runTest {
            val food: Food = mockk()
            coEvery { mGroceryDao.upsertFood(any()) } returns Unit

            sutRepository.upsertFood(food)

            coVerify { mGroceryDao.upsertFood(any()) }
        }
    }

    @Test
    fun deleteFood_callsCorrectDaoMethod() {
        runTest {
            val food: Food = mockk()
            coEvery { mGroceryDao.deleteFood(any()) } returns Unit
            sutRepository.deleteFood(food)
            coVerify { mGroceryDao.deleteFood(any()) }
        }
    }

    @Test
    fun deleteGrocery_callsCorrectDaoMethod() {
        runTest {
            val grocery: Grocery = mockk()
            coEvery { mGroceryDao.deleteGrocery(any()) } returns Unit
            sutRepository.deleteGrocery(grocery)
            coVerify { mGroceryDao.deleteGrocery(any()) }
        }
    }

    @Test
    fun deleteGroceryByName_callsCorrectDaoMethod() {
        runTest {
            coEvery { mGroceryDao.deleteGroceryByName(any()) } returns Unit
            sutRepository.deleteGroceryByName("a")
            coVerify { mGroceryDao.deleteGroceryByName(any()) }
        }
    }

    @Test
    fun updateName_callsCorrectDaoMethod() {
        runTest {
            val nameUpdate: GroceryListNameUpdate = mockk()
            coEvery { mCustomListDao.updateName(any()) } returns Unit

            sutRepository.updateName(nameUpdate)
            coVerify { mCustomListDao.updateName(any()) }
        }
    }

    @Test
    fun updateVisibility_callsCorrectDaoMethod() {
        runTest {
            val visibilityUpdate: GroceryListVisibilityUpdate = mockk()
            coEvery { mCustomListDao.updateVisibility(any()) } returns Unit

            sutRepository.updateVisibility(visibilityUpdate)
            coVerify { mCustomListDao.updateVisibility(any()) }
        }
    }

}