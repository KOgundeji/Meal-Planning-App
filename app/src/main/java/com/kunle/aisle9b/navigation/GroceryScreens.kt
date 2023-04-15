package com.kunle.aisle9b.navigation

enum class GroceryScreens {
    ListScreen,
    MealScreen,
    SettingsScreen,
    RecipeScreen,
    FoodListScreen,
    PremadeListScreen;

    companion object {
        fun headerTitle(header: GroceryScreens): String =
            when (header) {
                ListScreen -> "Grocery List"
                MealScreen -> "Meals"
                SettingsScreen -> "Settings"
                RecipeScreen -> "Recipes"
                FoodListScreen -> "Food Library"
                PremadeListScreen -> "Saved Grocery Lists"
            }
    }
}
