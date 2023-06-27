package com.kunle.aisle9b.screens.groceries


import com.google.common.truth.Truth.assertThat
import com.kunle.aisle9b.MainDispatcherRule
import com.kunle.aisle9b.models.Food
import com.kunle.aisle9b.models.Grocery
import com.kunle.aisle9b.repositories.groceries.FakeGroceryRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GroceryVMTest {

    @get:Rule
    val coroutineRule = MainDispatcherRule()

    private lateinit var groceryViewModel: GroceryVM
    private lateinit var groceryList: List<Grocery>

    @Before
    fun setUp() {
        groceryViewModel = GroceryVM(FakeGroceryRepository())
        groceryList = listOf(
            Grocery(3, "strawberries", "four"),
            Grocery(4, "bananas", "8"),
            Grocery(13, "potatoes", "3"),
            Grocery(99, "kidney beans", "2 cans"),
            Grocery(1, "almonds", "2 lbs")
        )
    }

    @Test
    fun getAllGroceries_returnsCurrentList() {
        val list = groceryViewModel.groceryList.value
        assertThat(list).isEqualTo(groceryList)
    }

    @Test
    fun getAllFoodNames_returnsCurrentList() {
        val foodNames = listOf("Strawberries", "Cherries", "Plantain")
        val listFromVM = groceryViewModel.namesOfAllFoods.value
        assertThat(listFromVM).isEqualTo(foodNames)
    }

    @Test
    fun insertGrocery_returnsUpdatedList() {
        val tempGroceryList = listOf(
            Grocery(3, "strawberries", "four"),
            Grocery(4, "bananas", "8"),
            Grocery(13, "potatoes", "3"),
            Grocery(99, "kidney beans", "2 cans"),
            Grocery(1, "almonds", "2 lbs"),
            Grocery(15, "toilet paper", "16 rolls")
        )
        runBlocking {
            groceryViewModel.insertGrocery(Grocery(15, "toilet paper", "16 rolls"))
            val list = groceryViewModel.groceryList.value
            assertThat(list).isEqualTo(tempGroceryList)
        }
    }

    @Test
    fun insertFood_returnsLong() {
        runBlocking {
            val returnedIndex = groceryViewModel.insertFood(Food.createBlank())
            assertThat(returnedIndex).isEqualTo(1L)
        }
    }

    @Test
    fun deleteFood_returnsCurrentList() {
        val tempGroceryList = listOf(
            Grocery(3, "strawberries", "four"),
            Grocery(4, "bananas", "8"),
            Grocery(13, "potatoes", "3"),
            Grocery(99, "kidney beans", "2 cans")
        )

        runBlocking {
            groceryViewModel.deleteGrocery(Grocery(1, "almonds", "2 lbs"))
            val list = groceryViewModel.groceryList.value
            assertThat(list).isEqualTo(tempGroceryList)
        }
    }

    @Test
    fun upsertFood_failReturnsOriginalList() {
        runBlocking {
            groceryViewModel.upsertGrocery(Grocery(3, "strawberries", "four"))
            val list = groceryViewModel.groceryList.value
            assertThat(list).isEqualTo(groceryList)
        }
    }

    @Test
    fun upsertFood_insertReturnsUpdatedList() {
        val tempGroceryList = listOf(
            Grocery(3, "strawberries", "four"),
            Grocery(4, "bananas", "8"),
            Grocery(13, "potatoes", "3"),
            Grocery(99, "kidney beans", "2 cans"),
            Grocery(1, "almonds", "2 lbs"),
            Grocery(5, "sugar", "10 oz")
        )
        runBlocking {
            groceryViewModel.upsertGrocery(Grocery(5, "sugar", "10 oz"))
            val list = groceryViewModel.groceryList.value
            assertThat(list).isEqualTo(tempGroceryList)
        }
    }

    @Test
    fun deleteGroceryByName_provideGrocery_failReturnsOriginalList() {
        runBlocking {
            groceryViewModel.deleteGroceryByName("oranges")
            val list = groceryViewModel.groceryList.value
            assertThat(list).isEqualTo(groceryList)
        }
    }

    @Test
    fun deleteGroceryByName_provideGrocery_returnsUpdatedList() {
        val tempGroceryList = listOf(
            Grocery(3, "strawberries", "four"),
            Grocery(4, "bananas", "8"),
            Grocery(99, "kidney beans", "2 cans"),
            Grocery(1, "almonds", "2 lbs")
        )

        runBlocking {
            groceryViewModel.deleteGroceryByName("potatoes")
            val list = groceryViewModel.groceryList.value
            assertThat(list).isEqualTo(tempGroceryList)
        }
    }
}