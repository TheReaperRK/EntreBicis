package cat.copernic.frontend.profile_management.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.copernic.frontend.core.models.DTO.ProfileDTO
import cat.copernic.frontend.core.models.User
import cat.copernic.frontend.profile_management.management.UserRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: UserRepo) : ViewModel() {

    private val _user = MutableStateFlow<ProfileDTO?>(null)
    val user: StateFlow<ProfileDTO?> = _user

    fun loadUserByEmail(email: String) {
        viewModelScope.launch {
            val response = repository.getUserByEmail(email)
            if (response.isSuccessful) {
                _user.value = response.body()
            }
        }
    }
}