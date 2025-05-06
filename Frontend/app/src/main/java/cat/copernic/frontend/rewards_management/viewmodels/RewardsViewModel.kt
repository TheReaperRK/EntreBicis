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

    private val _reward = MutableStateFlow<Reward?>(null)
    val reward: StateFlow<Reward?> = _reward

    private val _loading = MutableStateFlow(true)
    val loading: StateFlow<Boolean> = _loading

    fun loadRewards() {
        viewModelScope.launch {
            _loading.value = true
            try {
                _rewards.value = repo.fetchRewards()
            } catch (e: Exception) {

                _rewards.value = emptyList()
            }
            _loading.value = false
        }
    }

    fun loadRewardById(id: Long) {
        viewModelScope.launch {
            try {
                val result = repo.getRewardById(id)
                _reward.value = result
            } catch (e: Exception) {
                _reward.value = null
            }
        }
    }

    fun solicitarRecompensa(id: Long, onResult: (String?) -> Unit) {
        viewModelScope.launch {
            try {
                val response = repo.solicitarRecompensa(id)
                if (response.isSuccessful) {
                    onResult(null) // Éxito
                } else {
                    val errorBody = response.errorBody()?.string()
                    val message = errorBody ?: "Error desconegut"
                    onResult(message)
                }
            } catch (e: Exception) {
                onResult(e.message ?: "Error inesperat")
            }
        }
    }

    fun recollirRecompensa(id: Long, onResult: (String?) -> Unit) {
        viewModelScope.launch {
            try {
                val response = repo.recollirRecompensa(id)
                if (response.isSuccessful) {
                    onResult(null) // Éxito
                } else {
                    val errorBody = response.errorBody()?.string()
                    val message = errorBody ?: "Error desconegut"
                    onResult(message)
                }
            } catch (e: Exception) {
                onResult(e.message ?: "Error inesperat")
            }
        }
    }
}
