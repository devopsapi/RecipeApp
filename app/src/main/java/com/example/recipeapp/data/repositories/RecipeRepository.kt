package com.example.recipeapp.data.repositories

import com.example.recipeapp.data.models.Recipe
import com.example.recipeapp.data.models.SpecificRecipes
import com.example.recipeapp.utils.Result

interface RecipeRepository {
    suspend fun getRecipes(recipeAmount: Int, forceUpdate:Boolean): Result<List<Recipe>>
    suspend fun getRecipeById(id: Long): Result<Recipe>
    suspend fun getSpecificRecipe(query: String): Result<SpecificRecipes>
}