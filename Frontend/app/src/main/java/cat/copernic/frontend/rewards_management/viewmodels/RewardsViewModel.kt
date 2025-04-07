package cat.copernic.frontend.rewards_management.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.copernic.frontend.core.models.Reward
import cat.copernic.frontend.rewards_management.management.RewardRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RewardsViewModel(private val repo: RewardRepo) : ViewModel() {

    private val _rewards = MutableStateFlow<List<Reward>>(emptyList())
    val rewards: StateFlow<List<Reward>> = _rewards

    private val _loading = MutableStateFlow(true)
    val loading: StateFlow<Boolean> = _loading

    init {
        loadRewards()
    }

    fun loadRewards() {
        viewModelScope.launch {
            _loading.value = true
            try {
                _rewards.value = repo.fetchRewards()
            } catch (e: Exception) {
                // Podrías notificar con un log o Toast si lo necesitas
                _rewards.value = emptyList()
            }
            _loading.value = false
        }
    }
}
