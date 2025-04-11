package com.neu.mobileapplicationdevelopment202430.model

import android.util.Log
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

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

fun FridgeEntity.toFridgeItem(): FridgeItem {
    return FridgeItem(this.name, this.dateBought, this.quantity, this.imageUrl)
}

fun FridgeItem.toEntity(): FridgeEntity {
    return FridgeEntity(
        name = this.name,
        dateBought = this.dateBought,
        quantity = this.quantity,
        imageUrl = this.imageUrl
    )
}

fun GroceryListEntity.toGroceryItem(): GroceryListItem {
    return GroceryListItem(this.name, this.quantity)
}

fun GroceryListItem.toEntity(): GroceryListEntity {
    return GroceryListEntity(
        name = this.name,
        quantity = this.quantity
    )
}

fun ReminderEntity.toReminderItem(): ReminderItem {
    return ReminderItem(this.name, this.imageUrl, this.numDaysTilExpired)
}

fun ReminderItem.toEntity(): ReminderEntity {
    return ReminderEntity(
        name = this.name,
        imageUrl = this.imageUrl,
        numDaysTilExpired = this.numDaysTilExpired
    )
}

fun FridgeItem.toReminderItem(numDaysGoodFor: Int): ReminderItem {
    Log.d("In mapping", "In Mapping")
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    Log.d("In mapping", dateFormat.toString())
    val boughtDate: Date = dateFormat.parse(this.dateBought)!!
    Log.d("In mapping", boughtDate.toString())

    val currentDate = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.time

    Log.d("In mapping", currentDate.toString())

    val diffInMillis = currentDate.time - boughtDate.time
    val daysSinceBought = TimeUnit.MILLISECONDS.toDays(diffInMillis).toInt()
    Log.d("In mapping", daysSinceBought.toString())
    val daysLeft = numDaysGoodFor - daysSinceBought
    Log.d("In mapping", daysLeft.toString())

    return ReminderItem(
        name = this.name,
        imageUrl = this.imageUrl,
        numDaysTilExpired = daysLeft
    )
}