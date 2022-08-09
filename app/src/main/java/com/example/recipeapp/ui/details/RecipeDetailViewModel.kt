package com.example.recipeapp.ui.details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.models.ExtendedIngredient
import com.example.recipeapp.data.models.CalculatedIngredient
import com.example.recipeapp.data.models.Recipe
import com.example.recipeapp.data.repositories.RecipeRepository
import com.example.recipeapp.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.ArrayList
import javax.inject.Inject

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(private val repository: RecipeRepository) :
    ViewModel() {

    private val TAG = "RECIPE_DETAILS_TAG"

    init {
        Log.i(TAG, "recipeDetailViewModel init")
    }

    private val _recipe = MutableLiveData<Recipe>()
    val recipe: LiveData<Recipe> = _recipe

    private val _calculatedIngredient = MutableLiveData<CalculatedIngredient>()
    val calculatedIngredient: LiveData<CalculatedIngredient> = _calculatedIngredient

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private var defaultServings = 0
    private var defaultCalories = 0
    private var defaultIngredients = emptyList<ExtendedIngredient>()
    private var previousUserInput = 0

    fun getRecipeById(id: Int) {
        viewModelScope.launch {
            when (val result = repository.getRecipeById(id)) {
                is Result.Success -> {

                    // Convert HTML tags in summary text
                    val regex = "(\\d+) calories".toRegex()
                    val match = regex.find(result.data.summary)
                    val caloriesAmount = match?.value?.split(" ")
                    result.data.calories = caloriesAmount?.get(0)?.toInt() ?: 0

                    _calculatedIngredient.postValue(
                        CalculatedIngredient(
                            result.data.calories,
                            result.data.extendedIngredients
                        )
                    )

                    _recipe.postValue(result.data)

                    // Save values for future user calculation
                    defaultServings = result.data.servings
                    defaultIngredients = result.data.extendedIngredients
                    defaultCalories = caloriesAmount?.get(0)?.toInt() ?: 0
                }
                is Result.Error -> _error.postValue(result.exception.toString())
            }
        }
    }

    fun calculateNewCaloriesAndIngredientsAmount(userInputServings: Int) {
        if (userInputServings != previousUserInput) {
            previousUserInput = userInputServings
            viewModelScope.launch(Dispatchers.Default) {
                val newCalories = (defaultCalories / defaultServings) * userInputServings
                val ingredientWithNewAmount = ArrayList<ExtendedIngredient>()

                for (i in defaultIngredients.indices) {
                    val newExtendedIngredient = ExtendedIngredient(
                        aisle = defaultIngredients[i].aisle,
                        amount = (defaultIngredients[i].amount / defaultServings.toDouble()) * userInputServings,
                        consistency = defaultIngredients[i].consistency,
                        id = defaultIngredients[i].id,
                        image = defaultIngredients[i].image,
                        measures = defaultIngredients[i].measures,
                        meta = defaultIngredients[i].meta,
                        name = defaultIngredients[i].name,
                        nameClean = defaultIngredients[i].nameClean,
                        original = defaultIngredients[i].original,
                        originalName = defaultIngredients[i].originalName,
                        unit = defaultIngredients[i].unit
                    )
                    ingredientWithNewAmount.add(newExtendedIngredient)
                }

                _calculatedIngredient.postValue(
                    CalculatedIngredient(
                        newCalories,
                        ingredientWithNewAmount
                    )
                )
            }
        }
    }
}