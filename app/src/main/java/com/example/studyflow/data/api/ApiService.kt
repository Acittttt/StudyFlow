package com.example.studyflow.data.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("auth/register")
    suspend fun registerUser(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("auth/login")
    suspend fun loginUser(@Body request: LoginRequest): Response<AuthResponse>

    @PUT("auth/forgot-password")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest): Response<GenericResponse>

    @POST("auth/verify-user")
    suspend fun verifyUser(@Body request: VerifyUserRequest): Response<VerifyUserResponse>

    // Endpoint untuk mengambil profil pengguna
    @GET("profile/data")
    suspend fun getProfile(@Header("Authorization") token: String): retrofit2.Response<ProfileApiResponse>

    // Endpoint update profile dengan multipart request (termasuk file gambar)
    @Multipart
    @PUT("profile/edit")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Part("full_name") fullName: RequestBody,
        @Part("username") username: RequestBody,
        @Part("email") email: RequestBody,
        @Part("alamat") alamat: RequestBody,
        @Part profile_picture: MultipartBody.Part? = null
    ): Response<UpdateProfileApiResponse>
}
