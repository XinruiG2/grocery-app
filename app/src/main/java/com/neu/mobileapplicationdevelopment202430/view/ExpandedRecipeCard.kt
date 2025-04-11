package com.neu.mobileapplicationdevelopment202430.view

import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.neu.mobileapplicationdevelopment202430.model.FridgeItem
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.neu.mobileapplicationdevelopment202430.model.RecipeItem

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ExpandedRecipeCard(recipe: RecipeItem, onBack: () -> Unit) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(20.dp)
            .testTag("expandedRecipeCard")
            .padding(bottom = 15.dp)
            .border(width = 1.dp, color = Color.Gray, shape = RectangleShape)
    ) {
        TopAppBar(
            title = {},
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
            },
            backgroundColor = Color.Black,
            contentColor = Color.White,
            elevation = 4.dp,
            modifier = Modifier.testTag("topBar")
        )

        GlideImage(
            model = recipe.imageUrl,
            contentDescription = recipe.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth().height(300.dp).testTag("image"),
        )

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 15.dp)) {
            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = recipe.name,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.testTag("name")
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Ingredients:",
                fontSize = 26.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.testTag("ingredientsHeader")
            )
            val ingredientList = recipe.ingredients.split(",").map { it.trim().capitalize() }

            Spacer(modifier = Modifier.height(5.dp))

            ingredientList.forEach { ingredient ->
                Text("• $ingredient", fontSize = 18.sp, modifier = Modifier.testTag("ingredient"))
            }

            Spacer(modifier = Modifier.height(15.dp))
        }
    }
}
