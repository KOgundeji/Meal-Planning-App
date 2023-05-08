package com.kunle.aisle9b.api

import com.kunle.aisle9b.keys.spoonacular_key
import com.kunle.aisle9b.models.apiModels.recipeModels.Recipe
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface RecipeAPI {
    @GET(value = "recipes/{id}/information")
    suspend fun getRecipes(
        @Header(value = "x-api-key") apiKey: String = spoonacular_key,
        @Path(value = "id") id: Int,
        @Query(value = "includeNutrition") includeNutrition: Boolean = false
    ): Recipe
}
