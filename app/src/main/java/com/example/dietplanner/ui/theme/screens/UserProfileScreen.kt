package com.example.dietplanner.ui.theme.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.dietplanner.ui.viewmodel.UserViewModel

@Composable
fun UserProfileScreen(navController: NavController) {
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    var activityLevel by remember { mutableStateOf("Active") }
    var goal by remember { mutableStateOf("Maintain Weight") }
    val userViewModel: UserViewModel = hiltViewModel()
    val user by userViewModel.user.collectAsState()

    val fullName = user?.fullName ?: "Loading..."
    val ageText = user?.age?.toString() ?: "Loading..."
    val heightText = user?.height_cm?.toString() ?: "Loading..."
    val weightText = user?.weight_kg?.toString() ?: "Loading..."

    var showDialog by remember { mutableStateOf(false) }
    var currentField by remember { mutableStateOf("") }
    var currentValue by remember { mutableStateOf("") }

    fun openEditDialog(field: String, value: String) {
        currentField = field
        currentValue = value
        showDialog = true
    }

    val lightGreenGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFB9F6CA), Color(0xFF69F0AE), Color(0xFF00E676))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = lightGreenGradient)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "User Profile",
            fontSize = 32.sp,
            color = Color.Black,
            modifier = Modifier
                .padding(vertical = 20.dp)
                .align(Alignment.Start)
        )

        Box(
            modifier = Modifier
                .size(140.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
                .clickable { imagePickerLauncher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            if (imageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(imageUri),
                    contentDescription = "Profile Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Default Profile Picture",
                    modifier = Modifier.size(80.dp),
                    tint = Color.DarkGray
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text(fullName, fontSize = 28.sp)

        Spacer(modifier = Modifier.height(24.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            ProfileItem("Age: $ageText") { openEditDialog("Age", ageText) }
            ProfileItem("Height: ${heightText} cm") { openEditDialog("Height", heightText) }
            ProfileItem("Weight: ${weightText} kg") { openEditDialog("Weight", weightText) }
            ProfileItem("Activity Level: $activityLevel") { openEditDialog("Activity Level", activityLevel) }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                userViewModel.logout()
                navController.navigate("login") {
                    popUpTo("home") { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007f9c))
        ) {
            Text("Sign Out", color = Color.White)
        }
    }

    if (showDialog) {
        EditDialog(
            field = currentField,
            currentValue = currentValue,
            onDismiss = { showDialog = false },
            onSave = { newValue ->
                when (currentField) {
                    "Age" -> {}
                    "Height" -> {}
                    "Weight" -> {}
                    "Activity Level" -> activityLevel = newValue
                    "Goal" -> goal = newValue
                }
                showDialog = false
            }
        )
    }
}



@Composable
fun ProfileItem(text: String, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text, fontSize = 18.sp)
            Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDialog(
    field: String,
    currentValue: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var updatedValue by remember { mutableStateOf(currentValue) }

    val activityOptions = listOf(
        "Basal Metabolic Rate (BMR)",
        "Sedentary",
        "Light",
        "Moderate",
        "Active",
        "Very Active",
        "Extra Active"
    )
    val goalOptions = listOf("Lose Weight", "Maintain Weight", "Gain Weight")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit $field") },
        text = {
            when (field) {
                "Activity Level" -> {
                    DropdownSelector(
                        options = activityOptions,
                        selected = updatedValue,
                        label = "Select Activity Level",
                        onSelected = { updatedValue = it }
                    )
                }
                "Goal" -> {
                    DropdownSelector(
                        options = goalOptions,
                        selected = updatedValue,
                        label = "Select Goal",
                        onSelected = { updatedValue = it }
                    )
                }
                else -> {
                    OutlinedTextField(
                        value = updatedValue,
                        onValueChange = { updatedValue = it },
                        label = { Text("Enter new $field") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onSave(updatedValue) }) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownSelector(
    options: List<String>,
    selected: String,
    label: String,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
