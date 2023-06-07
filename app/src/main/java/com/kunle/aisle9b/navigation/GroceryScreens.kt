package com.kunle.aisle9b.navigation

enum class GroceryScreens {
    SplashScreen,
    GroceryListScreen,
    MealScreen,
    SettingsScreen,
    RecipeScreen,
    CustomListScreen,
    AddMealsScreenTEST,
    AddCustomListScreen,
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
                AddMealsScreenTEST -> "Add New Meal"
                AddCustomListScreen -> "Create Custom Grocery List"
                RecipeDetailsScreen -> "Recipe Details"
                MealDetailsScreen -> "Meal Details"
            }
    }
}
