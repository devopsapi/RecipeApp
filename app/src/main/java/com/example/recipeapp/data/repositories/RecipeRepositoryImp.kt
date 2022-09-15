package com.example.recipeapp.data.repositories

import com.example.recipeapp.data.models.Recipe
import com.example.recipeapp.data.models.SpecificRecipes
import com.example.recipeapp.data.source.remote.RecipeRemoteDataSource
import com.example.recipeapp.utils.Result
import javax.inject.Inject

class RecipeRepositoryImp @Inject constructor(
    private val recipeRemoteDataSource: RecipeRemoteDataSource,
) : RecipeRepository {

    override suspend fun getRecipes(
        recipeAmount: Int,
        forceUpdate: Boolean
    ): Result<List<Recipe>> {
        return recipeRemoteDataSource.getRecipes(recipeAmount)
    }

    override suspend fun getRecipeById(id: Int): Result<Recipe> {
        return recipeRemoteDataSource.getRecipeById(id)
    }

    override suspend fun getSpecificRecipe(query: String): Result<SpecificRecipes> {
        return recipeRemoteDataSource.getSpecificRecipes(query)
    }
}