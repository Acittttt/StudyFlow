package com.example.studyflow.screen.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.studyflow.viewmodel.ProfileViewModel
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import coil.compose.AsyncImage

@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel,
    onEditProfile: () -> Unit,
    onLogout: () -> Unit
) {
    val tokenFlow = profileViewModel.tokenFlow
    val token by tokenFlow.collectAsState(initial = null)

    LaunchedEffect(token) {
        if (!token.isNullOrEmpty()) {
            profileViewModel.getUserProfile()
        } else {
            profileViewModel.userProfile.value = null
        }
    }

    val userProfile = profileViewModel.userProfile.value

    if (userProfile == null) {
        Text("Loading profile or no data yet...")
        return
    }
    Column(Modifier.verticalScroll(rememberScrollState())) {
        AsyncImage(
            model = userProfile.profile_picture_url ?: "",
            contentDescription = "Profile Picture"
        )
        Text("Full Name: ${userProfile.full_name}")
        Text("Username: ${userProfile.username}")
        Text("Email: ${userProfile.email}")
        Text("Alamat: ${userProfile.alamat}")
        Text("Role: ${userProfile.role}")
        Text("Created At: ${userProfile.created_at}")

        Spacer(modifier = Modifier.height(16.dp))

        // Tombol edit dan logout
        Button(onClick = onEditProfile) {
            Text(text = "Edit Profile")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            profileViewModel.logout()
            onLogout()
        }) {
            Text(text = "Logout")
        }
    }
}