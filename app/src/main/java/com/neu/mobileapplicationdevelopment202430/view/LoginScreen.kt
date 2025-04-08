package com.neu.mobileapplicationdevelopment202430.view

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import com.neu.mobileapplicationdevelopment202430.viewmodel.LoginVM
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import com.neu.mobileapplicationdevelopment202430.R
import com.neu.mobileapplicationdevelopment202430.model.FoodDatabase
import com.neu.mobileapplicationdevelopment202430.model.FoodRepository
import com.neu.mobileapplicationdevelopment202430.model.LoginVMCreator
import com.neu.mobileapplicationdevelopment202430.model.RecipesVMCreator
import com.neu.mobileapplicationdevelopment202430.navigation.NavigationItem
import com.neu.mobileapplicationdevelopment202430.viewmodel.RecipeVM

@Composable
fun LoginScreen(navController: NavHostController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    val foodRepository = FoodRepository(FoodDatabase.getDatabase(context).foodDao(), context)
    val loginVM: LoginVM = viewModel(factory = LoginVMCreator(foodRepository))
    val loginStatus by loginVM.loginStatus.observeAsState()
    val isLoading by loginVM.isLoading.observeAsState()
    val errorMessage by loginVM.errorMessage.observeAsState()

    BackHandler {
        (context as? Activity)?.finish()
    }

    if (isLoading == true) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "My Grocery App",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("username") },
                modifier = Modifier.fillMaxWidth(0.93f),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFF800080),
                    unfocusedBorderColor = Color.Gray
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(0.93f),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFF800080),
                    unfocusedBorderColor = Color.Gray
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                loginVM.validUserOrNot(username, password)
            }) {
                Text("login")
            }

            Text(
                text = "sign up",
                color = colorResource(id = R.color.purple_700),
                fontSize = 16.sp,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 6.dp)
                    .clickable {
                        navController.navigate(NavigationItem.Register.route)
                    }
            )

            LaunchedEffect(loginStatus) {
                loginStatus?.let { isValid ->
                    if (isValid) {
                        navController.navigate(NavigationItem.Reminders.route)
                    } else {
                        Toast.makeText(context, "Invalid credentials", Toast.LENGTH_SHORT).show()
                    }
                    loginVM.resetLoginStatus()
                }
            }
        }
    }
}