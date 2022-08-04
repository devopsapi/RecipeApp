package com.example.recipeapp.data.repositories

import com.example.recipeapp.data.api.RecipeApi
import com.example.recipeapp.data.models.Recipe
import com.example.recipeapp.data.models.RecipeResponse
import com.example.recipeapp.data.models.SpecificRecipes
import com.example.recipeapp.utils.Result
import javax.inject.Inject

class RecipeRepositoryImp @Inject constructor(private val recipeApi: RecipeApi) : RecipeRepository {
    override suspend fun getRandomRecipe(recipeAmount: Int): Result<RecipeResponse> {
        return try {
            Result.Success(recipeApi.getRandomRecipes(recipeAmount))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getRecipeById(id: Int): Result<Recipe> {
        return try {
            Result.Success(recipeApi.getRecipeById(id))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getSpecificRecipe(query: String): Result<SpecificRecipes> {
        return try {
            Result.Success(recipeApi.getSpecificRecipe(query))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}