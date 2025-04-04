package com.example.studyflow.screen.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.studyflow.viewmodel.ForgotPasswordStep
import com.example.studyflow.viewmodel.ForgotPasswordViewModel

@Composable
fun ForgotPasswordScreen(
    forgotPasswordViewModel: ForgotPasswordViewModel,
    onPasswordResetSuccess: () -> Unit,
    onCancel: () -> Unit
) {
    val step by forgotPasswordViewModel.step.collectAsState()

    when (step) {
        ForgotPasswordStep.ValidateUser -> ForgotPasswordValidateScreen(
            onValidate = { username, email ->
                forgotPasswordViewModel.validateUser(username, email)
            },
            onCancel = onCancel
        )
        ForgotPasswordStep.ResetPassword -> ForgotPasswordResetScreen(
            onSave = { newPassword, username, email ->
                forgotPasswordViewModel.updatePassword(newPassword, username, email, onPasswordResetSuccess)
            },
            onCancel = onCancel
        )
    }
}

@Composable
fun ForgotPasswordValidateScreen(
    onValidate: (String, String) -> Unit,
    onCancel: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    val isValid = username.isNotBlank() && email.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Lupa Password", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Masukkan Username") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Masukkan Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { onValidate(username, email) },
            enabled = isValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Next")
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = onCancel) {
            Text("Batal")
        }
    }
}

@Composable
fun ForgotPasswordResetScreen(
    onSave: (String, String, String) -> Unit,
    onCancel: () -> Unit
) {
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    // Pastikan juga untuk meneruskan username dan email dari validasi sebelumnya
    // Anda bisa menyimpan state tersebut di ViewModel atau melalui parameter; untuk contoh ini kita anggap sudah tersimpan.
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    val isValid = newPassword.isNotBlank() &&
            confirmPassword.isNotBlank() &&
            newPassword == confirmPassword &&
            username.isNotBlank() && email.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Reset Password", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))
        // Jika memungkinkan, tampilkan username dan email dari langkah validasi
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = newPassword,
            onValueChange = { newPassword = it },
            label = { Text("Masukkan Password Baru") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Ulangi Password Baru") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { onSave(newPassword, username, email) },
            enabled = isValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Simpan")
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = onCancel) {
            Text("Batal")
        }
    }
}