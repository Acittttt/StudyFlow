package com.example.studyflow.util

import android.content.Context
import android.net.Uri
import android.util.Log
import java.io.File
import java.io.FileOutputStream

fun getFileFromUri(context: Context, uri: Uri): File? {
    return try {
        Log.d("FileUtil", "Attempting to get file from uri: $uri")
        val inputStream = context.contentResolver.openInputStream(uri)
        if (inputStream == null) {
            Log.e("FileUtil", "InputStream is null for uri: $uri")
            return null
        }
        // Dapatkan mime type dari URI
        val mimeType = context.contentResolver.getType(uri)
        Log.d("FileUtil", "MimeType for uri: $mimeType")
        // Tentukan ekstensi file berdasarkan mime type
        val extension = when (mimeType) {
            "image/jpeg" -> ".jpg"
            "image/png" -> ".png"
            "image/heic", "image/heif" -> ".heic"
            else -> ".jpg" // Default ke jpg jika mime type tidak terdeteksi
        }
        val tempFile = File(context.cacheDir, "temp_image_${System.currentTimeMillis()}$extension")
        val outputStream = FileOutputStream(tempFile)
        inputStream.copyTo(outputStream)
        outputStream.flush()
        outputStream.close()
        inputStream.close()
        Log.d("FileUtil", "File created at: ${tempFile.absolutePath}, size: ${tempFile.length()}")
        tempFile
    } catch (e: Exception) {
        Log.e("FileUtil", "Error in getFileFromUri: ${e.message}", e)
        null
    }
}