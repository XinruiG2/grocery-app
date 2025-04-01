package com.neu.mobileapplicationdevelopment202430.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegisterVM : ViewModel() {
    private val _registerStatus = MutableLiveData<Boolean?>()
    val registerStatus: MutableLiveData<Boolean?> get() = _registerStatus
}