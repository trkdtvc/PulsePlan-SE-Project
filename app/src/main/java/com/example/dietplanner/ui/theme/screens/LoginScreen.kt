package com.example.dietplanner.ui.theme.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.dietplanner.ui.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    navController: NavController,
    viewModel: UserViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }

    val user by viewModel.user.collectAsState()
    val error by viewModel.error.collectAsState()

    val lightGreenGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFB9F6CA), Color(0xFF69F0AE), Color(0xFF00E676))
    )

    val isValid = email.isNotBlank() && password.length >= 8

    // ⚡ Reaguj na uspješan login
    LaunchedEffect(user) {
        if (user != null) {
            Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show()
            onLoginSuccess()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = lightGreenGradient),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .background(Color.White, RoundedCornerShape(16.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Login", style = MaterialTheme.typography.headlineMedium, color = Color.Black)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = if (it.isBlank()) "Email is required" else ""
                },
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                isError = emailError.isNotEmpty()
            )
            if (emailError.isNotEmpty()) {
                Text(emailError, color = Color.Red, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = when {
                        it.isBlank() -> "Password is required"
                        it.length < 8 -> "Password must be at least 8 characters"
                        else -> ""
                    }
                },
                label = { Text("Password") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                trailingIcon = {
                    val icon = if (passwordVisible) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(icon, contentDescription = "Toggle password visibility", tint = Color.Gray)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                isError = passwordError.isNotEmpty()
            )
            if (passwordError.isNotEmpty()) {
                Text(passwordError, color = Color.Red, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (!isValid) {
                        emailError = if (email.isBlank()) "Email is required" else ""
                        passwordError = when {
                            password.isBlank() -> "Password is required"
                            password.length < 8 -> "Password must be at least 8 characters"
                            else -> ""
                        }
                        return@Button
                    }

                    coroutineScope.launch {
                        viewModel.login(email.trim(), password)
                        if (viewModel.user.value == null) {
                            Toast.makeText(context, error ?: "Login failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007f9c))
            ) {
                Text("Login", color = Color.White)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Don't have an account? Register",
                color = Color.Blue,
                modifier = Modifier.clickable {
                    navController.navigate("registration")
                }
            )
        }
    }
}
