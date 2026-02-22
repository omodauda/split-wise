package com.example.splitwise.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.splitwise.data.network.model.AuthUserData
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

interface IAuthPreference {
    val isAuthenticated: Flow<Boolean>
    suspend fun setAuthenticated(value: Boolean)
    suspend fun saveAccessToken(token: String)
    fun getAccessToken(): Flow<String?>
    fun getAccessTokenSync(): String?
    suspend fun saveUser(user: AuthUserData)
    fun getUser(): Flow<AuthUserData?>
    suspend fun clearAll()
}

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

class AuthPreference(context: Context): IAuthPreference {
    // Datastore - insensitive data
    private val dataStore = context.dataStore

    // Encrypted shared preference - sensitive data
    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    private val encryptedPrefs: SharedPreferences = EncryptedSharedPreferences.create(
        "secure_prefs",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private object PreferencesKeys {
        val IS_AUTHENTICATED = booleanPreferencesKey("is_authenticated")
//        val ACCESS_TOKEN = stringPreferencesKey("access_token")
//        val USER_DATA = stringPreferencesKey("user_data")
    }

//    override suspend fun saveAccessToken(token: String) {
//        dataStore.edit { preferences ->
//            preferences[PreferencesKeys.ACCESS_TOKEN] = token
//        }
//    }

    override suspend fun saveAccessToken(token: String) = withContext(Dispatchers.IO) {
        encryptedPrefs.edit().putString("access_token", token).apply()
    }

//    override fun getAccessToken(): Flow<String?> {
//        return dataStore.data.map { preferences ->
//            preferences[PreferencesKeys.ACCESS_TOKEN]
//
//        }
//    }
    override fun getAccessTokenSync(): String? {
        return encryptedPrefs.getString("access_token", null)
    }

    override fun getAccessToken(): Flow<String?> {
        // Return a simple flow for consistency. The interceptor should use getAccessTokenSync().
        return flowOf(getAccessTokenSync())
    }

//    override suspend fun saveUser(user: AuthUserData) {
//        val userJson = Gson().toJson(user)
//        dataStore.edit { preferences ->
//            preferences[PreferencesKeys.USER_DATA] = userJson
//        }
//    }

    override suspend fun saveUser(user: AuthUserData) = withContext(Dispatchers.IO) {
        val userJson = Gson().toJson(user)
        encryptedPrefs.edit().putString("user_data_json", userJson).apply()
    }

//    override fun getUser(): Flow<AuthUserData?> {
//        return dataStore.data.map { preferences ->
//            val userJson = preferences[PreferencesKeys.USER_DATA]
//            if (userJson !== null) {
//                Gson().fromJson(userJson, AuthUserData::class.java)
//            } else {
//                null
//            }
//        }
//    }

    override fun getUser(): Flow<AuthUserData?> {
        val userJson = encryptedPrefs.getString("user_data_json", null)
        val user = if (userJson != null) {
            try {
                Gson().fromJson(userJson, AuthUserData::class.java)
            } catch (e: Exception) {
                null // Handle potential JSON parsing errors
            }
        } else {
            null
        }
        return flowOf(user)
    }


    override val isAuthenticated: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.IS_AUTHENTICATED] ?: false
    }

    override suspend fun setAuthenticated(value: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_AUTHENTICATED] = value
        }
    }

    override suspend fun clearAll() {
        dataStore.edit { it.clear() }
        // Clear EncryptedSharedPreferences
        withContext(Dispatchers.IO) {
            encryptedPrefs.edit().clear().apply()
        }
    }
}