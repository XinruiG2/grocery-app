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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.neu.mobileapplicationdevelopment202430.model.FoodDatabase
import com.neu.mobileapplicationdevelopment202430.model.FoodRepository
import com.neu.mobileapplicationdevelopment202430.model.FridgeRepository
import com.neu.mobileapplicationdevelopment202430.model.FridgeVMCreator
import com.neu.mobileapplicationdevelopment202430.model.IngredientItem
import com.neu.mobileapplicationdevelopment202430.model.IngredientsVMCreator
import com.neu.mobileapplicationdevelopment202430.viewmodel.FridgeVM
import com.neu.mobileapplicationdevelopment202430.viewmodel.IngredientsVM

@Composable
fun IngredientsScreen(navController: NavHostController) {

    val context = LocalContext.current
    val foodRepository = FoodRepository(FoodDatabase.getDatabase(context).foodDao(), context)
    val ingredientsVM: IngredientsVM = viewModel(factory = IngredientsVMCreator(foodRepository))
    val ingredients by ingredientsVM.ingredients.observeAsState(emptyList())
    val isLoading by ingredientsVM.isLoading.observeAsState()
    val errorMessage by ingredientsVM.errorMessage.observeAsState()

    LaunchedEffect(Unit) {
        ingredientsVM.loadIngredients()
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
                text = "Ingredients",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp)
                    .testTag("title")
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
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center).testTag("loading"))
                }
            } else if (errorMessage != null) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = errorMessage!!,
                        color = Color.Black,
                        fontSize = 22.sp,
                        modifier = Modifier.align(Alignment.Center).testTag("error")
                    )
                }
            } else {
                if (ingredients.isNullOrEmpty()) {
                    Text(
                        text = "No ingredients available",
                        modifier = Modifier.testTag("emptyIngredients"),
                        color = Color.Black,
                        fontSize = 22.sp
                    )
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize().testTag("ingredients")) {
                        items(ingredients ?: emptyList()) { ingredient ->
                            IngredientItemCard(item = ingredient)
                        }
                    }
                }
            }
        }
        FooterNavigation(navController, modifier = Modifier.align(Alignment.BottomCenter).testTag("footerNav"))
    }
}
