package com.neu.mobileapplicationdevelopment202430.model

fun IngredientEntity.toIngredient(): IngredientItem {
    return IngredientItem(this.name, this.numDaysGoodFor, this.numRecipesUsedIn, this.imageUrl)
}

fun IngredientItem.toEntity(): IngredientEntity {
    return IngredientEntity(
        name = this.name,
        numDaysGoodFor = this.numDaysGoodFor,
        numRecipesUsedIn = this.numRecipesUsedIn,
        imageUrl = this.imageUrl
    )
}