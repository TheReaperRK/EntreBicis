package cat.copernic.frontend.route_management.management

import android.content.Context
import cat.copernic.frontend.core.models.DTO.GpsPointDTO
import cat.copernic.frontend.core.models.DTO.RouteDTO
import cat.copernic.frontend.core.models.GpsPoint
import cat.copernic.frontend.core.models.Route
import cat.copernic.frontend.core.models.User
import cat.copernic.frontend.route_management.network.RouteRetrofitInstance
import kotlinx.coroutines.delay
import retrofit2.Response

class RouteRepository {

    suspend fun sendRoute(route: RouteDTO, context: Context, onResult: (Boolean, Route?) -> Unit) {
        val api = RouteRetrofitInstance.getApi(context)
        try {
            val response = api.sendRoute(route)
            if (response.isSuccessful) {
                val routeNew = response.body()
                onResult(true, routeNew)
            } else {
                onResult(false, null)
            }
        } catch (e: Exception) {
            onResult(false, null)
        }
    }


    fun getDummyUser(email: String): User {
        val user = User()
        user.setMail(email)
        return user
    }

    suspend fun getGpsPointsByRouteId(routeId: Route, context: Context): List<GpsPointDTO> {
        val api = RouteRetrofitInstance.getApi(context)
        val response = api.getGpsPointsByRouteId(routeId)

        return if (response.isSuccessful && response.body() != null) {
            response.body()!!
        } else {
            emptyList() // o lanza excepción si quieres
        }
    }
}