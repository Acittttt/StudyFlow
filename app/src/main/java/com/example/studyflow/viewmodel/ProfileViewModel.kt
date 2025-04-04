package com.example.studyflow.viewmodel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.studyflow.data.api.ApiService
import com.example.studyflow.data.api.RetrofitClient
import com.example.studyflow.data.api.UserProfileResponse
import com.example.studyflow.data.local.UserPreferences
import com.example.studyflow.util.getFileFromUri
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val apiService: ApiService = RetrofitClient.apiService
    private val userPreferences = UserPreferences(application.applicationContext)
    val userProfile = mutableStateOf<UserProfileResponse?>(null)
    val tokenFlow = userPreferences.getToken

    init {
        getUserProfile()
    }

    fun getUserProfile() {
        viewModelScope.launch {
            val token = userPreferences.fetchToken()
            if (token != null) {
                try {
                    val response = apiService.getProfile("Bearer $token")
                    Log.d("ProfileViewModel", "Response code: ${response.code()}")
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null) {
                            userProfile.value = body.profile
                        }
                    } else {
                        val errorMessage = response.errorBody()?.string()
                        Log.e("ProfileViewModel", "Error: $errorMessage")
                    }
                } catch (e: Exception) {
                    Log.e("ProfileViewModel", "Exception: ${e.message}", e)
                }
            } else {
                Log.d("ProfileViewModel", "Token is null or empty")
            }
        }
    }

    // Ubah parameter imageFile menjadi imageUri (tipe Uri?)
    fun updateProfile(
        fullName: String,
        username: String,
        email: String,
        alamat: String,
        imageUri: Uri? = null
    ) {
        viewModelScope.launch {
            val token = userPreferences.fetchToken()
            Log.d("ProfileViewModel", "Token yang didapat di updateProfile: $token")
            if (token != null) {
                try {
                    val fullNameBody = RequestBody.create("text/plain".toMediaTypeOrNull(), fullName)
                    val usernameBody = RequestBody.create("text/plain".toMediaTypeOrNull(), username)
                    val emailBody = RequestBody.create("text/plain".toMediaTypeOrNull(), email)
                    val alamatBody = RequestBody.create("text/plain".toMediaTypeOrNull(), alamat)

                    // Konversi imageUri menjadi File
                    val imageFile: File? = imageUri?.let { getFileFromUri(getApplication(), it) }
                    val imagePart = imageFile?.let {
                        val mediaType = when {
                            it.name.endsWith(".png", ignoreCase = true) -> "image/png"
                            it.name.endsWith(".jpg", ignoreCase = true) || it.name.endsWith(".jpeg", ignoreCase = true) -> "image/jpeg"
                            it.name.endsWith(".heic", ignoreCase = true) || it.name.endsWith(".heif", ignoreCase = true) -> "image/heic"
                            else -> "image/jpeg"
                        }
                        val requestFile = RequestBody.create(mediaType.toMediaTypeOrNull(), it)
                        MultipartBody.Part.createFormData("profile_picture", it.name, requestFile)
                    }

                    val response = apiService.updateProfile(
                        "Bearer $token",
                        fullNameBody,
                        usernameBody,
                        emailBody,
                        alamatBody,
                        imagePart
                    )
                    Log.d("ProfileViewModel", "Response code updateProfile: ${response.code()}")
                    if (response.isSuccessful) {
                        val body = response.body() // Tipe: UpdateProfileApiResponse?
                        if (body != null) {
                            userProfile.value = body.profile
                        }
                    } else {
                        Log.d("ProfileViewModel", "Error updateProfile: ${response.errorBody()?.string()}")
                    }
                } catch (e: Exception) {
                    Log.e("ProfileViewModel", "Exception in updateProfile: ${e.message}", e)
                }
            } else {
                Log.d("ProfileViewModel", "Token is null in updateProfile")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            userPreferences.clear()
            userProfile.value = null
        }
    }
}