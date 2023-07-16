package com.kunle.aisle9b.repositories.groceries

import com.google.common.truth.Truth.assertThat
import com.kunle.aisle9b.data.GroceryDao
import com.kunle.aisle9b.models.Food
import com.kunle.aisle9b.models.Grocery
import io.mockk.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.random.Random

class GroceryRepositoryShould {

    private val mGroceryDao: GroceryDao = mockk()
    private lateinit var sutRepository: GroceryRepositoryImpl

    @Before
    fun setUp() {
        sutRepository = GroceryRepositoryImpl(mGroceryDao)
    }

    @Test
    fun getAllGroceries_callsCorrectDaoMethod() {
        runTest {
            val groceryList: List<Grocery> = listOf(mockk())
            every { mGroceryDao.getAllGroceries() } returns flow { emit(groceryList) }

            sutRepository.getAllGroceries()
            verify { mGroceryDao.getAllGroceries() }
        }
    }

    @Test
    fun getAllGroceries_returnsCorrectList() {
        runTest {
            val groceryList: List<Grocery> = listOf(mockk())
            every { mGroceryDao.getAllGroceries() } returns flow { emit(groceryList) }

            val actualList = sutRepository.getAllGroceries().first()
            assertThat(actualList).isEqualTo(groceryList)
        }
    }

    @Test
    fun getAllFoodNames_callsCorrectDaoMethod() {
        runTest {
            val foodNamesList: List<String> = listOf("d", "e", "f")
            every { mGroceryDao.getAllFoodNames() } returns flow { emit(foodNamesList) }

            sutRepository.getAllFoodNames()
            verify { mGroceryDao.getAllFoodNames() }
        }
    }

    @Test
    fun getAllFoodNames_returnsCorrectList() {
        runTest {
            val foodNamesList: List<String> = listOf("d", "e", "f")
            every { mGroceryDao.getAllFoodNames() } returns flow { emit(foodNamesList) }

            val actualList = sutRepository.getAllFoodNames().first()
            assertThat(actualList).isEqualTo(foodNamesList)
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
    fun updateGlobalFoodCategories_callsCorrectDaoMethod() {
        runTest {
            val name = "Test Name"
            val category = "Test Category"
            coEvery { mGroceryDao.updateGlobalFoodCategories(any(), any()) } returns Unit

            sutRepository.updateGlobalFoodCategories(name, category)
            coVerify { mGroceryDao.updateGlobalFoodCategories(any(), any()) }
        }
    }

    @Test
    fun updateGlobalGroceryCategories_callsCorrectDaoMethod() {
        runTest {
            val name = "Test Name"
            val category = "Test Category"
            coEvery { mGroceryDao.updateGlobalGroceryCategories(any(), any()) } returns Unit

            sutRepository.updateGlobalGroceryCategories(name, category)
            coVerify { mGroceryDao.updateGlobalGroceryCategories(any(), any()) }
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
    fun upsertGrocery_callsCorrectDaoMethod() {
        runTest {
            val grocery: Grocery = mockk()
            coEvery { mGroceryDao.upsertGrocery(any()) } returns Unit
            sutRepository.upsertGrocery(grocery)
            coVerify { mGroceryDao.upsertGrocery(any()) }
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
    fun deleteGroceryByName_callsCorrectDaoMethod() {
        runTest {
            coEvery { mGroceryDao.deleteGroceryByName(any()) } returns Unit
            sutRepository.deleteGroceryByName("a")
            coVerify { mGroceryDao.deleteGroceryByName(any()) }
        }
    }
}