package com.example.recipeapp.data.models

data class SpecificRecipes(
    val results: List<Recipe>,
    val offset: Int,
    val number: Int,
)
