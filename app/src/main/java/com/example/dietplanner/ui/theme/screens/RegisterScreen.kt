package com.example.dietplanner.ui.theme.screens

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Lock
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
import com.example.dietplanner.model.User
import com.example.dietplanner.ui.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun RegistrationScreen(onRegistrationSuccess: () -> Unit) {
    val userViewModel: UserViewModel = hiltViewModel()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var sex by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }

    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    val isValid = username.isNotBlank() &&
            email.isNotBlank() && isValidEmail(email) &&
            password.length >= 8 &&
            sex.isNotBlank() &&
            age.toIntOrNull() != null &&
            weight.toFloatOrNull() != null &&
            height.toFloatOrNull() != null

    val gradient = Brush.verticalGradient(
        listOf(Color(0xFFB9F6CA), Color(0xFF69F0AE), Color(0xFF00E676))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradient)
            .verticalScroll(scrollState),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .padding(24.dp)
                .background(Color.White, shape = RoundedCornerShape(16.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Register", style = MaterialTheme.typography.headlineMedium, color = Color.Black)
            Spacer(modifier = Modifier.height(16.dp))

            // Email
            InputField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = when {
                        it.isBlank() -> "Email is required"
                        !isValidEmail(it) -> "Invalid email"
                        !it.contains("stu.ibu.edu") -> "Must be stu.ibu.edu email"
                        else -> ""
                    }
                },
                label = "Email",
                icon = Icons.Default.Email,
                errorMessage = emailError
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Password
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = when {
                        it.isBlank() -> "Password is required"
                        it.length < 8 -> "Min 8 characters"
                        else -> ""
                    }
                },
                label = { Text("Password") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                trailingIcon = {
                    val icon = if (passwordVisible) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = icon, contentDescription = null)
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                isError = passwordError.isNotEmpty()
            )
            if (passwordError.isNotEmpty()) {
                Text(passwordError, color = Color.Red, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Name
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Gender
            Text("Gender", color = Color.Black)
            Row {
                listOf("Male", "Female").forEach { gender ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .clickable { sex = gender }
                    ) {
                        RadioButton(selected = sex == gender, onClick = { sex = gender })
                        Text(gender)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Age, Weight, Height
            OutlinedTextField(value = age, onValueChange = { age = it }, label = { Text("Age") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = weight, onValueChange = { weight = it }, label = { Text("Weight (kg)") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = height, onValueChange = { height = it }, label = { Text("Height (cm)") }, modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(16.dp))

            // Submit
            Button(
                onClick = {
                    if (!isValid) {
                        Toast.makeText(context, "Please correct form fields", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val user = User(
                        fullName = username,
                        username = username,
                        email = email,
                        password_hash = password,
                        height_cm = height.toFloat(),
                        weight_kg = weight.toFloat(),
                        age = age.toInt(),
                        gender = sex,
                        activity_level = "moderate",
                        daily_calorie_goal = 2200f
                    )

                    coroutineScope.launch {
                        userViewModel.register(user)
                        Toast.makeText(context, "Registration successful", Toast.LENGTH_SHORT).show()
                        onRegistrationSuccess()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007f9c))
            ) {
                Text("Register", color = Color.White)
            }
        }
    }
}

@Composable
fun InputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    errorMessage: String = ""
) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            leadingIcon = { Icon(icon, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            isError = errorMessage.isNotEmpty()
        )
        if (errorMessage.isNotEmpty()) {
            Text(errorMessage, color = Color.Red, style = MaterialTheme.typography.bodySmall)
        }
    }
}

fun isValidEmail(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}
