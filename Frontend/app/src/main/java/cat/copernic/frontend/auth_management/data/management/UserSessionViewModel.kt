package cat.copernic.frontend.auth_management.data.management

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.copernic.frontend.core.models.DTO.ProfileDTO
import cat.copernic.frontend.core.models.DTO.UserDTO
import cat.copernic.frontend.core.models.User
import cat.copernic.frontend.profile_management.management.UserRetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserSessionViewModel : ViewModel() {
    private val _userDTO = MutableStateFlow<UserDTO?>(null)
    val userDto = _userDTO.asStateFlow()

    private val _user = MutableStateFlow<ProfileDTO?>(null)
    val user = _user.asStateFlow()

    private val _token = MutableStateFlow("")
    val token: StateFlow<String> = _token

    fun setUser(user: UserDTO) {
        _userDTO.value = user
    }

    fun clearUser() {
        _userDTO.value = null
    }

    fun setSession(user: UserDTO, token: String) {
        _userDTO.value = user
        _token.value = token
    }

    private val _isSessionRestored = MutableStateFlow(false)
    val isSessionRestored = _isSessionRestored.asStateFlow()


    fun restoreSession(context: Context) {
        val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val token = prefs.getString("jwt_token", null)
        val email = prefs.getString("user_email", null)

        Log.d("RESTORE", "Token en prefs: $token, email en prefs: $email")

        if (_isSessionRestored.value.not()) {
            if (!token.isNullOrBlank() && !email.isNullOrBlank()) {
                _token.value = token
                Log.d("RESTORE", "Lanzamos getUserByEmail al backend con email=$email")

                viewModelScope.launch {
                    try {
                        val api = UserRetrofitInstance.getApi(context) // Usa cliente con token
                        val response = api.getUserByEmailDTO(email)
                        Log.d("RESTORE", "Respuesta code: ${response.code()}, isSuccessful=${response.isSuccessful}")

                        if (response.isSuccessful) {
                            val user = response.body()
                            if (user != null) {
                                _userDTO.value = user
                                Log.d("RESTORE", "Usuario recibido: $user")
                            } else {
                                Log.d("RESTORE", "response.body() es nulo")
                            }
                        } else {
                            Log.d("RESTORE", "Respuesta no exitosa. Code=${response.code()}")
                        }
                    } catch (e: Exception) {
                        Log.e("RESTORE", "Excepción en getUserByEmail", e)
                    } finally {
                        // ✅ SIEMPRE marcar como restaurada, incluso si falló
                        _isSessionRestored.value = true
                    }
                }
            } else {
                Log.d("RESTORE", "No había token o email en prefs")
                _isSessionRestored.value = true // ✅ Sin datos: restauración fallida, pero finalizada
            }
        } else {
            Log.d("RESTORE", "isSessionRestored ya estaba en true")
        }
    }

    fun markSessionAsRestored() {
        _isSessionRestored.value = true
    }

    fun refreshUserData(context: Context) {
        val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val email = prefs.getString("user_email", null)

        if (!email.isNullOrBlank()) {
            viewModelScope.launch {
                try {
                    val api = UserRetrofitInstance.getApi(context)
                    val response = api.getUserByEmail(email)

                    if (response.isSuccessful) {
                        response.body()?.let {
                            _user.value = it
                            Log.d("REFRESH", "Usuario actualizado: $it")
                        }
                    } else {
                        Log.w("REFRESH", "No se pudo refrescar el usuario. Code=${response.code()}")
                    }
                } catch (e: Exception) {
                    Log.e("REFRESH", "Excepción al refrescar usuario", e)
                }
            }
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
