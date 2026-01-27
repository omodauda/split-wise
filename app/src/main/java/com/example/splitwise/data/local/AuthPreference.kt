package com.example.splitwise.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface IAuthPreference {
    val isAuthenticated: Flow<Boolean>
    suspend fun setAuthenticated(value: Boolean)
}

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")

class AuthPreference(context: Context): IAuthPreference {
    private val dataStore = context.dataStore

    private object PreferencesKeys {
        val IS_AUTHENTICATED = booleanPreferencesKey("is_authenticated")
    }
    override val isAuthenticated: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.IS_AUTHENTICATED] ?: false
    }

    override suspend fun setAuthenticated(value: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_AUTHENTICATED] = value
        }
    }
}