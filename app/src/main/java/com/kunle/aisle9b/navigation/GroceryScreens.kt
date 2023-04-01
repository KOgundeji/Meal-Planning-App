package com.kunle.aisle9b.navigation

enum class GroceryScreens {
    ListScreen,
    MealScreen,
    SettingsScreen,
    AddMealScreen,
    EditIngredientsScreen,
    EditMealScreen;

    companion object {
        fun fromRoute(route:String?): GroceryScreens
        = when(route?.substringBefore("/")) {
            ListScreen.name -> ListScreen
            MealScreen.name -> MealScreen
            SettingsScreen.name -> SettingsScreen
            AddMealScreen.name -> AddMealScreen
            EditIngredientsScreen.name -> EditIngredientsScreen
            EditMealScreen.name -> EditMealScreen
            null -> ListScreen
            else -> throw java.lang.IllegalArgumentException("Route $route is not recognized")
        }
    }
}