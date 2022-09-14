package com.example.recipeapp.ui.util

sealed class UiEvent {
    data class Loading(val isLoading: Boolean) : UiEvent()
    data class Navigate(val route: String) : UiEvent()
    data class ShowToast(val message: String) : UiEvent()
}