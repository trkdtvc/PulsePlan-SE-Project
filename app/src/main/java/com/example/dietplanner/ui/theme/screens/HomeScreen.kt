package com.example.dietplanner.ui.theme.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.dietplanner.R
import com.example.dietplanner.ui.theme.screens.viewmodel.MealViewModel
import com.example.dietplanner.ui.viewmodel.MealLogViewModel
import com.example.dietplanner.ui.viewmodel.UserViewModel
import com.example.dietplanner.ui.viewmodel.WaterIntakeViewModel

@Composable
fun HomeScreen(
    userViewModel: UserViewModel = hiltViewModel(),
    viewModel: MealLogViewModel = hiltViewModel(),
    mealVm: MealViewModel = hiltViewModel(),
    waterViewModel: WaterIntakeViewModel = hiltViewModel(),
    navController: NavController
) {
    val meals by viewModel.meals.collectAsState()
    val error by viewModel.error.collectAsState()
    val user by userViewModel.user.collectAsState()
    val waterState by waterViewModel.waterState.collectAsState()

    val dailyCalorieGoal = user?.let {
        calculateDailyCalories(
            weightKg = it.weight_kg,
            heightCm = it.height_cm,
            age = it.age,
            gender = it.gender,
            activityLevel = it.activity_level
        )
    } ?: 2200f

    val dailyProteinGoal = (dailyCalorieGoal * 0.25) / 4
    val dailyFatGoal = (dailyCalorieGoal * 0.30) / 9
    val dailyCarbsGoal = (dailyCalorieGoal * 0.45) / 4

    val totalCalories = meals.sumOf {
        (it.calories_per_100g * it.quantity_g / 100).toInt()
    }.toFloat()

    val totalProtein = meals.sumOf {
        (it.protein_g * it.quantity_g / 100).toDouble()
    }

    val totalFat = meals.sumOf {
        (it.fats_g * it.quantity_g / 100).toDouble()
    }

    val totalCarbs = meals.sumOf {
        (it.carbs_g * it.quantity_g / 100).toDouble()
    }

    val proteinLeft = (dailyProteinGoal - totalProtein).coerceAtLeast(0.0)
    val fatLeft = (dailyFatGoal - totalFat).coerceAtLeast(0.0)
    val carbsLeft = (dailyCarbsGoal - totalCarbs).coerceAtLeast(0.0)

    val proteinProgress = (totalProtein / dailyProteinGoal).toFloat().coerceIn(0f, 1f)
    val fatProgress = (totalFat / dailyFatGoal).toFloat().coerceIn(0f, 1f)
    val carbsProgress = (totalCarbs / dailyCarbsGoal).toFloat().coerceIn(0f, 1f)

    val burnedKcal by mealVm.burnedCalories.collectAsState()

    val lightGreenGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFB9F6CA), Color(0xFF69F0AE), Color(0xFF00E676))
    )

    val boxGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFE8F5E9), Color(0xFFB9F6CA).copy(alpha = 0.8f))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = lightGreenGradient)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .width(94.dp)
                .padding(bottom = 16.dp, top = 20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Home",
                fontSize = 40.sp,
                fontWeight = FontWeight.W200,
                color = Color.Black,
                fontFamily = FontFamily.Cursive,
                fontStyle = FontStyle.Normal
            )
        }

        error?.let {
            Text(
                text = it,
                color = Color.Red,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }

        Text(
            text = "Daily Calorie Goal",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = FontFamily.Serif,
            color = Color.DarkGray,
            modifier = Modifier
                .padding(bottom = 15.dp)
                .align(Alignment.CenterHorizontally)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(180.dp)
                    .shadow(4.dp, shape = CircleShape, clip = false)
                    .background(brush = boxGradient, shape = CircleShape)
                    .padding(8.dp)
            ) {
                val progress = totalCalories / (dailyCalorieGoal + burnedKcal)

                CircularProgressIndicator(
                    progress = progress.coerceIn(0f, 1f),
                    modifier = Modifier.size(150.dp),
                    color = Color(0xFFFFA726),
                    strokeWidth = 10.dp
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${totalCalories.toInt()} kcal",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = "of ${(dailyCalorieGoal - totalCalories).coerceAtLeast(0f).toInt()} left",
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                    Text(
                        text = "$burnedKcal BURNED",
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                }
            }

            Button(
                onClick = { navController.navigate("running") },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text("Start Running Timer", color = Color.White)
            }
        }

        val context = LocalContext.current

        ShareProgressCard(
            totalCalories = totalCalories,
            burnedCalories = burnedKcal
        ) { textToShare ->
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, textToShare)
                type = "text/plain"
            }
            context.startActivity(Intent.createChooser(intent, "Share your progress via"))
        }

        WaterIntakeCard(
            amountMl = waterState.amountMl,
            goalMl = waterState.goalMl,
            onAddWater = { waterViewModel.addWater() },
            onRemoveWater = { waterViewModel.removeWater() },
            onResetWater = { waterViewModel.resetWater() },
            onGoalSave = { newGoal ->
                waterViewModel.setGoal(newGoal)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            MacroProgress("Protein", "${proteinLeft.toInt()}g", proteinProgress, Color.Green)
            MacroProgress("Fat", "${fatLeft.toInt()}g", fatProgress, Color(0xFF9C27B0))
            MacroProgress("Carbs", "${carbsLeft.toInt()}g", carbsProgress, Color.Blue)
        }

        Text(
            text = "Daily Meals",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (meals.isEmpty()) {
            Text(
                text = "No meals added yet",
                color = Color.Gray,
                modifier = Modifier.padding(16.dp)
            )
        } else {
            meals.forEach { meal ->
                val icon = getIconForFood(meal.food_name)
                val kcal = (meal.calories_per_100g * meal.quantity_g / 100).toInt()

                MealItem(
                    title = meal.food_name,
                    kcal = "$kcal kcal",
                    icon = icon,
                    onDelete = {
                        viewModel.deleteMealLog(meal.id)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
fun WaterIntakeCard(
    amountMl: Int,
    goalMl: Int,
    onAddWater: () -> Unit,
    onRemoveWater: () -> Unit,
    onResetWater: () -> Unit,
    onGoalSave: (Int) -> Unit
) {
    var goalInput by remember(goalMl) { mutableStateOf(goalMl.toString()) }
    val progress = if (goalMl > 0) {
        (amountMl.toFloat() / goalMl.toFloat()).coerceIn(0f, 1f)
    } else {
        0f
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Water Intake",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "$amountMl ml of $goalMl ml",
                fontSize = 15.sp,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = progress,
                color = Color(0xFF03A9F4),
                trackColor = Color.LightGray,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
            )

            if (amountMl > goalMl) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Daily water goal exceeded",
                    color = Color(0xFF00796B),
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onAddWater,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                ) {
                    Text("+250 ml", color = Color.White)
                }

                OutlinedButton(onClick = onRemoveWater) {
                    Text("-250 ml")
                }

                OutlinedButton(onClick = onResetWater) {
                    Text("Reset")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = goalInput,
                    onValueChange = { value ->
                        if (value.all { it.isDigit() }) {
                            goalInput = value
                        }
                    },
                    label = { Text("Goal ml") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.width(140.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Button(
                    onClick = {
                        goalInput.toIntOrNull()?.let { goal ->
                            onGoalSave(goal)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                ) {
                    Text("Save Goal", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun MacroProgress(label: String, value: String, progress: Float, color: Color) {
    Column(
        modifier = Modifier
            .width(90.dp)
            .padding(horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = label, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(text = value, fontSize = 15.sp, fontWeight = FontWeight.Medium)

        LinearProgressIndicator(
            progress = progress,
            color = color,
            modifier = Modifier
                .height(12.dp)
                .fillMaxWidth()
                .padding(top = 8.dp),
            trackColor = Color.LightGray
        )
    }
}

@Composable
fun MealItem(
    title: String,
    kcal: String,
    icon: ImageVector,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .shadow(1.dp, RoundedCornerShape(6.dp))
            .background(color = Color.White, shape = RoundedCornerShape(6.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(32.dp),
            tint = Color(0xFF4CAF50)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp)
        ) {
            Text(text = title, fontWeight = FontWeight.Medium)
            Text(text = kcal, fontWeight = FontWeight.SemiBold)
        }

        IconButton(onClick = onDelete) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete meal",
                tint = Color.Red
            )
        }
    }
}

@Composable
fun getIconForFood(foodName: String): ImageVector {
    return when {
        foodName.contains("Chicken", ignoreCase = true) -> ImageVector.vectorResource(id = R.drawable.baseline_set_meal_24)
        foodName.contains("Beef", ignoreCase = true) -> ImageVector.vectorResource(id = R.drawable.baseline_restaurant_24)
        foodName.contains("Egg", ignoreCase = true) -> ImageVector.vectorResource(id = R.drawable.baseline_egg_24)
        foodName.contains("Vegetable", ignoreCase = true) || foodName.contains("Salad", ignoreCase = true) -> ImageVector.vectorResource(id = R.drawable.baseline_emoji_food_beverage_24)
        foodName.contains("Fish", ignoreCase = true) -> ImageVector.vectorResource(id = R.drawable.baseline_set_meal_24)
        else -> ImageVector.vectorResource(id = R.drawable.baseline_fastfood_24)
    }
}

@Composable
fun ShareProgressCard(
    totalCalories: Float,
    burnedCalories: Float,
    onShare: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFB2EBF2)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Share Your Progress",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            val shareText = "Today I consumed ${totalCalories.toInt()} kcal and burned ${burnedCalories.toInt()} kcal by running!"

            Text(
                text = shareText,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { onShare(shareText) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Share", color = Color.White)
            }
        }
    }
}

fun calculateDailyCalories(
    weightKg: Float,
    heightCm: Float,
    age: Int,
    gender: String,
    activityLevel: String
): Float {
    val bmr = if (gender.equals("Male", ignoreCase = true)) {
        10 * weightKg + 6.25f * heightCm - 5 * age + 5
    } else {
        10 * weightKg + 6.25f * heightCm - 5 * age - 161
    }

    val activityFactor = when (activityLevel) {
        "Sedentary" -> 1.2f
        "Light" -> 1.375f
        "Moderate" -> 1.55f
        "Active" -> 1.725f
        "Very Active" -> 1.9f
        else -> 1.2f
    }

    return bmr * activityFactor
}