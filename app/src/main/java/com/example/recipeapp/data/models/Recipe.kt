package com.example.recipeapp.data.models

import java.util.ArrayList

data class Recipe(
    val aggregateLikes: Int = 0,
    val analyzedInstructions: List<AnalyzedInstruction> = ArrayList<AnalyzedInstruction>(),
    val extendedIngredients: List<ExtendedIngredient> =  ArrayList<ExtendedIngredient>(),
    val id: Int = 0,
    val image: String = "",
    val instructions: String = "",
    val readyInMinutes: Int = 0,
    val servings: Int = 0,
    val summary: String = "",
    val title: String = "",
    var calories: Int = 0,
)