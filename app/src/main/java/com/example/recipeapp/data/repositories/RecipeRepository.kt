package com.example.recipeapp.data.repositories

import com.example.recipeapp.data.models.Recipe
import com.example.recipeapp.data.models.RecipeResponse
import com.example.recipeapp.data.models.SpecificRecipes
import com.example.recipeapp.utils.Result

interface RecipeRepository {
    suspend fun getRandomRecipe(recipeAmount: Int): Result<RecipeResponse>
    suspend fun getRecipeById(id: Int): Result<Recipe>
    suspend fun getSpecificRecipe(query: String): Result<SpecificRecipes>
}