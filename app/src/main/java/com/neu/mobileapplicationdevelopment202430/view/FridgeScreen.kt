package com.neu.mobileapplicationdevelopment202430.view
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.util.Log
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.neu.mobileapplicationdevelopment202430.model.FoodDatabase
import com.neu.mobileapplicationdevelopment202430.model.FoodRepository
import com.neu.mobileapplicationdevelopment202430.model.FridgeItem
import com.neu.mobileapplicationdevelopment202430.model.FridgeVMCreator
import com.neu.mobileapplicationdevelopment202430.model.GroceryVMCreator
import com.neu.mobileapplicationdevelopment202430.model.RecipesVMCreator
import com.neu.mobileapplicationdevelopment202430.model.UserInformation
import com.neu.mobileapplicationdevelopment202430.viewmodel.FridgeVM
import com.neu.mobileapplicationdevelopment202430.viewmodel.GroceryVM
import com.neu.mobileapplicationdevelopment202430.viewmodel.RecipeVM

@Composable
fun FridgeScreen(navController: NavHostController) {
//    var items by remember {
//        mutableStateOf(
//            listOf(
//                FridgeItem("Apple", "2025-04-01", 3, "https://i.pinimg.com/736x/4f/6c/c4/4f6cc46e50e7a0ff21c5e0a77423b0b5.jpg"),
//                FridgeItem("Milk", "2025-03-25", 1, "https://i.pinimg.com/736x/4f/6c/c4/4f6cc46e50e7a0ff21c5e0a77423b0b5.jpg"),
//                FridgeItem("Carrot", "2025-03-30", 5, "https://i.pinimg.com/736x/4f/6c/c4/4f6cc46e50e7a0ff21c5e0a77423b0b5.jpg"),
//                FridgeItem("Strawberries", "2025-03-30", 5, "https://i.pinimg.com/736x/4f/6c/c4/4f6cc46e50e7a0ff21c5e0a77423b0b5.jpg"),
//                FridgeItem("Chocolate", "2026-05-30", 3, "https://i.pinimg.com/736x/4f/6c/c4/4f6cc46e50e7a0ff21c5e0a77423b0b5.jpg")
//            )
//        )
//    }

    val context = LocalContext.current
    val userPreferences = UserInformation(context)
    val userId = userPreferences.getUserId()
    val foodRepository = FoodRepository(FoodDatabase.getDatabase(context).foodDao(), context)
    val groceryVM: GroceryVM = viewModel(factory = GroceryVMCreator(foodRepository, userId))
    val fridgeVM: FridgeVM = viewModel(factory = FridgeVMCreator(foodRepository, userId, groceryVM))
    val fridgeItems by fridgeVM.fridgeItems.observeAsState(emptyList())
    val isLoading by fridgeVM.isLoading.observeAsState()
    val errorMessage by fridgeVM.errorMessage.observeAsState()

    LaunchedEffect(Unit) {
        fridgeVM.loadFridgeItems()
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
                text = "What's in my Fridge",
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

//            Text(
//                text = "user id test: ${userId}",
//                fontSize = 24.sp,
//                fontWeight = FontWeight.Bold
//            )

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(fridgeItems ?: emptyList()) { item ->
                    FridgeItemCard(
                        item = item,
                        updateQuantity = { newQuantity ->
                            fridgeVM.updateItemQuantity(userId, item.name, newQuantity)
//                            if (newQuantity == 0) {
//                                items = items.filterNot { it.name == item.name }
//                            }
                        }
                    )
                }
            }

        }

        FooterNavigation(navController, modifier = Modifier.align(Alignment.BottomCenter))
    }
}
