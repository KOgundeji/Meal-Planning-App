package com.kunle.aisle9b.api

import com.kunle.aisle9b.keys.spoonacular_key
import com.kunle.aisle9b.models.apiModels.instructionModels.Instructions
import com.kunle.aisle9b.models.apiModels.queryModels.RawJSON
import com.kunle.aisle9b.models.apiModels.recipeModels.BatchRecipes
import com.kunle.aisle9b.api.apiModels.recipeModels.Recipe
import com.kunle.aisle9b.api.apiModels.recipeModels.RecipeRawAPIData
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface RecipeAPI {
    @GET(value = "recipes/random")
    suspend fun getRandomRecipes(
        @Header(value = "x-api-key") apiKey: String = spoonacular_key,
        @Query(value = "limitLicense") limitLicense: Boolean = true,
        @Query(value = "tags") tags: String,
        @Query(value = "number") number: Int = 30
    ): RecipeRawAPIData

    @GET(value = "recipes/{id}/information")
    suspend fun getRecipe(
        @Header(value = "x-api-key") apiKey: String = spoonacular_key,
        @Path(value = "id") id: Int,
        @Query(value = "includeNutrition") includeNutrition: Boolean = false
    ): Recipe

    @GET(value = "recipes/informationBulk")
    suspend fun getBatchRecipes(
        @Header(value = "x-api-key") apiKey: String = spoonacular_key,
        @Query(value = "ids") ids: String,
        @Query(value = "includeNutrition") includeNutrition: Boolean = false
    ): BatchRecipes

    @GET(value = "recipes/complexSearch")
    suspend fun getSearchResults(
        @Header(value = "x-api-key") apiKey: String = spoonacular_key,
        @Query(value = "query") query: String,
        @Query(value = "addRecipeInformation") addRecipeInformation: Boolean = true,
        @Query(value = "number") number: Int = 75,
        @Query(value = "limitLicense") limitLicense: Boolean = true
    ): RawJSON

    @GET(value = "recipes/{id}/analyzedInstructions")
    suspend fun getInstructions(
        @Header(value = "x-api-key") apiKey: String = spoonacular_key,
        @Path(value = "id") id: Int,
        @Query(value = "stepBreakdown") stepBreakdown: Boolean = false
    ): Instructions
}
