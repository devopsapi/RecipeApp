package com.example.recipeapp.ui.favourite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.models.Recipe
import com.example.recipeapp.data.repositories.RecipeRepository
import com.example.recipeapp.utils.Constants
import com.example.recipeapp.utils.Result
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class FavouriteRecipesViewModel @Inject constructor(
    private val repository: RecipeRepository,
    private val firebaseAuth: FirebaseAuth,
    private val collectionReference: CollectionReference
) : ViewModel() {

    private val _recipes = MutableLiveData<List<Recipe>>()
    val recipes: LiveData<List<Recipe>> = _recipes

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _noFavourites = MutableLiveData(false)
    val noFavourites: LiveData<Boolean> = _noFavourites

    init {
        getFavourites(object : FirestoreCallback {
            override fun onCallBack(ids: List<Long>) {
                viewModelScope.launch {
                    loadRecipes(ids)
                }
            }
        })
    }

    private fun getFavourites(firestoreCallback: FirestoreCallback) {
        _loading.value = true
        viewModelScope.launch {
            val documentReference = collectionReference.document(firebaseAuth.uid ?: "")
            documentReference
                .collection(Constants.FAVORITE_COLLECTION)
                .addSnapshotListener { value, error ->
                    if (value != null) {
                        if (value.size() == 0) {
                            _noFavourites.value = true
                            _loading.value = false
                            _recipes.value = emptyList()
                        } else {
                            _noFavourites.value = false
                            val ids = mutableListOf<Long>()
                            for (recipeDoc in value) {
                                val recipeId = (recipeDoc.get(Constants.RECIPE_ID_FIELD)) as Long
                                ids.add(recipeId)
                            }
                            firestoreCallback.onCallBack(ids.toList())
                        }
                    }
                    if (error != null) {
                        _error.value = error.message
                        _loading.value = false
                    }
                }
        }
    }

    suspend fun loadRecipes(recipeIds: List<Long>) = withContext(Dispatchers.IO) {

        val loadedRecipes = mutableListOf<Deferred<Result<Recipe>>>()

        for (recipeId in recipeIds) {
            val recipe = async {
                repository.getRecipeById(recipeId)
            }
            loadedRecipes.add(recipe)
        }

        val resultRecipes = loadedRecipes.awaitAll()
        val recipes = mutableListOf<Recipe>()

        for (resultRecipe in resultRecipes) {
            when (resultRecipe) {
                is Result.Success -> recipes.add(resultRecipe.data)
                is Result.Error -> continue
            }
        }

        _recipes.postValue(recipes)
        _loading.postValue(false)
    }
}

interface FirestoreCallback {
    fun onCallBack(ids: List<Long>)
}