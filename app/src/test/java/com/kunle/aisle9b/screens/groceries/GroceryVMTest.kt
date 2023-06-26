package com.kunle.aisle9b.screens.groceries


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.kunle.aisle9b.models.Grocery
import com.kunle.aisle9b.repositories.groceries.FakeGroceryRepository
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GroceryVMTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var groceryViewModel: GroceryVM

    @Before
    fun setUp() {
        groceryViewModel = GroceryVM(FakeGroceryRepository())
    }

    @Test
    fun getAllGroceries_returnsCurrentList() {
        val groceryList = mutableListOf<Grocery>()
        groceryList.add(Grocery(3, "strawberries", quantity = "four", category = "Uncategorized"))
        groceryList.add(Grocery(4, "bananas", quantity = "8", category = "Uncategorized"))
        groceryList.add(Grocery(13, "potatoes", quantity = "3", category = "Uncategorized"))
        groceryList.add(Grocery(99, "kidney beans", quantity = "2 cans", category = "Uncategorized"))
        groceryList.add(Grocery(1, "almonds", quantity = "2 lbs", category = "Uncategorized"))

        val list = groceryViewModel.groceryList
        assertThat(list).isEqualTo(groceryList)
    }
}