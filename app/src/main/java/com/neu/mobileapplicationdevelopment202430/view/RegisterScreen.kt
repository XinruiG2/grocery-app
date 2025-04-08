package com.neu.mobileapplicationdevelopment202430.view
import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.neu.mobileapplicationdevelopment202430.R
import com.neu.mobileapplicationdevelopment202430.model.FoodDatabase
import com.neu.mobileapplicationdevelopment202430.model.FoodRepository
import com.neu.mobileapplicationdevelopment202430.model.LoginVMCreator
import com.neu.mobileapplicationdevelopment202430.model.RegisterVMCreator
import com.neu.mobileapplicationdevelopment202430.navigation.NavigationItem
import com.neu.mobileapplicationdevelopment202430.viewmodel.LoginVM
import com.neu.mobileapplicationdevelopment202430.viewmodel.RegisterVM

@Composable
fun RegisterScreen(navController: NavHostController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    val foodRepository = FoodRepository(FoodDatabase.getDatabase(context).foodDao())
    val registerVM: RegisterVM = viewModel(factory = RegisterVMCreator(foodRepository))
    val signupStatus by registerVM.signupStatus.observeAsState()
    val isLoading by registerVM.isLoading.observeAsState()
    val errorMessage by registerVM.errorMessage.observeAsState()

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
                text = "Sign Up",
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
                registerVM.validSignUpOrNot(username, password)
            }) {
                Text("sign up")
            }

            Text(
                text = "login",
                color = colorResource(id = R.color.purple_700),
                fontSize = 16.sp,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 6.dp)
                    .clickable {
                        navController.navigate(NavigationItem.Login.route)
                    }
            )

            LaunchedEffect(signupStatus) {
                signupStatus?.let { isValid ->
                    if (isValid) {
                        navController.navigate(NavigationItem.Reminders.route)
                    } else {
                        Toast.makeText(context, "User already exists", Toast.LENGTH_SHORT).show()
                    }
                    registerVM.resetRegisterStatus()
                }
            }
        }
    }
}
