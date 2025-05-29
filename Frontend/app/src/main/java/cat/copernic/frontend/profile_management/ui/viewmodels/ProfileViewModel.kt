package cat.copernic.frontend.profile_management.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.copernic.frontend.core.models.DTO.ProfileDTO
import cat.copernic.frontend.core.models.User
import cat.copernic.frontend.profile_management.management.UserRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel encarregat de gestionar la lògica relacionada amb el perfil de l'usuari.
 *
 * Aquesta classe s'encarrega de recuperar les dades del perfil des del repositori
 * i exposar-les com un StateFlow perquè la UI pugui reaccionar als canvis.
 *
 * @property repository Repositori que proporciona accés a les dades de l'usuari.
 */
class ProfileViewModel(private val repository: UserRepo) : ViewModel() {

    /** Estat observable que conté les dades del perfil de l'usuari carregat */
    private val _user = MutableStateFlow<ProfileDTO?>(null)
    val user: StateFlow<ProfileDTO?> = _user

    /**
     * Carrega les dades de perfil de l'usuari a partir del seu correu electrònic.
     * Si la resposta és exitosa, actualitza l'estat intern amb les dades rebudes.
     *
     * @param email Correu electrònic de l'usuari a cercar.
     */
    fun loadUserByEmail(email: String) {
        viewModelScope.launch {
            val response = repository.getUserByEmail(email)
            if (response.isSuccessful) {
                _user.value = response.body()
            }
        }
    }
}
