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

class RouteDetailViewModel : ViewModel() {
    private val _ruta = MutableStateFlow<RouteDTO?>(null)
    val ruta: StateFlow<RouteDTO?> = _ruta

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