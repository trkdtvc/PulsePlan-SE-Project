package com.example.dietplanner.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.dietplanner.ui.theme.screens.viewmodel.MealViewModel
import com.example.dietplanner.ui.viewmodel.UserViewModel
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun RunningTimerScreen(
    userViewModel: UserViewModel = hiltViewModel(),
    onFinish: (burnedCalories: Int) -> Unit
) {
    var secondsElapsed by remember { mutableStateOf(0) }
    var isRunning by remember { mutableStateOf(false) }

    val user by userViewModel.user.collectAsState()

    LaunchedEffect(isRunning) {
        while (isRunning) {
            delay(1000)
            secondsElapsed++
        }
    }

    val minutes = secondsElapsed / 60
    val seconds = secondsElapsed % 60

    val metValue = 9.8
    val weightKg = user?.weight_kg ?: 70f
    val burnedCalories = ((metValue * 3.5 * weightKg) / 200 * (secondsElapsed / 60f)).roundToInt()

    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFB2FEFA), Color(0xFF0ED2F7))
    )

    val timerText = String.format("%02d:%02d", minutes, seconds)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundGradient)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Running Timer",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.Black,
            modifier = Modifier.padding(top = 32.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Timer
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(220.dp)
                .shadow(6.dp, shape = CircleShape)
                .background(Color.White, shape = CircleShape)
        ) {
            Text(
                text = timerText,
                fontSize = 48.sp,
                color = Color(0xFF00796B),
                style = MaterialTheme.typography.displayMedium,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Calories burned: $burnedCalories",
            fontSize = 20.sp,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(40.dp))


        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = { isRunning = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00796B))
            ) {
                Text("Start", color = Color.White)
            }
            Button(
                onClick = { isRunning = false },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA000))
            ) {
                Text("Pause", color = Color.White)
            }
            Button(
                onClick = {
                    isRunning = false
                    onFinish(burnedCalories)
                    secondsElapsed = 0
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
            ) {
                Text("Finish", color = Color.White)
            }
        }
    }
}
