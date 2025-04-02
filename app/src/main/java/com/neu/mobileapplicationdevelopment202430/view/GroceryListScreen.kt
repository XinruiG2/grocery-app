package com.neu.mobileapplicationdevelopment202430.view

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.geometry.Offset
import androidx.navigation.NavHostController
import com.neu.mobileapplicationdevelopment202430.model.GroceryListItem

@Composable
fun GroceryListScreen(navController: NavHostController) {
    var groceryItems by remember {
        mutableStateOf(
            listOf(
                GroceryListItem("Carrots", 4),
                GroceryListItem("Eggs", 12)
            )
        )
    }
    var checkedItems by remember { mutableStateOf(setOf<GroceryListItem>()) }
    var showAddItemPopup by remember { mutableStateOf(false) }
    var selectedItemName by remember { mutableStateOf("Carrots") }
    var selectedItemQuantity by remember { mutableStateOf(1) }

    // maybe replace later, get db's ingredients
    val itemOptions = listOf("Carrots", "Eggs", "Tomatoes", "Strawberries", "Milk")
    val quantityOptions = (1..50).toList() //m should this be constant or an api call??

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
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

                    groceryItems.forEach { item ->
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
                                onClick = { groceryItems = groceryItems - item }
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
                        Text(text = "add item to list")
                    }
                    Button(
                        onClick = {}
                    ) {
                        Text(text = "add items to fridge")
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
                    Row (modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp),
                        verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "Select an item:")
                        Spacer(modifier = Modifier.width(3.dp))
                        DropDown(
                            options = itemOptions,
                            selectedOption = selectedItemName,
                            onOptionSelected = { selectedItemName = it }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row (modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp),
                        verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "Select quantity:")
                        Spacer(modifier = Modifier.width(3.dp))
                        DropDown(
                            options = quantityOptions.map { it.toString() },
                            selectedOption = selectedItemQuantity.toString(),
                            onOptionSelected = { selectedItemQuantity = it.toInt() }
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    modifier = Modifier.padding(end = 15.dp, bottom = 9.dp),
                    onClick = {
                        groceryItems = groceryItems + GroceryListItem(selectedItemName, selectedItemQuantity)
                        showAddItemPopup = false
                    }
                ) {
                    Text("Add to List")
                }
            },
            dismissButton = {
                Button(modifier = Modifier.padding(bottom = 9.dp), onClick = { showAddItemPopup = false }) {
                    Text("Cancel")
                }
            }
        )
    }

}
