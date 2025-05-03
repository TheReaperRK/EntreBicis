package cat.copernic.frontend.core.models.DTO

import java.math.BigDecimal
import java.sql.Time
import java.time.LocalDateTime

data class GpsPointDTO(
    val latitud: BigDecimal,
    val longitud: BigDecimal,
    val timestamp: String,
    val idPunt : Long,
    val idRouteDTO: RouteDTO? = null
)

data class RouteDTO(
    val idRouteDTO: Long? = null,
    val startDate: String,
    val endDate: String,
    val distance: Double,
    val averageSpeed: Double,
    val totalTime: String,
    val generatedBalance: Int,
    val userEmail: String,
    val gpsPoints: List<GpsPointDTO>
)