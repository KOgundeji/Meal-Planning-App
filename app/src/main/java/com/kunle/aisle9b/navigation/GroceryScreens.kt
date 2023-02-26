package com.kunle.aisle9b.navigation

enum class GroceryScreens {
    GroceryScreen,
    MealScreen,
    SettingsScreen;

    companion object {
        fun fromRoute(route:String?): GroceryScreens
        = when(route?.substringBefore("/")) {
            GroceryScreen.name -> GroceryScreen
            MealScreen.name -> MealScreen
            SettingsScreen.name -> SettingsScreen
            null -> GroceryScreen
            else -> throw java.lang.IllegalArgumentException("Route $route is not recognized")
        }
    }
}