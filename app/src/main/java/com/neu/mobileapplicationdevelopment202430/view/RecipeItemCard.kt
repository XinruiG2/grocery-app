package com.neu.mobileapplicationdevelopment202430.view

import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.neu.mobileapplicationdevelopment202430.model.FridgeItem
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.neu.mobileapplicationdevelopment202430.model.RecipeItem


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun RecipeItemCard(item: RecipeItem, onReadMore: (RecipeItem) -> Unit) {
    val maxHeight = 150.dp

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = maxHeight)
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
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = item.description,
                        fontSize = 16.sp,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    TextButton(
                        onClick = { onReadMore(item) },
                        modifier = Modifier.padding(0.dp)
                    ) {
                        Text("Read more...", color = Color.Gray)
                    }

        } }
    }
}