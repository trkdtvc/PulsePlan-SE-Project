package com.example.dietplanner.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.dietplanner.model.Food
import com.example.dietplanner.ui.viewmodel.FoodViewModel
import com.example.dietplanner.ui.viewmodel.MealLogViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMealScreen(
    mealLogViewModel: MealLogViewModel = hiltViewModel(),
    foodViewModel: FoodViewModel = hiltViewModel(),
    navController: NavController
) {
    val foods by foodViewModel.foods.collectAsState()
    val selectedFood = remember { mutableStateOf<Food?>(null) }
    var gramsInput by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val currentUserId = 1 // TODO: zamijeniti sa SessionManager kasnije

    LaunchedEffect(Unit) {
        foodViewModel.loadAllFoods()
    }

    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFB9F6CA),
            Color(0xFF69F0AE),
            Color(0xFF00E676)
        )
    )

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = Color.Transparent
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundGradient)
                .padding(24.dp)
                .padding(innerPadding)
        ) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Restaurant,
                        contentDescription = null,
                        tint = Color(0xFF00796B),
                        modifier = Modifier.size(50.dp)
                    )

                    Text(
                        text = "Add a Meal",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.Black
                    )

                    Text(
                        text = "Track your food and calories by adding a meal entry below.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.DarkGray,
                        textAlign = TextAlign.Center
                    )

                    var expanded by remember { mutableStateOf(false) }

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = selectedFood.value?.food_name ?: "Select Food",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Food") },
                            trailingIcon = {
                                Icon(
                                    imageVector = if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                                    contentDescription = null
                                )
                            },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            foods.forEach { food ->
                                DropdownMenuItem(
                                    text = { Text(food.food_name) },
                                    onClick = {
                                        selectedFood.value = food
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = gramsInput,
                        onValueChange = { gramsInput = it },
                        label = { Text("Quantity (grams)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    val previewCalories = selectedFood.value?.calories_per_100g?.let {
                        gramsInput.toDoubleOrNull()?.let { grams ->
                            (it * grams / 100).toInt()
                        }
                    }
                    if (previewCalories != null) {
                        Text(
                            text = "Estimated Calories: $previewCalories kcal",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF00796B)
                        )
                    }

                    errorMessage?.let {
                        Text(
                            text = it,
                            color = Color.Red,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Button(
                        onClick = {
                            val selected = selectedFood.value
                            val grams = gramsInput.toDoubleOrNull()
                            if (selected == null || grams == null || grams <= 0) {
                                errorMessage = "Please select a food and enter a valid quantity"
                                return@Button
                            }

                            mealLogViewModel.addMealLog(
                                foodId = selected.food_id,
                                grams = grams
                            )

                            scope.launch {
                                snackbarHostState.showSnackbar("Meal successfully added!")
                            }

                            navController.popBackStack()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007f9c))
                    ) {
                        Text("Add Meal", color = Color.White)
                    }
                }
            }
        }
    }
}
