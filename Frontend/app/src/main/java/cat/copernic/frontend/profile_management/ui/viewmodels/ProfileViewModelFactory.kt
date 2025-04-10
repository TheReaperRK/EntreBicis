package cat.copernic.frontend.profile_management.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cat.copernic.frontend.profile_management.management.UserRepo

class ProfileViewModelFactory(
    private val userRepo: UserRepo
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(userRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}