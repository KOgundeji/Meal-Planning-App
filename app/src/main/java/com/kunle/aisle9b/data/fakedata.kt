package com.kunle.aisle9b.data

import com.kunle.aisle9b.models.*
import com.kunle.aisle9b.screens.SharedVM
import com.kunle.aisle9b.screens.customLists.CustomListVM
import com.kunle.aisle9b.screens.meals.MealVM
//
//var oranges =
//    Food(name = "Oranges", quantity = "4 bunches", category = "Fruit", isInGroceryList = true)
//var pineapple = Food(name = "Pineapple", quantity = "3", category = "Fruit", isInGroceryList = true)
//var eggs =
//    Food(name = "Eggs", quantity = "1 dozen", category = "Uncategorized", isInGroceryList = true)
//var cheese = Food(
//    name = "Cheese",
//    quantity = "50 grams",
//    category = "Uncategorized",
//    isInGroceryList = false
//)
//var pasta =
//    Food(name = "Pasta", quantity = "2 boxes", category = "Condiments", isInGroceryList = true)
//var tissues =
//    Food(name = "Tissues", quantity = "1 box", category = "Uncategorized", isInGroceryList = true)
//var potatoes =
//    Food(name = "Potatoes", quantity = "3", category = "Uncategorized", isInGroceryList = false)
//var bananas =
//    Food(name = "Bananas", quantity = "2 bunches", category = "Fruit", isInGroceryList = true)
//var kiwis =
//    Food(name = "Kiwis", quantity = "8", category = "Fruit", isInGroceryList = true)
//var brownrice =
//    Food(
//        name = "Brown Rice",
//        quantity = "1 box",
//        category = "Uncategorized",
//        isInGroceryList = false
//    )
//
//val sampleFoodData = listOf(
//    oranges, pineapple, eggs, cheese, pasta, tissues, potatoes, bananas, kiwis,
//    brownrice
//)
//
//fun addFakeToDatabase(
//    sharedVM: SharedVM,
//    mealVM: MealVM,
//    customListVM: CustomListVM
//) {
//    sampleFoodData.forEach {
//        sharedVM.upsertFood(it)
//    }
//    fakeMealList.forEach {
//        mealVM.upsertMeal(it)
//    }
//    fakeGroceryLists.forEach {
//        customListVM.insertList(it)
//    }
//    fakeCustomGroceryList.forEach { lwg ->
//        lwg.groceries.forEach { groceries ->
//            customListVM.insertPair(ListFoodMap(lwg.list.listId, groceries.foodId))
//        }
//    }
//    fakeMealWithIngredients.forEach { mwi ->
//        mwi.foods.forEach { groceries ->
//            mealVM.insertPair(MealFoodMap(mwi.meal.mealId, groceries.foodId))
//        }
//    }
//}
//
//val meal1 = Meal(name = "Pumpkin Pie", servingSize = "1", notes = "Notes")
//val meal2 = Meal(name = "Risotto", servingSize = "1", notes = "Notes")
//val meal3 = Meal(name = "Meat Lasagna", servingSize = "1", notes = "Notes")
//
//val list1 = GroceryList(name = "Regular")
//val list2 = GroceryList(name = "Hungry")
//val list3 = GroceryList(name = "Entertaining Guests")
//
//val fakeMealList: List<Meal> = listOf(meal1, meal2, meal3)
//val fakeGroceryLists: List<GroceryList> = listOf(list1, list2, list3)
//
//val fakeCustomGroceryList: List<ListWithGroceries> = listOf(
//    ListWithGroceries(list = list1, groceries = listOf(oranges, pasta, potatoes)),
//    ListWithGroceries(list = list2, groceries = listOf(tissues, cheese, eggs)),
//    ListWithGroceries(list = list3, groceries = listOf(pineapple, oranges, pasta))
//)
//
//val fakeMealWithIngredients: List<MealWithIngredients> = listOf(
//    MealWithIngredients(meal = meal1, foods = listOf(kiwis, brownrice)),
//    MealWithIngredients(meal = meal2, foods = listOf(pasta, cheese, pineapple, bananas)),
//    MealWithIngredients(meal = meal3, foods = listOf(cheese, eggs, tissues)),
//)
//
