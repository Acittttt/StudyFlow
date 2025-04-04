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

data class ForgotPasswordRequest(
    val username: String,
    val email: String,
    val newPassword: String
)

data class GenericResponse(
    val message: String
)

data class VerifyUserRequest(
    val username: String,
    val email: String
)

data class VerifyUserResponse(
    val valid: Boolean,
    val message: String?
)

data class ProfileUpdateRequest(
    val full_name: String,
    val username: String,
    val email: String,
    val alamat: String,
    val profile_picture_url: String? = null
)

data class UserProfileResponse(
    val id: Int,
    val full_name: String,
    val username: String,
    val email: String,
    val alamat: String,
    val profile_picture_url: String,
    val role: String
)
