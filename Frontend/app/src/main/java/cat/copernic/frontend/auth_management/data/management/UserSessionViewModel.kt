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

/**
 * ViewModel encarregat de gestionar la sessió de l'usuari dins de l'aplicació.
 *
 * Gestiona l'estat de l'usuari autenticat, el token JWT, la restauració de sessió
 * i la recuperació de dades de perfil.
 */
class UserSessionViewModel : ViewModel() {

    /** Flow que conté la informació bàsica de l'usuari autenticat (UserDTO) */
    private val _userDTO = MutableStateFlow<UserDTO?>(null)
    val userDto = _userDTO.asStateFlow()

    /** Flow que conté informació detallada de perfil de l'usuari (ProfileDTO) */
    private val _user = MutableStateFlow<ProfileDTO?>(null)
    val user = _user.asStateFlow()

    /** Flow que emmagatzema el token JWT actiu */
    private val _token = MutableStateFlow("")
    val token: StateFlow<String> = _token

    /**
     * Assigna un usuari autenticat a la sessió actual.
     * @param user Usuari que s'ha de desar.
     */
    fun setUser(user: UserDTO) {
        _userDTO.value = user
    }

    /**
     * Esborra l'usuari actual de la sessió.
     */
    fun clearUser() {
        _userDTO.value = null
    }

    /**
     * Estableix l'usuari i el token de la sessió.
     * @param user Usuari autenticat.
     * @param token Token JWT associat a l'usuari.
     */
    fun setSession(user: UserDTO, token: String) {
        _userDTO.value = user
        _token.value = token
    }

    /** Flow que indica si la sessió s'ha restaurat (encara que sigui fallida) */
    private val _isSessionRestored = MutableStateFlow(false)
    val isSessionRestored = _isSessionRestored.asStateFlow()

    /**
     * Intenta restaurar la sessió llegint el token i email des de SharedPreferences.
     * Si existeixen, es recupera l'usuari del backend. Independentment del resultat,
     * marca la sessió com a restaurada.
     *
     * @param context Context de l'aplicació per accedir a SharedPreferences.
     */
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
                        val api = UserRetrofitInstance.getApi(context)
                        val response = api.getUserByEmailDTO(email)
                        Log.d("RESTORE", "Resposta code: ${response.code()}, isSuccessful=${response.isSuccessful}")

                        if (response.isSuccessful) {
                            val user = response.body()
                            if (user != null) {
                                _userDTO.value = user
                                Log.d("RESTORE", "Usuari rebut: $user")
                            } else {
                                Log.d("RESTORE", "response.body() és nul")
                            }
                        } else {
                            Log.d("RESTORE", "Resposta no exitosa. Code=${response.code()}")
                        }
                    } catch (e: Exception) {
                        Log.e("RESTORE", "Excepció en getUserByEmail", e)
                    } finally {
                        _isSessionRestored.value = true
                    }
                }
            } else {
                Log.d("RESTORE", "No hi havia token o email en prefs")
                _isSessionRestored.value = true
            }
        } else {
            Log.d("RESTORE", "isSessionRestored ja estava a true")
        }
    }

    /**
     * Marca manualment la sessió com a restaurada.
     */
    fun markSessionAsRestored() {
        _isSessionRestored.value = true
    }

    /**
     * Refresca les dades de perfil de l'usuari des del backend,
     * sempre que hi hagi un email desat a SharedPreferences.
     *
     * @param context Context de l'aplicació per obtenir SharedPreferences.
     */
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
                            Log.d("REFRESH", "Usuari actualitzat: $it")
                        }
                    } else {
                        Log.w("REFRESH", "No s'ha pogut refrescar l'usuari. Code=${response.code()}")
                    }
                } catch (e: Exception) {
                    Log.e("REFRESH", "Excepció al refrescar usuari", e)
                }
            }
        }
    }

    /**
     * Esborra completament la sessió de l'usuari, tant en memòria com al disc.
     * Elimina el token i reinicia els valors del ViewModel.
     *
     * @param context Context per accedir a SharedPreferences.
     */
    fun clearSession(context: Context) {
        val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        prefs.edit().remove("jwt_token").apply()

        _user.value = null
        _token.value = ""
    }
}

