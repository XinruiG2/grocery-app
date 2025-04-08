package com.neu.mobileapplicationdevelopment202430.view
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
import com.neu.mobileapplicationdevelopment202430.model.RecipeItem
import com.neu.mobileapplicationdevelopment202430.model.RecipesVMCreator
import com.neu.mobileapplicationdevelopment202430.viewmodel.IngredientsVM
import com.neu.mobileapplicationdevelopment202430.viewmodel.RecipeVM

@Composable
fun RecipesScreen(navController: NavHostController) {
    var searchBarText by remember { mutableStateOf("") }
    var expandRecipe by remember { mutableStateOf<RecipeItem?>(null) }
//    val recipes = listOf(
//        RecipeItem("Pasta", "Pasta with tomato sauce.", "pasta, tomato, garlic, basil", "https://i.pinimg.com/736x/4f/6c/c4/4f6cc46e50e7a0ff21c5e0a77423b0b5.jpg"),
//        RecipeItem("Omelette", "Veggie omelette with cheese.", "egg, cheese, oil, spinach", "https://i.pinimg.com/736x/4f/6c/c4/4f6cc46e50e7a0ff21c5e0a77423b0b5.jpg"),
//        RecipeItem("Smoothie", "Berry smoothie.", "strawberries, blueberries, banana, milk", "https://i.pinimg.com/736x/4f/6c/c4/4f6cc46e50e7a0ff21c5e0a77423b0b5.jpg")
//    )

    val context = LocalContext.current
    val foodRepository = FoodRepository(FoodDatabase.getDatabase(context).foodDao())
    val recipesVM: RecipeVM = viewModel(factory = RecipesVMCreator(foodRepository))
    val recipes by recipesVM.recipes.observeAsState(emptyList())
    val isLoading by recipesVM.isLoading.observeAsState()
    val errorMessage by recipesVM.errorMessage.observeAsState()

    LaunchedEffect(Unit) {
        recipesVM.loadRecipes()
    }

    val filteredRecipes = remember(searchBarText, recipes) {
        if (searchBarText.isBlank()) {
            recipes
        } else {
            val searchIngredients = searchBarText.lowercase()
                .split(",")
                .map { it.trim() }
                .filter { it.isNotEmpty() }

            recipes?.filter { recipe ->
                searchIngredients.any { ing ->
                    recipe.ingredients.lowercase().contains(ing)
                }
            }
        }
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
                text = "Recipes",
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

            Spacer(modifier = Modifier.height(3.dp))

            expandRecipe?.let { recipe ->
                ExpandedRecipeCard(recipe) {
                    expandRecipe = null
                }
            } ?: run {
                OutlinedTextField(
                    value = searchBarText,
                    onValueChange = { searchBarText = it },
                    label = { Text("Ingredients separated by commas") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp, horizontal = 8.dp)
                )
                LazyColumn {
                    items(filteredRecipes ?: emptyList()) { recipe ->
                        RecipeItemCard(
                            item = recipe,
                            onReadMore = {
                                expandRecipe = it
                            }
                        )
                    }
                }
            }
        }

        FooterNavigation(navController, modifier = Modifier.align(Alignment.BottomCenter))
    }
}
