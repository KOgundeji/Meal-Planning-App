package com.kunle.aisle9b.api

import com.kunle.aisle9b.keys.X_RapidAPI_Key
import com.kunle.aisle9b.models.apiModels.searchedRecipeModels.SearchedRawAPIData
import com.kunle.aisle9b.util.Constants
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface SearchedRecipeAPI {
    @GET(value = "recipes/list")
    suspend fun getSearchedRecipes(
        @Header(value = "X-RapidAPI-Key") apiKey: String = X_RapidAPI_Key,
        @Header(value = "X-RapidAPI-Host") apiHost: String = Constants.RECIPE_API_HOST,
        @Query(value = "from") from: Int = 0,
        @Query(value = "size") size: Int = 10,
        @Query(value = "tags") tags: String,
        @Query(value = "q") query: String = ""
    ): SearchedRawAPIData
}
