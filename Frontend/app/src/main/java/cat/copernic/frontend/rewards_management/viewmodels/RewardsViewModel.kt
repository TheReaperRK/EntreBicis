package cat.copernic.frontend.rewards_management.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.copernic.frontend.core.models.Reward
import cat.copernic.frontend.rewards_management.management.RewardRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel encarregat de gestionar la lògica de recompenses.
 *
 * Aquesta classe interactua amb el repositori [RewardRepo] per obtenir i modificar
 * les recompenses disponibles i les de l'usuari. També gestiona l'estat de càrrega
 * i la recompensa seleccionada.
 *
 * @property repo Repositori encarregat de les operacions relacionades amb recompenses.
 */
class RewardsViewModel(private val repo: RewardRepo) : ViewModel() {

    /** Llista observable de totes les recompenses carregades */
    private val _rewards = MutableStateFlow<List<Reward>>(emptyList())
    val rewards: StateFlow<List<Reward>> = _rewards

    /** Recompensa seleccionada o carregada per ID */
    private val _reward = MutableStateFlow<Reward?>(null)
    val reward: StateFlow<Reward?> = _reward

    /** Estat observable que indica si s'estan carregant dades */
    private val _loading = MutableStateFlow(true)
    val loading: StateFlow<Boolean> = _loading

    /**
     * Carrega totes les recompenses disponibles des del repositori.
     * Actualitza l'estat de càrrega durant el procés.
     */
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

    /**
     * Carrega una recompensa concreta pel seu identificador.
     * @param id ID de la recompensa a cercar.
     */
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

    /**
     * Intenta sol·licitar una recompensa al backend.
     *
     * @param id ID de la recompensa a sol·licitar.
     * @param onResult Callback que retorna `null` si és exitós o un missatge d'error si falla.
     */
    fun solicitarRecompensa(id: Long, onResult: (String?) -> Unit) {
        viewModelScope.launch {
            try {
                val response = repo.solicitarRecompensa(id)
                if (response.isSuccessful) {
                    onResult(null) // Èxit
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

    /**
     * Intenta recollir una recompensa ja assignada.
     *
     * @param id ID de la recompensa a recollir.
     * @param onResult Callback que retorna `null` si és exitós o un missatge d'error si falla.
     */
    fun recollirRecompensa(id: Long, onResult: (String?) -> Unit) {
        viewModelScope.launch {
            try {
                val response = repo.recollirRecompensa(id)
                if (response.isSuccessful) {
                    onResult(null) // Èxit
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

    /**
     * Carrega totes les recompenses associades a un usuari concret.
     *
     * @param email Correu electrònic de l'usuari.
     */
    fun carregarRecompensesUsuari(email: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                _rewards.value = repo.getRewardsByUser(email)
            } catch (e: Exception) {
                _rewards.value = emptyList()
            }
            _loading.value = false
        }
    }
}

