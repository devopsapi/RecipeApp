package com.example.recipeapp.ui.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipeapp.ui.util.Routes
import com.example.recipeapp.ui.util.UiEvent
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _uiEvent = MutableLiveData<UiEvent>()
    val uiEvent: LiveData<UiEvent> = _uiEvent

    fun loginUser(email: String, password: String) {
        _uiEvent.value = UiEvent.Loading(isLoading = true)
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _uiEvent.postValue(UiEvent.Navigate(Routes.NAVIGATE_TO_HOME))
            } else {
                Log.i("TAG", task.exception.toString())
                _uiEvent.value = UiEvent.Loading(isLoading = false)
                _uiEvent.value = UiEvent.ShowToast(task.exception.toString())
            }
        }
    }

    fun registerUser(email: String, password: String) {
        _uiEvent.value = UiEvent.Loading(isLoading = true)
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _uiEvent.postValue(UiEvent.Navigate(Routes.NAVIGATE_TO_HOME))
            } else {
                Log.i("TAG", task.exception.toString())
                _uiEvent.value = UiEvent.Loading(isLoading = false)
                _uiEvent.value = UiEvent.ShowToast(task.exception.toString())
            }
        }
    }
}