package com.kunle.aisle9b.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kunle.aisle9b.models.*
import com.kunle.aisle9b.util.UUIDConverter

@Database(
    entities = [Food::class, GroceryList::class, Meal::class, AppSettings::class, ListFoodMap::class, MealFoodMap::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(UUIDConverter::class)
abstract class ShoppingRoomDB : RoomDatabase() {

    abstract fun foodDao(): FoodDao
    abstract fun listDao(): ListDao
    abstract fun mealDao(): MealDao
    abstract fun settingsDao(): SettingsDao
    abstract fun listWithGroceriesDao(): ListWithGroceriesDao
    abstract fun mealWithIngredientsDao(): MealWithIngredientsDao
}