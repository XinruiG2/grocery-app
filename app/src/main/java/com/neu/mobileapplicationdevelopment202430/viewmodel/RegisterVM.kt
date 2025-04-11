package com.neu.mobileapplicationdevelopment202430.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.neu.mobileapplicationdevelopment202430.model.FoodRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterVM(private val repository: FoodRepository) : ViewModel() {
    private val _signupStatus = MutableLiveData<Boolean?>()
    val signupStatus: MutableLiveData<Boolean?> get() = _signupStatus
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun validSignUpOrNot(username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val signupResponse = repository.signup(username, password)
                if (signupResponse != null && signupResponse.message == "sign-up successful") {
                    val userId = signupResponse.user_id
                    Log.d("RegisterVM", "Sign-up successful. User ID: $userId")
                    withContext(Dispatchers.Main) {
                        _signupStatus.value = true
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        _errorMessage.value = "user already exists"
                    }
                }
            } catch (e: Exception) {
//                Log.e("RegisterVM", "FOUND AN ERROR!: ${e.message}")

                withContext(Dispatchers.Main) {
                    _errorMessage.value = e.toString()
                }
            } finally {
                withContext(Dispatchers.Main) {
                    _isLoading.value = false
                }
            }
        }
    }

    fun resetRegisterStatus() {
        _signupStatus.value = null
    }
}