package com.example.recipeapp.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.models.Recipe
import com.example.recipeapp.data.repositories.RecipeRepository
import com.example.recipeapp.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val recipeRepository: RecipeRepository) :
    ViewModel() {

    private val _recipes = MutableLiveData<List<Recipe>>()
    val recipes: LiveData<List<Recipe>> = _recipes

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _progressBar = MutableLiveData<Boolean>()
    val progressBar: LiveData<Boolean> = _progressBar

    fun getRandomRecipes(recipeNumber: Int = 100) {
        _progressBar.value = true
        viewModelScope.launch {
            when (val result = recipeRepository.getRandomRecipe(recipeNumber)) {
                is Result.Success -> _recipes.value = result.data.recipes
                is Result.Error -> _error.value = result.exception.toString()
            }
            _progressBar.value = false
        }
    }

    fun getSpecificRecipe(query: String) {
        _progressBar.value = true
        viewModelScope.launch {
            when (val result = recipeRepository.getSpecificRecipe(query)) {
                is Result.Success -> _recipes.value = result.data.results
                is Result.Error -> _error.value = result.exception.toString()
            }
            _progressBar.value = false
        }
    }
}