package com.kunle.aisle9b.dagger

import android.content.Context
import androidx.room.Room
import com.kunle.aisle9b.data.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//dependency injection
@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): ShoppingRoomDB =
        Room.databaseBuilder(
            context = context,
            klass = ShoppingRoomDB::class.java,
            name = "shopping_db"
        ).fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun provideFoodDao(shoppingRoomDB: ShoppingRoomDB): FoodDao =
        shoppingRoomDB.foodDao()

    @Singleton
    @Provides
    fun provideMealDao(shoppingRoomDB: ShoppingRoomDB): MealDao =
        shoppingRoomDB.mealDao()

    @Singleton
    @Provides
    fun provideSettingsDao(shoppingRoomDB: ShoppingRoomDB): SettingsDao =
        shoppingRoomDB.settingsDao()

    @Singleton
    @Provides
    fun provideMWIDao(shoppingRoomDB: ShoppingRoomDB): MealWithIngredientsDao =
        shoppingRoomDB.mealWithIngredientsDao()


}