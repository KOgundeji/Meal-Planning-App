package com.kunle.aisle9b.api

import com.kunle.aisle9b.keys.X_RapidAPI_Key
import com.kunle.aisle9b.models.apiModels.trendingRecipeModels.TrendingRawAPIData
import com.kunle.aisle9b.util.Constants
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface TrendingRecipeAPI {
    @GET(value = "feeds/list")
    suspend fun getTrendingRecipes(
        @Header(value = "X-RapidAPI-Key") apiKey: String = X_RapidAPI_Key,
        @Header(value = "X-RapidAPI-Host") apiHost: String = Constants.RECIPE_API_HOST,
        @Query(value = "size") size: Int = 5,
        @Query(value = "timezone") timezone: String = "+0000",
        @Query(value = "vegetarian") vegetarian: Boolean,
        @Query(value = "from") from: Int = 0
    ): TrendingRawAPIData

}