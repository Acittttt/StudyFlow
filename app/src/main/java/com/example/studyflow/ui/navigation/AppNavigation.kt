package com.example.studyflow.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.studyflow.screen.auth.LoginScreen
import com.example.studyflow.screen.auth.RegisterScreen
import com.example.studyflow.screen.dashboard.DashboardScreen
import com.example.studyflow.screen.onBoarding.OnBoardingScreen
import com.example.studyflow.viewmodel.AuthState
import com.example.studyflow.viewmodel.AuthViewModel

@Composable
fun AppNavigation(authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    val authState = authViewModel.authState.collectAsState()

    // Jika authState Idle (misalnya setelah logout), navigasikan ke login
    LaunchedEffect(authState.value) {
        if (authState.value is AuthState.Idle) {
            navController.navigate("login") {
                popUpTo(0) // Bersihkan seluruh back stack
            }
        }
    }

    NavHost(navController, startDestination = "onboarding") {
        composable("onboarding") {
            OnBoardingScreen(
                onGetStarted = {
                    navController.navigate("login") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            )
        }
        composable("login") {
            LoginScreen(
                authViewModel = authViewModel,
                onLoginSuccess = { role ->
                    navController.navigate("dashboard/$role") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate("register")
                }
            )
        }
        composable("register") {
            RegisterScreen(
                authViewModel = authViewModel,
                onRegisterSuccess = {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            )
        }
        composable(
            route = "dashboard/{role}",
            arguments = listOf(navArgument("role") { type = NavType.StringType })
        ) { backStackEntry ->
            val role = backStackEntry.arguments?.getString("role") ?: "User"
            DashboardScreen(
                role = role,
                authViewModel = authViewModel,
                onLogout = {
                    // Panggil logout di ProfileScreen (jika ingin memaksa navigasi langsung)
                    // Namun, dengan LaunchedEffect di atas, jika authState menjadi Idle, 
                    // navigasi akan langsung terjadi.
                    authViewModel.logout()
                }
            )
        }
    }
}