package com.example.studyflow.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studyflow.data.api.ForgotPasswordRequest
import com.example.studyflow.data.api.RetrofitClient
import com.example.studyflow.data.api.VerifyUserRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

enum class ForgotPasswordStep {
    ValidateUser,
    ResetPassword
}

class ForgotPasswordViewModel : ViewModel() {

    private val _step = MutableStateFlow(ForgotPasswordStep.ValidateUser)
    val step: StateFlow<ForgotPasswordStep> = _step

    private val _isUserValid = MutableStateFlow(false)
    val isUserValid: StateFlow<Boolean> = _isUserValid

    // Fungsi untuk memverifikasi username dan email melalui backend
    fun validateUser(username: String, email: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.verifyUser(
                    VerifyUserRequest(username, email)
                )
                if (response.isSuccessful) {
                    val verifyResponse = response.body()
                    Log.d("ForgotPasswordVM", "Verifikasi berhasil: $verifyResponse")
                    if (verifyResponse?.valid == true) {
                        _isUserValid.value = true
                        _step.value = ForgotPasswordStep.ResetPassword
                    } else {
                        _isUserValid.value = false
                        Log.d("ForgotPasswordVM", "Verifikasi gagal: ${verifyResponse?.message}")
                    }
                } else {
                    _isUserValid.value = false
                    Log.d("ForgotPasswordVM", "Error verifyUser: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                _isUserValid.value = false
                Log.e("ForgotPasswordVM", "Exception verifyUser: ${e.message}", e)
            }
        }
    }

    // Fungsi updatePassword tetap sama (memanggil forgotPassword endpoint) misalnya:
    fun updatePassword(newPassword: String, username: String, email: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                // Buat request forgot password
                val request = ForgotPasswordRequest(username, email, newPassword)
                val response = RetrofitClient.apiService.forgotPassword(request)
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    Log.d("ForgotPasswordVM", "Error updatePassword: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("ForgotPasswordVM", "Exception updatePassword: ${e.message}", e)
            }
        }
    }
}