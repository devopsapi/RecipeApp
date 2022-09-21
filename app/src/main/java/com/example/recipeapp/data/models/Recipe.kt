package com.example.recipeapp.data.models

data class Recipe(
    val aggregateLikes: Int = 0,
    val analyzedInstructions: List<AnalyzedInstruction>,
    val extendedIngredients: List<ExtendedIngredient>,
    val id: Int = 0,
    val image: String = "",
    val instructions: String = "",
    val readyInMinutes: Int = 0,
    val servings: Int = 0,
    val summary: String = "",
    val title: String = "",
    var calories: Int = 0,
)