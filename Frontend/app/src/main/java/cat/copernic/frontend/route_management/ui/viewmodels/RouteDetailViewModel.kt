package cat.copernic.frontend.route_management.ui.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.copernic.frontend.core.models.DTO.RouteDTO
import cat.copernic.frontend.route_management.network.RouteRetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel encarregat de carregar els detalls d'una ruta específica.
 *
 * Aquesta classe s'encarrega de recuperar una ruta concreta des del backend a través
 * del seu identificador i exposar-la com a [StateFlow] perquè la UI la pugui observar.
 */
class RouteDetailViewModel : ViewModel() {

    /** Estat observable que conté la ruta carregada en format [RouteDTO] */
    private val _ruta = MutableStateFlow<RouteDTO?>(null)
    val ruta: StateFlow<RouteDTO?> = _ruta

    /**
     * Carrega una ruta des del backend mitjançant el seu ID.
     *
     * @param id Identificador únic de la ruta a carregar.
     * @param context Context necessari per obtenir una instància de l'API configurada amb token.
     */
    fun carregarRuta(id: Long, context: Context) {
        val api = RouteRetrofitInstance.getApi(context)

        viewModelScope.launch {
            try {
                val response = api.getRutaById(id)
                _ruta.value = response
            } catch (e: Exception) {
                Log.e("RUTA", "Error carregant ruta", e)
            }
        }
    }
}
