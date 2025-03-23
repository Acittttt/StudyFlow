package com.example.studyflow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studyflow.data.api.AuthResponse
import com.example.studyflow.data.api.LoginRequest
import com.example.studyflow.data.api.RegisterRequest
import com.example.studyflow.data.api.RetrofitClient
import com.example.studyflow.data.local.UserPreferences
import com.example.studyflow.data.api.UserResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.firstOrNull
import retrofit2.Response

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val authResponse: AuthResponse) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState = _authState.asStateFlow()

    init {
        // Saat ViewModel dibuat, cek apakah sudah ada token di DataStore.
        viewModelScope.launch {
            val existingToken = userPreferences.fetchToken()
            val existingRole = userPreferences.fetchRole()

            if (!existingToken.isNullOrBlank() && !existingRole.isNullOrBlank()) {
                // Misalnya kita langsung set ke Success agar user skip login
                // (Anda bisa verifikasi token ke server di sini jika ingin)
                _authState.value = AuthState.Success(
                    AuthResponse(
                        message = "Already Logged In",
                        user = UserResponse(
                            id = 0,
                            full_name = "",
                            username = "",
                            email = "",
                            role = existingRole
                        ),
                        token = existingToken
                    )
                )
            }
        }
    }

    fun registerUser(fullName: String, username: String, password: String, email: String, role: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val response = RetrofitClient.apiService.registerUser(
                    RegisterRequest(fullName, username, password, email, role)
                )
                handleResponse(response)
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "An error occurred")
            }
        }
    }

    fun loginUser(username: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val response = RetrofitClient.apiService.loginUser(
                    LoginRequest(username, password)
                )
                handleResponse(response)
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "An error occurred")
            }
        }
    }

    private fun handleResponse(response: Response<AuthResponse>) {
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                _authState.value = AuthState.Success(body)

                // Jika ada token & role, simpan ke DataStore
                val token = body.token
                val role = body.user?.role
                if (!token.isNullOrBlank() && !role.isNullOrBlank()) {
                    viewModelScope.launch {
                        userPreferences.saveTokenAndRole(token, role)
                    }
                }
            } else {
                _authState.value = AuthState.Error("Response body is null")
            }
        } else {
            val errorMessage = response.errorBody()?.string() ?: "Unknown error"
            _authState.value = AuthState.Error(errorMessage)
        }
    }

    // Fungsi logout, jika diperlukan
    fun logout() {
        viewModelScope.launch {
            userPreferences.clear()
            _authState.value = AuthState.Idle
        }
    }
}