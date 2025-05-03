package cat.copernic.frontend.route_management.api

import cat.copernic.frontend.core.models.DTO.GpsPointDTO
import cat.copernic.frontend.core.models.DTO.RouteDTO
import cat.copernic.frontend.core.models.Route
import cat.copernic.frontend.core.models.GpsPoint
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RouteApiRest {

    @POST("/api/routes/send")
    suspend fun sendRoute(@Body route: RouteDTO): Response<Route>

    @POST("/api/routes/points")
    suspend fun sendPoints(@Body points: List<GpsPoint>): Response<Void>

    @POST("/api/routes/pointsByRoute")
    suspend fun getGpsPointsByRoute(@Body route: Route): Response<List<GpsPointDTO>>
}
