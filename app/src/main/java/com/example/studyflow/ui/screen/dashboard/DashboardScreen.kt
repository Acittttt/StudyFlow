package com.example.studyflow.screen.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.studyflow.screen.profile.ProfileScreen
import com.example.studyflow.viewmodel.AuthViewModel

@Composable
fun DashboardScreen(
    role: String,
    authViewModel: AuthViewModel,
    onLogout: () -> Unit
) {
    // Tentukan item bottom navigation berdasarkan role
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
                    NavigationBarItem(
                        label = { Text(item) },
                        icon = { Icon(Icons.Filled.Home, contentDescription = item) },
                        selected = selectedItem == item,
                        onClick = { selectedItem = item }
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
                    // Panggil ProfileScreen yang sudah memiliki tombol Logout
                    ProfileScreen(
                        authViewModel = authViewModel,
                        onLogout = onLogout
                    )
                }
                else -> {
                    // Tampilkan halaman lain sesuai dengan selectedItem
                    Text("Halaman: $selectedItem (Role: $role)")
                }
            }
        }
    }
}