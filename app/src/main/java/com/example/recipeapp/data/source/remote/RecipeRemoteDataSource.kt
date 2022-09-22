package com.example.recipeapp.data.source.remote

import com.example.recipeapp.data.models.Recipe
import com.example.recipeapp.data.models.SpecificRecipes
import com.example.recipeapp.utils.Result

interface RecipeRemoteDataSource {
    suspend fun getRecipes(recipeAmount: Int): Result<List<Recipe>>
    suspend fun getRecipeById(recipeId: Long): Result<Recipe>
    suspend fun getSpecificRecipes(query: String): Result<SpecificRecipes>
}