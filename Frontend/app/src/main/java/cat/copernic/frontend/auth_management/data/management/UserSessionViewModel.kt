package cat.copernic.frontend.auth_management.data.management

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.copernic.frontend.core.models.User
import cat.copernic.frontend.profile_management.management.UserRetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserSessionViewModel : ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()

    private val _token = MutableStateFlow("")
    val token: StateFlow<String> = _token

    fun setUser(user: User) {
        _user.value = user
    }

    fun clearUser() {
        _user.value = null
    }

    fun setSession(user: User, token: String) {
        _user.value = user
        _token.value = token
    }

    var isSessionRestored by mutableStateOf(false)
        private set

    fun restoreSession(context: Context) {
        val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val token = prefs.getString("jwt_token", null)
        val email = prefs.getString("user_email", null)

        Log.d("RESTORE", "Token en prefs: $token, email en prefs: $email")

        if (!isSessionRestored) {

            if (!token.isNullOrBlank() && !email.isNullOrBlank()) {
                _token.value = token
                Log.d("RESTORE", "Lanzamos getUserByEmail al backend con email=$email")

                viewModelScope.launch {
                    try {
                        val api = UserRetrofitInstance.getApi(context) // Usa cliente con token
                        val response = api.getUserByEmail(email)
                        Log.d("RESTORE", "Respuesta code: ${response.code()}, isSuccessful=${response.isSuccessful}")

                        if (response.isSuccessful) {
                            val user = response.body()
                            if (user != null) {
                                _user.value = user
                                Log.d("RESTORE", "Usuario recibido: $user")
                            } else {
                                Log.d("RESTORE", "response.body() es nulo")
                            }
                            isSessionRestored = true
                        } else {
                            Log.d("RESTORE", "La respuesta no es success. Code=${response.code()}")
                        }
                    } catch (e: Exception) {
                        Log.e("RESTORE", "Excepción en getUserByEmail", e)
                    }
                }
            } else {
                Log.d("RESTORE", "No había token o email en prefs")
            }
        } else {
            Log.d("RESTORE", "isSessionRestored ya estaba en true")
        }
    }


    fun clearSession(context: Context) {
        // Borra el token del almacenamiento local
        val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        prefs.edit().remove("jwt_token").apply()

        // Borra el usuario y el token de la sesión actual
        _user.value = null
        _token.value = ""
    }

}
