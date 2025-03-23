package com.example.studyflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.studyflow.data.local.UserPreferences
import com.example.studyflow.ui.navigation.AppNavigation
import com.example.studyflow.viewmodel.AuthViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.studyflow.ui.theme.StudyFlowTheme

// Factory sederhana untuk AuthViewModel
class AuthViewModelFactory(
    private val userPreferences: UserPreferences
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(userPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Buat instance UserPreferences
        val userPreferences = UserPreferences(applicationContext)

        // Buat AuthViewModel dengan factory
        val authViewModel = ViewModelProvider(
            this,
            AuthViewModelFactory(userPreferences)
        ).get(AuthViewModel::class.java)

        setContent {
            StudyFlowTheme {
                AppNavigation(authViewModel)
            }
        }
    }
}