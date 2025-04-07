package com.neu.mobileapplicationdevelopment202430.model

fun IngredientEntity.toIngredient(): IngredientItem {
    return IngredientItem(name, numDaysGoodFor, numRecipesUsedIn, imageUrl)
}