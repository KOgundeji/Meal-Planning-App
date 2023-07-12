package com.kunle.aisle9b.screens.customLists

import com.google.common.truth.Truth.assertThat
import com.kunle.aisle9b.MainDispatcherRule
import com.kunle.aisle9b.models.*
import com.kunle.aisle9b.repositories.customLists.CustomListRepositoryImpl
import io.mockk.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.random.Random

class CustomListVMShould {
    @get:Rule
    val coroutineRule = MainDispatcherRule()

    private val mRepository: CustomListRepositoryImpl = mockk()
    private lateinit var sutCustomListVM: CustomListVM
    private var expectedGroceryList: List<GroceryList> = listOf(mockk())
    private var expectedListWithGroceries: List<ListWithGroceries> = listOf(mockk())

    @Before
    fun setUp() {
        every { mRepository.getAllLists() } returns flow { emit(expectedGroceryList) }
        every { mRepository.getAllListWithGroceries() } returns flow { emit(expectedListWithGroceries)}
        sutCustomListVM = CustomListVM(mRepository)
    }

    @Test
    fun getAllLists_callsCorrectRepositoryMethod() {
        runTest {
            sutCustomListVM.customLists
            verify { mRepository.getAllLists() }
        }
    }

    @Test
    fun getAllLists_returnsCorrectList() {
        runTest {
            val actualList = sutCustomListVM.customLists.first()
            assertThat(actualList).isEqualTo(expectedGroceryList)
        }
    }

    @Test
    fun getAllListWithGroceries_callsCorrectRepositoryMethod() {
        runTest {
            sutCustomListVM.groceriesOfCustomLists
            verify { mRepository.getAllListWithGroceries() }
        }
    }

    @Test
    fun getAllListWithGroceries_returnsCorrectList() {
        runTest {
            val actualList = sutCustomListVM.groceriesOfCustomLists.first()
            assertThat(actualList).isEqualTo(expectedListWithGroceries)
        }
    }

    @Test
    fun insertList_callsCorrectRepositoryMethod() {
        runTest {
            val groceryListClass: GroceryList = mockk()
            val returnId: Long = Random(42L).nextLong()
            coEvery { mRepository.insertList(any()) } returns returnId

            sutCustomListVM.insertList(groceryListClass)
            coVerify { mRepository.insertList(any()) }
        }
    }

    @Test
    fun insertList_returnsCorrectListId() {
        runTest {
            val groceryListClass: GroceryList = mockk()
            val returnId: Long = Random(42L).nextLong()
            coEvery { mRepository.insertList(any()) } returns returnId

            val actualId = sutCustomListVM.insertList(groceryListClass)
            assertThat(actualId).isEqualTo(returnId)
        }
    }

    @Test
    fun deleteList_callsCorrectRepositoryMethod() {
        runTest {
            val groceryListClass: GroceryList = mockk()
            coEvery { mRepository.deleteList(any()) } returns Unit

            sutCustomListVM.deleteList(groceryListClass)
            coVerify { mRepository.deleteList(any()) }
        }
    }

    @Test
    fun updateList_callsCorrectRepositoryMethod() {
        runTest {
            val groceryListClass: GroceryList = mockk()
            coEvery { mRepository.updateList(any()) } returns Unit

            sutCustomListVM.updateList(groceryListClass)
            coVerify { mRepository.updateList(any()) }
        }
    }

    @Test
    fun insertPair_callsCorrectRepositoryMethod() {
        runTest {
            val listFoodMap: ListFoodMap = mockk()
            coEvery { mRepository.insertPair(any()) } returns Unit

            sutCustomListVM.insertPair(listFoodMap)
            coVerify { mRepository.insertPair(any()) }
        }
    }

    @Test
    fun deletePair_callsCorrectRepositoryMethod() {
        runTest {
            val listFoodMap: ListFoodMap = mockk()
            coEvery { mRepository.deletePair(any()) } returns Unit

            sutCustomListVM.deletePair(listFoodMap)
            coVerify { mRepository.deletePair(any()) }
        }
    }

    @Test
    fun deleteSpecificListWithGroceries_callsCorrectRepositoryMethod() {
        runTest {
            val mLong = Random(42L).nextLong()
            coEvery { mRepository.deleteSpecificListWithGroceries(any()) } returns Unit

            sutCustomListVM.deleteSpecificListWithGroceries(mLong)
            coVerify { mRepository.deleteSpecificListWithGroceries(any()) }
        }
    }

    @Test
    fun updateName_callsCorrectRepositoryMethod() {
        runTest {
            val nameUpdate: GroceryListNameUpdate = mockk()
            coEvery { mRepository.updateName(any()) } returns Unit

            sutCustomListVM.updateName(nameUpdate)
            coVerify { mRepository.updateName(any()) }
        }
    }

    @Test
    fun updateVisibility_callsCorrectRepositoryMethod() {
        runTest {
            val visibilityUpdate: GroceryListVisibilityUpdate = mockk()
            coEvery { mRepository.updateVisibility(any()) } returns Unit

            sutCustomListVM.updateVisibility(visibilityUpdate)
            coVerify { mRepository.updateVisibility(any()) }
        }
    }

    @Test
    fun insertFood_callsCorrectRepositoryMethod() {
        runTest {
            val food: Food = mockk()
            val returnId = Random(42L).nextLong()
            coEvery { mRepository.insertFood(any()) } returns returnId

            sutCustomListVM.insertFood(food)

            coVerify { mRepository.insertFood(any()) }
        }
    }

    @Test
    fun insertFood_returnsFoodId() {
        runTest {
            val food: Food = mockk()
            val returnId = Random(42L).nextLong()
            coEvery { mRepository.insertFood(any()) } returns returnId

            val actualId = sutCustomListVM.insertFood(food)

            assertThat(actualId).isEqualTo(returnId)
        }
    }

    @Test
    fun upsertFood_callsCorrectRepositoryMethod() {
        runTest {
            val food: Food = mockk()
            coEvery { mRepository.upsertFood(any()) } returns Unit
            sutCustomListVM.upsertFood(food)
            coVerify { mRepository.upsertFood(any()) }
        }
    }

    @Test
    fun deleteFood_callsCorrectRepositoryMethod() {
        runTest {
            val food: Food = mockk()
            coEvery { mRepository.deleteFood(any()) } returns Unit
            sutCustomListVM.deleteFood(food)
            coVerify { mRepository.deleteFood(any()) }
        }
    }

    @Test
    fun deleteGroceryByName_callsCorrectRepositoryMethod() {
        runTest {
            coEvery { mRepository.deleteGroceryByName(any()) } returns Unit
            sutCustomListVM.deleteGroceryByName("a")
            coVerify { mRepository.deleteGroceryByName(any()) }
        }
    }
}