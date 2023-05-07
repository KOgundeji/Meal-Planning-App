package com.kunle.aisle9b.dagger

import android.content.Context
import androidx.room.Room
import com.kunle.aisle9b.api.SearchedRecipeAPI
import com.kunle.aisle9b.api.TrendingRecipeAPI
import com.kunle.aisle9b.data.*
import com.kunle.aisle9b.models.apiModels.searchedRecipeModels.SearchedRawAPIData
import com.kunle.aisle9b.models.apiModels.trendingRecipeModels.TrendingRawAPIData
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
    fun provideSearchedRecipeAPI(): SearchedRecipeAPI {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SearchedRecipeAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideTrendingRecipeAPI(): TrendingRecipeAPI {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TrendingRecipeAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideFoodDao(shoppingRoomDB: ShoppingRoomDB): FoodDao =
        shoppingRoomDB.foodDao()

    @Singleton
    @Provides
    fun provideListDao(shoppingRoomDB: ShoppingRoomDB): ListDao =
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
    fun provideLWGDao(shoppingRoomDB: ShoppingRoomDB): ListWithGroceriesDao =
        shoppingRoomDB.listWithGroceriesDao()

    @Singleton
    @Provides
    fun provideMWIDao(shoppingRoomDB: ShoppingRoomDB): MealWithIngredientsDao =
        shoppingRoomDB.mealWithIngredientsDao()


}

