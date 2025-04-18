package com.neu.mobileapplicationdevelopment202430.view

import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.neu.mobileapplicationdevelopment202430.model.FridgeItem
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.neu.mobileapplicationdevelopment202430.model.IngredientItem


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun IngredientItemCard(item: IngredientItem) {
    val maxHeight = 150.dp

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = maxHeight)
            .testTag("ingredientCard")
            .padding(vertical = 8.dp, horizontal = 8.dp),
        elevation = 4.dp
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            GlideImage(
                model = item.imageUrl,
                contentDescription = item.name,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(0.dp)
                    .testTag("image")
            )

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(2f)
                    .padding(10.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = item.name,
                    fontSize = 16.sp,
                    modifier = Modifier.testTag("name")
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = "Good for: ${item.numDaysGoodFor} days",
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.testTag("freshness")
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = "Used in: ${item.numRecipesUsedIn} recipes",
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.testTag("numRecipes")
                )
            }
        }
    }
}