package com.example.studyflow.data.local

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.firstOrNull

private val Context.dataStore by preferencesDataStore("user_prefs")

class UserPreferences(private val context: Context) {

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("token_key")
        private val ROLE_KEY = stringPreferencesKey("role_key")
    }

    //Flow untuk memantau perubahan token yang tersimpan.
    val getToken: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[TOKEN_KEY]
    }

    //Flow untuk memantau role yang tersimpan.
    val getRole: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[ROLE_KEY]
    }

    //Menyimpan token dan role ke DataStore.
    suspend fun saveTokenAndRole(token: String, role: String) {
        context.dataStore.edit { prefs ->
            prefs[TOKEN_KEY] = token
            prefs[ROLE_KEY] = role
        }
    }

    //Menghapus token dan role dari DataStore (misalnya saat logout).
    suspend fun clear() {
        context.dataStore.edit { prefs ->
            prefs.remove(TOKEN_KEY)
            prefs.remove(ROLE_KEY)
        }
    }

    /**
     * Fungsi helper untuk mengambil token secara langsung (bukan Flow).
     * Kadang berguna untuk inisialisasi cepat.
     */
    suspend fun fetchToken(): String? {
        return getToken.firstOrNull()
    }

    suspend fun fetchRole(): String? {
        return getRole.firstOrNull()
    }
}