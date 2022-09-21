package com.example.recipeapp.ui.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipeapp.ui.util.Routes
import com.example.recipeapp.ui.util.UiEvent
import com.example.recipeapp.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val collectionReference: CollectionReference
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
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { firebaseAuthTask ->
                if (firebaseAuthTask.isSuccessful) {
                    val userId = firebaseAuth.uid ?: ""
                    val documentReference = collectionReference.document(userId)
                    val userMap = HashMap<String, Any>()
                    userMap[Constants.EMAIL_FIELD] = email
                    documentReference.set(userMap).addOnCompleteListener { firebaseFirestoreTask ->
                        if (firebaseFirestoreTask.isSuccessful) {
                            _uiEvent.postValue(UiEvent.Navigate(Routes.NAVIGATE_TO_HOME))
                        } else {
                            Log.i("TAG", firebaseFirestoreTask.exception.toString())
                            _uiEvent.value = UiEvent.Loading(isLoading = false)
                            _uiEvent.value =
                                UiEvent.ShowToast(firebaseFirestoreTask.exception.toString())
                        }
                    }
                } else {
                    Log.i("TAG", firebaseAuthTask.exception.toString())
                    _uiEvent.value = UiEvent.Loading(isLoading = false)
                    _uiEvent.value = UiEvent.ShowToast(firebaseAuthTask.exception.toString())
                }
            }
    }
}