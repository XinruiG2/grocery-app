package com.neu.mobileapplicationdevelopment202430.model

import android.content.Context
import android.content.SharedPreferences

class UserInformation(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun saveUserId(userId: Int) {
        sharedPreferences.edit().putInt("user_id", userId).apply()
    }

    fun getUserId(): Int {
        return sharedPreferences.getInt("user_id", -1)
    }
}
