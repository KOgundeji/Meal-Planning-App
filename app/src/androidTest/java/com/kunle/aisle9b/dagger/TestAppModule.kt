package com.kunle.aisle9b.dagger

import android.content.Context
import androidx.room.Room
import com.kunle.aisle9b.api.RecipeAPI
import com.kunle.aisle9b.data.*
import com.kunle.aisle9b.repositories.customLists.CustomListRepository
import com.kunle.aisle9b.repositories.customLists.CustomListRepositoryImpl
import com.kunle.aisle9b.repositories.general.GeneralRepository
import com.kunle.aisle9b.repositories.general.GeneralRepositoryImpl
import com.kunle.aisle9b.repositories.groceries.GroceryRepository
import com.kunle.aisle9b.repositories.groceries.GroceryRepositoryImpl
import com.kunle.aisle9b.repositories.meals.MealRepository
import com.kunle.aisle9b.repositories.meals.MealRepositoryImpl
import com.kunle.aisle9b.repositories.recipes.RecipeRepository
import com.kunle.aisle9b.repositories.recipes.RecipeRepositoryImpl
import com.kunle.aisle9b.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

//dependency injection
@InstallIn(SingletonComponent::class)
@Module
object TestAppModule {

    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): ShoppingRoomDB =
        Room.inMemoryDatabaseBuilder(
            context,
            ShoppingRoomDB::class.java
        ).build()

    @Singleton
    @Provides
    fun provideRecipeAPI(): RecipeAPI {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RecipeAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideGeneralRepository(db: ShoppingRoomDB) =
        GeneralRepositoryImpl(
            db.groceryDao(),
            db.mealWithIngredientsDao(),
            db.mealDao(),
            db.instructionDao(),
            db.settingsDao()
        ) as GeneralRepository

    @Singleton
    @Provides
    fun provideCustomListRepository(db: ShoppingRoomDB) =
        CustomListRepositoryImpl(
            db.listDao(),
            db.listWithGroceriesDao(),
            db.groceryDao()
        ) as CustomListRepository

    @Singleton
    @Provides
    fun provideGroceryRepository(db: ShoppingRoomDB) =
        GroceryRepositoryImpl(db.groceryDao()) as GroceryRepository

    @Singleton
    @Provides
    fun provideMealRepository(
        db: ShoppingRoomDB,
        recipeAPI: RecipeAPI
    ) =
        MealRepositoryImpl(
            db.groceryDao(),
            db.mealDao(),
            db.instructionDao(),
            db.mealWithIngredientsDao(),
            recipeAPI
        ) as MealRepository

    @Singleton
    @Provides
    fun provideRecipeRepository(
        db: ShoppingRoomDB,
        recipeAPI: RecipeAPI,
    ) =
        RecipeRepositoryImpl(
            db.mealDao(),
            recipeAPI,
            db.mealWithIngredientsDao()
        ) as RecipeRepository
}

