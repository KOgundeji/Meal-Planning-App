package com.kunle.aisle9b.screens.groceries

import com.google.common.truth.Truth.assertThat
import com.kunle.aisle9b.MainDispatcherRule
import com.kunle.aisle9b.models.Food
import com.kunle.aisle9b.models.Grocery
import com.kunle.aisle9b.repositories.groceries.GroceryRepositoryImpl
import io.mockk.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.random.Random

class GroceryVMShould {

    @get:Rule
    val coroutineRule = MainDispatcherRule()

    private val mRepository: GroceryRepositoryImpl = mockk()
    private var grocery: Grocery = mockk()
    private var groceryList: List<Grocery> = listOf(grocery)
    private var foodNameList: List<String> = listOf("a", "b", "c")

    private lateinit var sutGroceryViewModel: GroceryVM

    @Before
    fun setUp() {
        every { mRepository.getAllGroceries() } returns flow { emit(groceryList) }
        every { mRepository.getAllFoodNames() } returns flow { emit(foodNameList) }
        sutGroceryViewModel = GroceryVM(mRepository)
    }

    @Test
    fun getAllGroceries_callsCorrectRepositoryMethod() {
        sutGroceryViewModel.groceryList
        verify { mRepository.getAllGroceries() }
    }

    @Test
    fun getAllGroceries_returnsCorrectList() {
        runTest {
            val actual = sutGroceryViewModel.groceryList.first()
            assertThat(actual).isEqualTo(groceryList)
        }
    }

    @Test
    fun getAllFoodNames_callsCorrectRepositoryMethod() {
        sutGroceryViewModel.namesOfAllFoods
        verify { mRepository.getAllFoodNames() }
    }

    @Test
    fun getAllFoodNames_returnsCorrectList() {
        runTest {
            val actual = sutGroceryViewModel.namesOfAllFoods.first()
            assertThat(actual).isEqualTo(foodNameList)
        }
    }

    @Test
    fun updateCategories_callsCorrectRepositoryMethods() {
        coEvery { mRepository.updateGlobalFoodCategories(any(), any()) } returns Unit
        coEvery { mRepository.updateGlobalGroceryCategories(any(), any()) } returns Unit

        sutGroceryViewModel.updateCategories("bananas", "Fruit")

        coVerify { mRepository.updateGlobalFoodCategories(any(), any()) }
        coVerify { mRepository.updateGlobalGroceryCategories(any(), any()) }
    }

    @Test
    fun deleteGrocery_callsCorrectRepositoryMethod() {
        runTest {
            coEvery { mRepository.deleteGrocery(any()) } returns Unit
            sutGroceryViewModel.deleteGrocery(grocery)
            coVerify { mRepository.deleteGrocery(any()) }
        }
    }

    @Test
    fun insertFood_callsCorrectRepositoryMethod() {
        runTest {
            val food: Food = mockk()
            val returnId = Random(42L).nextLong()
            coEvery { mRepository.insertFood(any()) } returns returnId

            sutGroceryViewModel.insertFood(food)

            coVerify { mRepository.insertFood(any()) }
        }
    }

    @Test
    fun insertFood_returnsFoodId() {
        runTest {
            val food: Food = mockk()
            val returnId = Random(42L).nextLong()
            coEvery { mRepository.insertFood(any()) } returns returnId

            val actual = sutGroceryViewModel.insertFood(food)

            assertThat(actual).isEqualTo(returnId)
        }
    }

    @Test
    fun insertGrocery_callsCorrectRepositoryMethod() {
        runTest {
            coEvery { mRepository.insertGrocery(any()) } returns Unit
            sutGroceryViewModel.insertGrocery(grocery)
            coVerify { mRepository.insertGrocery(any()) }
        }
    }

    @Test
    fun upsertGrocery_callsCorrectRepositoryMethod() {
        runTest {
            coEvery { mRepository.upsertGrocery(any()) } returns Unit
            sutGroceryViewModel.upsertGrocery(grocery)
            coVerify { mRepository.upsertGrocery(any()) }
        }
    }

    @Test
    fun upsertFood_callsCorrectRepositoryMethod() {
        runTest {
            val food: Food = mockk()
            coEvery { mRepository.upsertFood(any()) } returns Unit
            sutGroceryViewModel.upsertFood(food)
            coVerify { mRepository.upsertFood(any()) }
        }
    }

    @Test
    fun deleteFood_callsCorrectRepositoryMethod() {
        runTest {
            val food: Food = mockk()
            coEvery { mRepository.deleteFood(any()) } returns Unit
            sutGroceryViewModel.deleteFood(food)
            coVerify { mRepository.deleteFood(any()) }
        }
    }

    @Test
    fun deleteGroceryByName_callsCorrectRepositoryMethod() {
        runTest {
            coEvery { mRepository.deleteGroceryByName(any()) } returns Unit
            sutGroceryViewModel.deleteGroceryByName("a")
            coVerify { mRepository.deleteGroceryByName(any()) }
        }
    }
}