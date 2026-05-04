package com.example.dietplanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.dietplanner.ui.theme.screens.RegistrationScreen
import com.example.dietplanner.ui.theme.DietPlannerTheme
import com.example.dietplanner.ui.theme.screens.RegistrationScreen
import com.example.dietplanner.ui.theme.screens.LoginScreen
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.dietplanner.ui.theme.navigation.DietPlannerAppNav
import com.example.dietplanner.ui.theme.screens.HomeScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            DietPlannerTheme {
                DietPlannerAppNav()
            }

        }
    }
}



