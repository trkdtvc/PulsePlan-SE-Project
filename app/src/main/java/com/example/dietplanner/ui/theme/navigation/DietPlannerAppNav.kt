package com.example.dietplanner.ui.theme.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.dietplanner.ui.screens.RunningTimerScreen
import com.example.dietplanner.ui.theme.screens.AddMealScreen
import com.example.dietplanner.ui.theme.screens.HomeScreen
import com.example.dietplanner.ui.theme.screens.LoginScreen
import com.example.dietplanner.ui.theme.screens.RegistrationScreen
import com.example.dietplanner.ui.theme.screens.ShareScreenProgress
import com.example.dietplanner.ui.theme.screens.UserProfileScreen
import com.example.dietplanner.ui.theme.screens.navigations.BottomNavBar
import com.example.dietplanner.ui.viewmodel.MealLogViewModel
import com.example.dietplanner.ui.theme.screens.viewmodel.MealViewModel
import com.example.dietplanner.ui.theme.screens.viewmodel.ShareProgressViewModel

@Composable
fun DietPlannerAppNav() {
    val navController = rememberNavController()

    // Inject shared ViewModel
    val mealViewModel: MealLogViewModel = hiltViewModel()
    val mealVm: MealViewModel = hiltViewModel()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in listOf("home", "profile", "add")

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("registration") {
                RegistrationScreen(onRegistrationSuccess = {
                    navController.navigate("login") {
                        popUpTo("registration") { inclusive = true }
                    }
                })
            }
            composable("login") {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    },
                    navController = navController
                )
            }
            composable("home") {
                HomeScreen(viewModel = mealViewModel, mealVm = mealVm, navController = navController)
            }
            composable("profile") {
                UserProfileScreen(navController = navController)
            }
            composable("add") {
                AddMealScreen(
                    navController = navController,
                    mealLogViewModel = mealViewModel
                )
            }
            composable("running") {
                RunningTimerScreen(
                    onFinish = { burnedCalories ->
                        mealVm.setCaloriesBurned(burnedCalories.toFloat())
                        navController.navigate("home")
                    }
                )
            }
            composable("share_progress") {
                ShareScreenProgress()
            }
        }
    }
}
