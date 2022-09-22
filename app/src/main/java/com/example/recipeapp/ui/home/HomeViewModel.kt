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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val recipeRepository: RecipeRepository) :
    ViewModel() {

    private val _recipes = MutableLiveData<List<Recipe>>()
    val recipes: LiveData<List<Recipe>> = _recipes

    private val _error = MutableSharedFlow<String>()
    val error: SharedFlow<String> = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    init {
        getRecipes()
    }

    fun getRecipes(recipeNumber: Int = 100) {
        _loading.value = true
        viewModelScope.launch {
            when (val result = recipeRepository.getRecipes(recipeNumber, false)) {
                is Result.Success -> _recipes.value = result.data
                is Result.Error -> {
                    Log.i("TAG", "Error: ${result.exception}")
                   _error.emit(result.exception.toString())
                }
            }
            _loading.value = false
        }
    }

    fun getSpecificRecipe(query: String) {
        _loading.value = true
        viewModelScope.launch {
            when (val result = recipeRepository.getSpecificRecipe(query)) {
                is Result.Success -> _recipes.value = result.data.results
                is Result.Error -> _error.emit(result.exception.toString())
            }
            _loading.value = false
        }
    }
}