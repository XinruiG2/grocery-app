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

fun RecipeEntity.toRecipe(): RecipeItem {
    return RecipeItem(this.name, this.description, this.ingredients, this.imageUrl)
}

fun RecipeItem.toEntity(): RecipeEntity {
    return RecipeEntity(
        name = this.name,
        description = this.description,
        ingredients = this.ingredients,
        imageUrl = this.imageUrl
    )
}