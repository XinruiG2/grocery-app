package com.neu.mobileapplicationdevelopment202430.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neu.mobileapplicationdevelopment202430.model.FoodRepository
import com.neu.mobileapplicationdevelopment202430.model.FridgeEntity
import com.neu.mobileapplicationdevelopment202430.model.FridgeItem
import com.neu.mobileapplicationdevelopment202430.model.GroceryListItem
import com.neu.mobileapplicationdevelopment202430.model.IngredientItem
import com.neu.mobileapplicationdevelopment202430.model.RecipeItem
import com.neu.mobileapplicationdevelopment202430.model.ReminderEntity
import com.neu.mobileapplicationdevelopment202430.model.ReminderItem
import com.neu.mobileapplicationdevelopment202430.model.toFridgeItem
import com.neu.mobileapplicationdevelopment202430.model.toReminderItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RemindersVM(private val repository: FoodRepository, private val userId : Int) : ViewModel() {
    private val _reminders = MutableLiveData<List<ReminderItem>?>(emptyList())
    val reminders: LiveData<List<ReminderItem>?> get() = _reminders
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun loadReminderItems() {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val apiUserInformation = repository.getUserInformationFromApi(userId)
                if (apiUserInformation != null) {
                    val ingredients = repository.getIngredientsFromApi().distinctBy { it.name }
                    Log.d("RemindersVM", ingredients.toString())
                    val fridgeItems = apiUserInformation.fridge_items.map { it.toFridgeItem() }
                    val ingredientNames = ingredients.associateBy { it.name }
                    Log.d("RemindersVM", ingredientNames.toString())
                    Log.d("RemindersVM", fridgeItems.toString())

                    val reminderItems = fridgeItems.map { fridgeItem ->
                        val ingredient = ingredientNames.get(fridgeItem.name)
                        val daysGoodFor = ingredient?.numDaysGoodFor ?: 0
                        Log.d("Reminders VM", "DaysGoodForPassedin: ${daysGoodFor}")

                        fridgeItem.toReminderItem(daysGoodFor)
                    }

                    Log.d("RemindersVM", reminderItems.toString())

                    val filteredReminderItems = reminderItems.filter { it.numDaysTilExpired > 0 }
                    Log.d("RemindersVM", filteredReminderItems.toString())

                    repository.saveRemindersToDatabase(filteredReminderItems)
                    withContext(Dispatchers.Main) {
                        _reminders.value = filteredReminderItems
                    }
                }
            } catch (e: Exception) {
                Log.e("ReminderVM", "FOUND AN ERROR!: ${e.message}")

                val storedReminders = repository.getRemindersFromDatabase()
                withContext(Dispatchers.Main) {
                    if (!storedReminders.isNullOrEmpty()) {
                        _reminders.value = storedReminders
                    } else {
                        _errorMessage.value = "Reminders unavailable right now"
                    }
                }
            } finally {
                withContext(Dispatchers.Main) {
                    _isLoading.value = false
                }
            }
        }
    }
}