package com.example.dictionary

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.firstOrNull

class PasswordStorage(private val context: Context) {
    private val Context.dataStore by preferencesDataStore(name = "password")
    private val key = stringPreferencesKey("pin_code_key")

    suspend fun savePin(code: String) {
        try {
            context.dataStore.edit { pref -> pref[key] = code }
        } catch (e: Exception) {
            Log.e("PasswordStorage", "Error saving PIN: ${e.message}")
        }
    }

    suspend fun getPin(): String? {
        return context.dataStore.data.firstOrNull()?.get(key)
    }

    suspend fun clearPin() {
        context.dataStore.edit { pref -> pref.remove(key) }
    }
}