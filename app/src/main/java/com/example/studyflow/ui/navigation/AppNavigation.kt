package com.example.studyflow.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.studyflow.screen.auth.ForgotPasswordScreen
import com.example.studyflow.screen.auth.LoginScreen
import com.example.studyflow.screen.auth.RegisterScreen
import com.example.studyflow.screen.dashboard.DashboardScreen
import com.example.studyflow.screen.onBoarding.OnBoardingScreen
import com.example.studyflow.screen.profile.EditProfileScreen
import com.example.studyflow.screen.profile.ProfileScreen
import com.example.studyflow.viewmodel.AuthState
import com.example.studyflow.viewmodel.AuthViewModel
import com.example.studyflow.viewmodel.ForgotPasswordViewModel
import com.example.studyflow.viewmodel.ProfileViewModel

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel,
    profileViewModel: ProfileViewModel
) {
    val navController = rememberNavController()
    val authState = authViewModel.authState.collectAsState()

    LaunchedEffect(authState.value) {
        if (authState.value is AuthState.Idle) {
            navController.navigate("login") {
                popUpTo(0)
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
                },
                onNavigateToForgotPassword = {
                    navController.navigate("forgotPassword")
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
        composable("forgotPassword") {
            val forgotPasswordViewModel = remember { ForgotPasswordViewModel() }
            ForgotPasswordScreen(
                forgotPasswordViewModel = forgotPasswordViewModel,
                onPasswordResetSuccess = {
                    navController.navigate("login") {
                        popUpTo("forgotPassword") { inclusive = true }
                    }
                },
                onCancel = {
                    navController.popBackStack()
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
                profileViewModel = profileViewModel,
                onLogout = {
                    authViewModel.logout()
                },
                onEditProfile = {
                    navController.navigate("editProfile")
                }
            )
        }
        composable("profile") {
            ProfileScreen(
                profileViewModel = profileViewModel,
                onEditProfile = { navController.navigate("editProfile") },
                onLogout = {
                    profileViewModel.logout()
                    navController.navigate("login") {
                        popUpTo("profile") { inclusive = true }
                    }
                }
            )
        }
        composable("editProfile") {
            EditProfileScreen(
                profileViewModel = profileViewModel,
                onProfileUpdated = { navController.popBackStack() },
                onCancel = { navController.popBackStack() }
            )
        }
    }
}