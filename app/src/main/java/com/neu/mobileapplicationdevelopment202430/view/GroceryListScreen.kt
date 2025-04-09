package com.neu.mobileapplicationdevelopment202430.view

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.neu.mobileapplicationdevelopment202430.model.FoodDatabase
import com.neu.mobileapplicationdevelopment202430.model.FoodRepository
import com.neu.mobileapplicationdevelopment202430.model.FridgeVMCreator
import com.neu.mobileapplicationdevelopment202430.model.GroceryListItem
import com.neu.mobileapplicationdevelopment202430.model.GroceryVMCreator
import com.neu.mobileapplicationdevelopment202430.model.IngredientsVMCreator
import com.neu.mobileapplicationdevelopment202430.model.UserInformation
import com.neu.mobileapplicationdevelopment202430.viewmodel.FridgeVM
import com.neu.mobileapplicationdevelopment202430.viewmodel.GroceryVM
import com.neu.mobileapplicationdevelopment202430.viewmodel.IngredientsVM
import kotlinx.coroutines.launch

@Composable
fun GroceryListScreen(navController: NavHostController) {
//    var groceryItems by remember {
//        mutableStateOf(
//            listOf(
//                GroceryListItem("Carrots", 4),
//                GroceryListItem("Eggs", 12)
//            )
//        )
//    }

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val userPreferences = UserInformation(context)
    val userId = userPreferences.getUserId()
    val foodRepository = FoodRepository(FoodDatabase.getDatabase(context).foodDao(), context)
    val groceryVM: GroceryVM = viewModel(factory = GroceryVMCreator(foodRepository, userId))
    val groceryItems by groceryVM.groceryItems.observeAsState(emptyList())
    val isLoading by groceryVM.isLoading.observeAsState()
    val errorMessage by groceryVM.errorMessage.observeAsState()

    val ingredientsVM: IngredientsVM = viewModel(factory = IngredientsVMCreator(foodRepository))
    val fridgeVM: FridgeVM = viewModel(factory = FridgeVMCreator(foodRepository, userId, groceryVM))

    var checkedItems by remember { mutableStateOf(setOf<GroceryListItem>()) }
    var showAddItemPopup by remember { mutableStateOf(false) }
    var selectedItemName by remember { mutableStateOf("Carrots") }
    var selectedItemQuantity by remember { mutableStateOf(1) }

    // maybe replace later, get db's ingredients
    val availableIngredients by ingredientsVM.ingredients.observeAsState()
    val itemOptions = availableIngredients?.map { it.name } ?: emptyList()
    val quantityOptions = (1..50).toList() //m should this be constant or an api call??

    LaunchedEffect(Unit) {
        groceryVM.loadGroceryItems()
        ingredientsVM.loadIngredients()
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Column {
                    Text(
                        text = "Grocery List",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 15.dp)
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

                    if (isLoading == true) {
                        Box(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                        }
                    } else if (errorMessage != null) {
                        Box(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                text = errorMessage!!,
                                color = Color.Black,
                                fontSize = 22.sp,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    } else {
                        LazyColumn() {
                            items(groceryItems ?: emptyList()) { item ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Checkbox(
                                        checked = checkedItems.contains(item),
                                        onCheckedChange = { isChecked ->
                                            checkedItems = if (isChecked) {
                                                checkedItems + item
                                            } else {
                                                checkedItems - item
                                            }
                                        }
                                    )
                                    Text(
                                        text = "${item.name}, ${item.quantity}",
                                        fontSize = 18.sp,
                                        modifier = Modifier.weight(1f)
                                    )
                                    IconButton(
                                        onClick = { groceryVM.deleteGroceryItem(userId, item) }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "remove item from list",
                                            tint = Color.Black
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = { showAddItemPopup = true }
                    ) {
                        Text(text = "Add to Grocery List")
                    }
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                fridgeVM.addToOrUpdateGroceryList(
                                    checkedItems.toList(),
                                    availableIngredients ?: listOf()
                                )
                            }
                        }
                    ) {
                        Text(text = "Add to Fridge")
                    }
                }
            }
        }

        FooterNavigation(navController, modifier = Modifier.align(Alignment.BottomCenter))
    }

    if (showAddItemPopup) {
        AlertDialog(
            onDismissRequest = { showAddItemPopup = false },
            title = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "",
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            },
            text = {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Item:", fontSize = 16.sp, modifier = Modifier.padding(end = 12.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        DropDown(
                            options = itemOptions,
                            selectedOption = selectedItemName,
                            onOptionSelected = { selectedItemName = it }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Quantity:",
                            fontSize = 16.sp,
                            modifier = Modifier.padding(end = 12.dp)
                        )

                        IconButton(
                            onClick = { if (selectedItemQuantity > 1) selectedItemQuantity-- },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = "Decrease")
                        }

                        Text(
                            text = "$selectedItemQuantity",
                            fontSize = 18.sp,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )

                        IconButton(
                            onClick = { selectedItemQuantity++ },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Increase")
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    modifier = Modifier.padding(end = 15.dp, bottom = 9.dp),
                    onClick = {
                        groceryVM.addGroceryItem(userId, selectedItemName, selectedItemQuantity)
                        showAddItemPopup = false
                    }
                ) {
                    Text("Add to List")
                }
            },
            dismissButton = {
                Button(
                    modifier = Modifier.padding(bottom = 9.dp),
                    onClick = { showAddItemPopup = false }) {
                    Text("Cancel")
                }
            }
        )
    }

}
