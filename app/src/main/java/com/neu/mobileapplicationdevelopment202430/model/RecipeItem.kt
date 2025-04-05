package com.neu.mobileapplicationdevelopment202430.model

data class RecipeItem(
    val name: String,
    val description: String,
    var ingredients: String,
    var imageUrl: String
)