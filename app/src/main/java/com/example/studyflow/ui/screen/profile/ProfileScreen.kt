package com.example.studyflow.screen.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.studyflow.viewmodel.AuthViewModel

@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Ini Halaman Profile")
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                authViewModel.logout()  // Ubah state menjadi Idle dan hapus token
                onLogout() // Jika diperlukan callback tambahan
            }
        ) {
            Text("Logout")
        }
    }
}