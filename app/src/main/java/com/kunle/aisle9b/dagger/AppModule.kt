package com.kunle.aisle9b.dagger

import android.content.Context
import androidx.room.Room
import com.kunle.aisle9b.api.RecipeAPI
import com.kunle.aisle9b.data.*
import com.kunle.aisle9b.repositories.customLists.CustomListRepository
import com.kunle.aisle9b.repositories.customLists.CustomListRepositoryImpl
import com.kunle.aisle9b.repositories.general.GeneralRepositoryImpl
import com.kunle.aisle9b.repositories.general.GeneralRepository
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
object AppModule {

    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): ShoppingRoomDB =
        Room.databaseBuilder(
            context = context,
            klass = ShoppingRoomDB::class.java,
            name = "shopping_db"
        ).fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun provideGeneralRepositoryImpl(
        groceryDao: GroceryDao,
        mealDao: MealDao,
        settingsDao: SettingsDao,
        instructionDao: InstructionDao,
        mealWithIngredientsDao: MealWithIngredientsDao
    ) = GeneralRepositoryImpl(
        groceryDao,
        mealWithIngredientsDao,
        mealDao,
        instructionDao,
        settingsDao
    ) as GeneralRepository

    @Singleton
    @Provides
    fun provideCustomListRepositoryImpl(
        customListDao: CustomListDao,
        listWithGroceriesDao: ListWithGroceriesDao,
        groceryDao: GroceryDao
    ) = CustomListRepositoryImpl(
        customListDao, listWithGroceriesDao, groceryDao
    ) as CustomListRepository

    @Singleton
    @Provides
    fun provideGroceryRepositoryImpl(groceryDao: GroceryDao) =
        GroceryRepositoryImpl(groceryDao) as GroceryRepository

    @Singleton
    @Provides
    fun provideMealRepositoryImpl(
        groceryDao: GroceryDao,
        mealDao: MealDao,
        instructionDao: InstructionDao,
        mealWithIngredientsDao: MealWithIngredientsDao,
        recipeAPI: RecipeAPI
    ) =
        MealRepositoryImpl(
            groceryDao,
            mealDao,
            instructionDao,
            mealWithIngredientsDao,
            recipeAPI
        ) as MealRepository

    @Singleton
    @Provides
    fun provideRecipeRepositoryImpl(
        mealDao: MealDao,
        recipeAPI: RecipeAPI,
        mealWithIngredientsDao: MealWithIngredientsDao
    ) =
        RecipeRepositoryImpl(
            mealDao,
            recipeAPI,
            mealWithIngredientsDao
        ) as RecipeRepository

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
    fun provideFoodDao(shoppingRoomDB: ShoppingRoomDB): GroceryDao =
        shoppingRoomDB.foodDao()

    @Singleton
    @Provides
    fun provideListDao(shoppingRoomDB: ShoppingRoomDB): CustomListDao =
        shoppingRoomDB.listDao()

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
    fun provideInstructionDao(shoppingRoomDB: ShoppingRoomDB): InstructionDao =
        shoppingRoomDB.instructionDao()

    @Singleton
    @Provides
    fun provideLWGDao(shoppingRoomDB: ShoppingRoomDB): ListWithGroceriesDao =
        shoppingRoomDB.listWithGroceriesDao()

    @Singleton
    @Provides
    fun provideMWIDao(shoppingRoomDB: ShoppingRoomDB): MealWithIngredientsDao =
        shoppingRoomDB.mealWithIngredientsDao()
}

