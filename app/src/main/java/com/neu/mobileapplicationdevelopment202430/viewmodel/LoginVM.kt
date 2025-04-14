package com.neu.mobileapplicationdevelopment202430.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.neu.mobileapplicationdevelopment202430.model.FoodRepository
import com.neu.mobileapplicationdevelopment202430.model.UserInformation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginVM(private val repository: FoodRepository) : ViewModel() {
    private val _loginStatus = MutableLiveData<Boolean?>()
    val loginStatus: MutableLiveData<Boolean?> get() = _loginStatus
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun validUserOrNot(username: String, password: String) {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val loginResponse = repository.login(username, password)
                if (loginResponse != null && loginResponse.message == "login successful") {
                    val userId = loginResponse.user_id
//                    Log.d("LoginVM", "Login successful. User ID: $userId")
                    withContext(Dispatchers.Main) {
                        _loginStatus.value = true
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        _errorMessage.value = "invalid credentials"
                    }
                }
            } catch (e: Exception) {
//                Log.e("LoginVM", "FOUND AN ERROR!: ${e.message}")

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


    fun resetLoginStatus() {
        _loginStatus.value = null
    }
}