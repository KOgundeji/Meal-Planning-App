package com.kunle.aisle9b.navigation

enum class GroceryScreens {
    SplashScreen,
    GroceryListScreen,
    MealScreen,
    SettingsScreen,
    RecipeScreen,
    CustomListScreen,
    AddMealsScreen,
    AddCustomListScreen;

    companion object {
        fun headerTitle(header: GroceryScreens): String =
            when (header) {
                SplashScreen -> ""
                GroceryListScreen -> "Grocery List"
                MealScreen -> "Meals"
                SettingsScreen -> "Settings"
                RecipeScreen -> "Recipes"
                CustomListScreen -> "Saved Grocery Lists"
                AddMealsScreen -> "Add New Meal"
                AddCustomListScreen -> "Create Custom Grocery List"
            }
    }
}
