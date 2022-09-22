package com.example.recipeapp.data.source.remote

import com.example.recipeapp.data.models.Recipe
import com.example.recipeapp.data.models.SpecificRecipes
import com.example.recipeapp.data.source.remote.api.RecipeApi
import com.example.recipeapp.utils.Constants
import com.example.recipeapp.utils.Result
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import javax.inject.Inject

class RecipeNetworkDataSourceImp @Inject constructor(
    private val recipeApi: RecipeApi
) :
    RecipeRemoteDataSource {

    override suspend fun getRecipes(recipeAmount: Int): Result<List<Recipe>> {
        return try {
            Result.Success(recipeApi.getRandomRecipes(recipeAmount).recipes)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getRecipeById(recipeId: Long): Result<Recipe> {
        return try {
            Result.Success(recipeApi.getRecipeById(recipeId))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getSpecificRecipes(query: String): Result<SpecificRecipes> {
        return try {
            Result.Success(recipeApi.getSpecificRecipe(query))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}