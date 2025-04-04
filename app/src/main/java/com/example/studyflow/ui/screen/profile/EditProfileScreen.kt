package com.example.studyflow.screen.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.studyflow.viewmodel.ProfileViewModel

@Composable
fun EditProfileScreen(
    profileViewModel: ProfileViewModel,
    onProfileUpdated: () -> Unit,
    onCancel: () -> Unit
) {
    val currentProfile = profileViewModel.userProfile.value

    var fullName by remember { mutableStateOf(currentProfile?.full_name ?: "") }
    var username by remember { mutableStateOf(currentProfile?.username ?: "") }
    var email by remember { mutableStateOf(currentProfile?.email ?: "") }
    var alamat by remember { mutableStateOf(currentProfile?.alamat ?: "") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // Launcher untuk memilih gambar dari galeri
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        // Tampilkan preview gambar
        if (imageUri != null) {
            Image(
                painter = rememberAsyncImagePainter(imageUri),
                contentDescription = "Selected Profile Picture",
                modifier = Modifier
                    .size(150.dp)
                    .padding(bottom = 16.dp)
            )
        } else {
            val imageToShow = currentProfile?.profile_picture_url ?: "https://via.placeholder.com/150"
            Image(
                painter = rememberAsyncImagePainter(imageToShow),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(150.dp)
                    .padding(bottom = 16.dp)
            )
        }

        Button(onClick = { launcher.launch("image/*") }) {
            Text(text = "Pilih Gambar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text(text = "Nama Lengkap") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text(text = "Username") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(text = "Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = alamat,
            onValueChange = { alamat = it },
            label = { Text(text = "Alamat") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = {
                // Panggil updateProfile dengan imageUri langsung
                profileViewModel.updateProfile(fullName, username, email, alamat, imageUri)
                onProfileUpdated()
            }) {
                Text(text = "Simpan")
            }

            Button(onClick = { onCancel() }) {
                Text(text = "Batal")
            }
        }
    }
}
