package com.neu.mobileapplicationdevelopment202430.view
import android.util.Log
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
import androidx.navigation.NavHostController
import com.neu.mobileapplicationdevelopment202430.model.IngredientItem

@Composable
fun IngredientsScreen(navController: NavHostController) {
    var ingredients by remember {
        mutableStateOf(
            listOf(
                IngredientItem("Apple", 4, 0, "https://i.pinimg.com/736x/4f/6c/c4/4f6cc46e50e7a0ff21c5e0a77423b0b5.jpg"),
                IngredientItem("Egg", 7, 1, "https://i.pinimg.com/736x/4f/6c/c4/4f6cc46e50e7a0ff21c5e0a77423b0b5.jpg"),
                IngredientItem("Pasta", 30, 2, "https://i.pinimg.com/736x/4f/6c/c4/4f6cc46e50e7a0ff21c5e0a77423b0b5.jpg"),
                IngredientItem("Strawberries", 3, 2, "https://i.pinimg.com/736x/4f/6c/c4/4f6cc46e50e7a0ff21c5e0a77423b0b5.jpg"),
                IngredientItem("Milk", 7, 3, "https://i.pinimg.com/736x/4f/6c/c4/4f6cc46e50e7a0ff21c5e0a77423b0b5.jpg")
            )
        )
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

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(ingredients) { ingredient ->
                    IngredientItemCard(item = ingredient)
                }
            }
        }
        FooterNavigation(navController, modifier = Modifier.align(Alignment.BottomCenter))
    }
}
