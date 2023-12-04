package com.kunle.aisle9b.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kunle.aisle9b.models.*
import com.kunle.aisle9b.util.UriConverter

@Database(
    entities = [Grocery::class, Food::class,
        GroceryList::class, Meal::class, AppSettings::class,
        ListFoodMap::class, MealFoodMap::class, Instruction::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(UriConverter::class)
abstract class ShoppingRoomDB : RoomDatabase() {

    abstract fun groceryDao(): GroceryDao
    abstract fun listDao(): CustomListDao
    abstract fun mealDao(): MealDao
    abstract fun settingsDao(): SettingsDao
    abstract fun instructionDao(): InstructionDao
    abstract fun listWithGroceriesDao(): ListWithGroceriesDao
    abstract fun mealWithIngredientsDao(): MealWithIngredientsDao
}