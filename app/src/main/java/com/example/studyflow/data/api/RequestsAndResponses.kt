package com.example.studyflow.data.api

data class RegisterRequest(
    val full_name: String,
    val username: String,
    val password: String,
    val email: String,
    val role: String
)

data class LoginRequest(
    val username: String,
    val password: String
)

data class UserResponse(
    val id: Int,
    val full_name: String,
    val username: String,
    val email: String,
    val role: String
)

data class AuthResponse(
    val message: String,
    val user: UserResponse?,
    val token: String?
)
