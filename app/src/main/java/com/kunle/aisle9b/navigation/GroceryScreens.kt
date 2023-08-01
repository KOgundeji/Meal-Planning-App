package com.kunle.aisle9b.navigation

enum class GroceryScreens {
    SplashScreen,
    GroceryListScreen,
    MealScreen,
    SettingsScreen,
    RecipeScreen,
    CustomListScreen,
    AddNewMealScreen,
    AddNewCustomListScreen,
    RecipeDetailsScreen,
    MealDetailsScreen;

    companion object {
        fun headerTitle(header: GroceryScreens): String =
            when (header) {
                SplashScreen -> ""
                GroceryListScreen -> "Grocery List"
                MealScreen -> "Meals"
                SettingsScreen -> "Settings"
                RecipeScreen -> "Recipes"
                CustomListScreen -> "Saved Grocery Lists"
                AddNewMealScreen -> "Add New Meal"
                AddNewCustomListScreen -> "Create Custom Grocery List"
                RecipeDetailsScreen -> "Recipe Details"
                MealDetailsScreen -> "Meal Details"
            }
    }
}
