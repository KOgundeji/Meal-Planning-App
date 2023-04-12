package com.kunle.aisle9b.navigation

enum class GroceryScreens {
    ListScreen,
    MealScreen,
    SettingsScreen,
    RecipeScreen,
    FoodListScreen;

    companion object {
        fun fromRoute(route: String?): GroceryScreens = when (route?.substringBefore("/")) {
            ListScreen.name -> ListScreen
            MealScreen.name -> MealScreen
            SettingsScreen.name -> SettingsScreen
            null -> ListScreen
            else -> throw java.lang.IllegalArgumentException("Route $route is not recognized")
        }

        fun fullName(header: GroceryScreens): String =
            when (header) {
                ListScreen -> "Grocery List"
                MealScreen -> "Meals"
                SettingsScreen -> "Settings"
                RecipeScreen -> "Recipes"
                FoodListScreen -> "Saved Foods"
            }
    }
}
