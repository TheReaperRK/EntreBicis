package cat.copernic.frontend.route_management.management

import android.content.Context
import cat.copernic.frontend.core.models.DTO.RouteDTO
import cat.copernic.frontend.core.models.GpsPoint
import cat.copernic.frontend.core.models.Route
import cat.copernic.frontend.core.models.User
import cat.copernic.frontend.route_management.network.RouteRetrofitInstance
import kotlinx.coroutines.delay

class RouteRepository {

    suspend fun sendRoute(routeDTO: RouteDTO, context: Context): Boolean {
        val api = RouteRetrofitInstance.getApi(context)
        val response = api.sendRoute(routeDTO)
        return response.isSuccessful
    }


    fun getDummyUser(email: String): User {
        val user = User()
        user.setMail(email)
        return user
    }
}