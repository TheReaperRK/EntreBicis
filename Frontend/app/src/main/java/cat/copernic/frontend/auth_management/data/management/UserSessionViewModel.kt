package cat.copernic.frontend.auth_management.data.management

import androidx.lifecycle.ViewModel
import cat.copernic.frontend.core.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserSessionViewModel : ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()

    fun setUser(user: User) {
        _user.value = user
    }

    fun clearUser() {
        _user.value = null
    }
}
