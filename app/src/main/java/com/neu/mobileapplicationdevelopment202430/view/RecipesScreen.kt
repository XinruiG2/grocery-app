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
import androidx.compose.ui.platform.testTag
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
    val context = LocalContext.current
    val foodRepository = FoodRepository(FoodDatabase.getDatabase(context).foodDao(), context)
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

            Spacer(modifier = Modifier.height(3.dp))

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
                            .testTag("searchBar")
                            .padding(vertical = 5.dp, horizontal = 8.dp)
                    )
                    if (filteredRecipes.isNullOrEmpty()) {
                        Text(
                            text = "No recipes available",
                            modifier = Modifier.testTag("emptyRecipes"),
                            color = Color.Black,
                            fontSize = 22.sp
                        )
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize().testTag("recipesList")) {
                            items(filteredRecipes) { recipe ->
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
            }
        }

        FooterNavigation(navController, modifier = Modifier.align(Alignment.BottomCenter).testTag("footerNav"))
    }
}
