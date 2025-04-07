package cat.copernic.frontend.auth_management.data.management

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore: androidx.datastore.core.DataStore<Preferences> by preferencesDataStore(name = "user_session")

class SessionManager(private val context: Context) {
    companion object {
        val EMAIL = stringPreferencesKey("email")
        val SESSION_KEY = stringPreferencesKey("session_key")
    }

    suspend fun saveSession(email: String, sessionKey: String) {
        context.dataStore.edit { prefs ->
            prefs[EMAIL] = email
            prefs[SESSION_KEY] = sessionKey
        }
    }

    val email: Flow<String?> = context.dataStore.data.map { it[EMAIL] }
    val sessionKey: Flow<String?> = context.dataStore.data.map { it[SESSION_KEY] }

    suspend fun clearSession() {
        context.dataStore.edit { it.clear() }
    }

    suspend fun getEmail(): String? {
        return context.dataStore.data.first()[EMAIL]
    }
}