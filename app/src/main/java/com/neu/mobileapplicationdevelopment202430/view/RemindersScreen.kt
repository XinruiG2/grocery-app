package com.neu.mobileapplicationdevelopment202430.view
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.neu.mobileapplicationdevelopment202430.model.FoodDatabase
import com.neu.mobileapplicationdevelopment202430.model.FoodRepository
import com.neu.mobileapplicationdevelopment202430.model.FridgeVMCreator
import com.neu.mobileapplicationdevelopment202430.model.GroceryVMCreator
import com.neu.mobileapplicationdevelopment202430.model.IngredientItem
import com.neu.mobileapplicationdevelopment202430.model.ReminderItem
import com.neu.mobileapplicationdevelopment202430.model.ReminderVMCreator
import com.neu.mobileapplicationdevelopment202430.model.UserInformation
import com.neu.mobileapplicationdevelopment202430.viewmodel.FridgeVM
import com.neu.mobileapplicationdevelopment202430.viewmodel.GroceryVM
import com.neu.mobileapplicationdevelopment202430.viewmodel.RemindersVM

@Composable
fun RemindersScreen(navController: NavHostController) {
    val context = LocalContext.current
    val userPreferences = UserInformation(context)
    val userId = userPreferences.getUserId()
    val foodRepository = FoodRepository(FoodDatabase.getDatabase(context).foodDao(), context)
    val remindersVM: RemindersVM = viewModel(factory = ReminderVMCreator(foodRepository, userId))
    val reminders by remindersVM.reminders.observeAsState(emptyList())
    val isLoading by remindersVM.isLoading.observeAsState()
    val errorMessage by remindersVM.errorMessage.observeAsState()

    LaunchedEffect(Unit) {
        remindersVM.loadReminderItems()
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(bottom = 56.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Reminders",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp)
                    .drawBehind {
                        drawLine(
                            color = Color.Black,
                            start = Offset(0f, size.height + 14.dp.toPx()),
                            end = Offset(size.width, size.height + 14.dp.toPx()),
                            strokeWidth = 2.dp.toPx()
                        )
                    },
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (isLoading == true) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            } else if (errorMessage != null) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = errorMessage!!,
                        color = Color.Black,
                        fontSize = 22.sp,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(reminders ?: emptyList()) { reminder ->
                        ReminderItemCard(item = reminder)
                    }
                }
            }
        }
        FooterNavigation(navController, modifier = Modifier.align(Alignment.BottomCenter))
    }
}
