package com.kunle.aisle9b.navigation

enum class GroceryScreens {
    ListScreen,
    MealScreen,
    SettingsScreen,
    RecipeScreen,
    FoodListScreen;

    companion object {
        fun fullName(header: GroceryScreens): String =
            when (header) {
                ListScreen -> "Grocery List"
                MealScreen -> "Meals"
                SettingsScreen -> "Settings"
                RecipeScreen -> "Recipes"
                FoodListScreen -> "Food Library"
            }
    }
}
