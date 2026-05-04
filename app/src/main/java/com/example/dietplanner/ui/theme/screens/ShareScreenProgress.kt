package com.example.dietplanner.ui.theme.screens

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.dietplanner.ui.theme.screens.viewmodel.MealViewModel
import com.example.dietplanner.ui.viewmodel.MealLogViewModel
import kotlinx.coroutines.delay

@Composable
fun ShareScreenProgress(
    mealLogViewModel: MealLogViewModel = hiltViewModel(),
    mealViewModel: MealViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val meals by mealLogViewModel.meals.collectAsState()
    val burnedCalories by mealViewModel.burnedCalories.collectAsState()

    val totalCalories = meals.sumOf { (it.calories_per_100g * it.quantity_g / 100).toInt() }

    var shareText by remember {
        mutableStateOf("Today I consumed $totalCalories kcal and burned ${burnedCalories.toInt()} kcal by running!")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            "Share Your Progress",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F7FA)),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Preview:", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(shareText, style = MaterialTheme.typography.bodyLarge)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedButton(onClick = {
                shareText = "Feeling great! Burned ${burnedCalories.toInt()} kcal and ate $totalCalories kcal today 💪"
            }) {
                Icon(Icons.Default.Refresh, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Regenerate")
            }

            Button(onClick = {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, shareText)
                    type = "text/plain"
                }
                context.startActivity(Intent.createChooser(intent, "Share your progress via"))
            }) {
                Icon(Icons.Default.Share, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Share")
            }
        }
    }
}
