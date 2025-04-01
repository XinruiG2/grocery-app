package com.neu.mobileapplicationdevelopment202430.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.material.Surface
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.Modifier
import com.neu.mobileapplicationdevelopment202430.navigation.AppNavHost

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    MyTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            AppNavHost(navController)
        }
    }
}