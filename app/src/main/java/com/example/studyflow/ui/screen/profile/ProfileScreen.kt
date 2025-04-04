package com.example.studyflow.screen.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.studyflow.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel,
    onEditProfile: () -> Unit,
    onLogout: () -> Unit
) {
    val userProfile = profileViewModel.userProfile.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.size(150.dp)
        ) {
            if (userProfile?.profile_picture_url?.isNotEmpty() == true) {
                Image(
                    painter = rememberAsyncImagePainter(userProfile.profile_picture_url),
                    contentDescription = "Profile Picture",
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Image(
                    painter = rememberAsyncImagePainter("https://via.placeholder.com/150"),
                    contentDescription = "Default Profile Picture",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Username: ${userProfile?.username ?: ""}")
        Text(text = "Nama Lengkap: ${userProfile?.full_name ?: ""}")
        Text(text = "Email: ${userProfile?.email ?: ""}")
        Text(text = "Alamat: ${userProfile?.alamat ?: ""}")
        Text(text = "Role: ${userProfile?.role ?: ""}")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { onEditProfile() }) {
            Text(text = "Edit Profile")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            profileViewModel.logout()
            onLogout()
        }) {
            Text(text = "Logout")
        }
    }
}
