package com.kunle.aisle9b.api

import com.kunle.aisle9b.keys.spoonacular_key
import com.kunle.aisle9b.models.apiModels.recipeModels.RecipeRawAPIData
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface RandomRecipeAPI {
    @GET(value = "recipes/random")
    suspend fun getRandomRecipes(
        @Header(value = "x-api-key") apiKey: String = spoonacular_key,
        @Query(value = "limitLicense") limitLicense: Boolean = true,
        @Query(value = "tags") tags: String,
        @Query(value = "number") number: Int = 30
    ): RecipeRawAPIData
}
