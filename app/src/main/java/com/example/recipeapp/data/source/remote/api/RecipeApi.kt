package com.example.recipeapp.data.source.remote.api

import com.example.recipeapp.data.models.Recipe
import com.example.recipeapp.data.models.RecipeResponse
import com.example.recipeapp.data.models.SpecificRecipes
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApi {
    @GET("recipes/random")
    suspend fun getRandomRecipes(
        @Query("number") recipeAmount: Int
    ): RecipeResponse

    @GET("recipes/{id}/information")
    suspend fun getRecipeById(
        @Path("id") id: Long
    ): Recipe

    @GET("recipes/complexSearch")
    suspend fun getSpecificRecipe(
        @Query("query") query: String
    ): SpecificRecipes
}