package com.example.recipeapp.data.models

data class CalculatedIngredient(
    val caloriesAmount: Int,
    val extendedIngredient: List<ExtendedIngredient>
)