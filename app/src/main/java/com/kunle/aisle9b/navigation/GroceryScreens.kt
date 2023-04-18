package com.kunle.aisle9b.navigation

enum class GroceryScreens {
    ListScreen,
    MealScreen,
    SettingsScreen,
    RecipeScreen,
    PremadeListScreen,
    AddMealsScreen,
    AddCustomListScreen;

    companion object {
        fun headerTitle(header: GroceryScreens): String =
            when (header) {
                ListScreen -> "Grocery List"
                MealScreen -> "Meals"
                SettingsScreen -> "Settings"
                RecipeScreen -> "Recipes"
                PremadeListScreen -> "Saved Grocery Lists"
                AddMealsScreen -> "Add New Meal"
                AddCustomListScreen -> "Create Custom Grocery List"
            }
    }
}
