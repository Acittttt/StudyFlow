package com.example.studyflow.screen.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.studyflow.screen.profile.ProfileScreen
import com.example.studyflow.viewmodel.AuthViewModel
import com.example.studyflow.viewmodel.ProfileViewModel

@Composable
fun DashboardScreen(
    role: String,
    authViewModel: AuthViewModel,
    profileViewModel: ProfileViewModel,
    onLogout: () -> Unit,
    onEditProfile: () -> Unit // Callback untuk edit profile
) {
    val navItems = if (role == "Owner") {
        listOf("MyClass", "AddClass", "Profile")
    } else {
        listOf("Home", "MyClass", "Profile")
    }

    var selectedItem by remember { mutableStateOf(navItems.first()) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                navItems.forEach { item ->
                    val icon = when (item) {
                        "Home" -> Icons.Filled.Home
                        "MyClass" -> Icons.Filled.Email
                        "AddClass" -> Icons.Filled.Add
                        "Profile" -> Icons.Filled.Person
                        else -> Icons.Filled.Home
                    }
                    val isSelected = selectedItem == item
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { selectedItem = item },
                        icon = { Icon(imageVector = icon, contentDescription = item) },
                        label = {
                            AnimatedVisibility(
                                visible = isSelected,
                                enter = fadeIn(),
                                exit = fadeOut()
                            ) {
                                Text(text = item)
                            }
                        },
                        alwaysShowLabel = false
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when (selectedItem) {
                "Profile" -> {
                    ProfileScreen(
                        profileViewModel = profileViewModel,
                        onEditProfile = onEditProfile,
                        onLogout = onLogout
                    )
                }
                else -> {
                    Text("Halaman: $selectedItem (Role: $role)")
                }
            }
        }
    }
}