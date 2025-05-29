package cat.copernic.frontend.core.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UiVisibilityViewModel : ViewModel() {
    private val _showBottomBar = MutableStateFlow(true)
    val showBottomBar: StateFlow<Boolean> get() = _showBottomBar

    fun showBar() {
        _showBottomBar.value = true
    }

    fun hideBar() {
        _showBottomBar.value = false
    }
}