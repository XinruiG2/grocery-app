package com.neu.mobileapplicationdevelopment202430.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData

class LoginVM : ViewModel() {
    private val _loginStatus = MutableLiveData<Boolean?>()
    val loginStatus: MutableLiveData<Boolean?> get() = _loginStatus

    fun validUserOrNot(username: String, password: String) {
        _loginStatus.value = username == "admin" && password == "admin"
    }

    fun resetLoginStatus() {
        _loginStatus.value = null
    }
}