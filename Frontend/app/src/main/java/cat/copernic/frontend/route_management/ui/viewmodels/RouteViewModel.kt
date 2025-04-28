package cat.copernic.frontend.route_management.ui.viewmodels

import android.app.Application
import android.content.Context
import android.location.Location
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cat.copernic.frontend.core.models.DTO.GpsPointDTO
import cat.copernic.frontend.core.models.DTO.RouteDTO
import cat.copernic.frontend.core.models.GpsPoint
import cat.copernic.frontend.core.models.Route
import cat.copernic.frontend.route_management.management.RouteRepository
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.LocalDateTime.now
import java.time.format.DateTimeFormatter

class RouteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = RouteRepository()

    private val _routePoints = MutableStateFlow<List<LatLng>>(emptyList())
    val routePoints: StateFlow<List<LatLng>> = _routePoints

    private val _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> get() = _isRecording

    private var locationPoints = mutableListOf<Location>()
    private var startTimestamp: Long = 0

    private val _routeFinished = MutableStateFlow(false)
    val routeFinished: StateFlow<Boolean> = _routeFinished

    private val _ubicacioActual = MutableStateFlow<Location?>(null)
    val ubicacioActual: StateFlow<Location?> = _ubicacioActual

    private val _currentLocation = MutableStateFlow<Location?>(null)
    val currentLocation: StateFlow<Location?> get() = _currentLocation

    private var currentRoute: Route = Route()
    private var totalTimeString: String = "00:00:00"

    init {
        // Limpieza al iniciar ViewModel por seguridad
        locationPoints.clear()
    }

    /**
     * Inicia la grabación de una ruta.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun startRoute(userEmail: String) {
        _isRecording.value = true
        startTimestamp = System.currentTimeMillis()

        //locationPoints.clear() // Limpieza por si quedó algo anterior
        //_routePoints.value = emptyList() // ← LIMPIA la línea anterior

        currentRoute = Route().apply {
            startDate = LocalDateTime.now().toString()
            user = repository.getDummyUser(userEmail)
        }
    }

    /**
     * Agrega una nueva ubicación real.
     */
    fun addLocation(location: Location) {
        locationPoints.add(location)
        _currentLocation.value = location

        if (_isRecording.value) {
            val updatedList = _routePoints.value.toMutableList()
            updatedList.add(LatLng(location.latitude, location.longitude))
            _routePoints.value = updatedList
        }
    }

    /**
     * Finaliza la ruta y calcula los valores.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun stopRoute() {
        _routeFinished.value = true
        _isRecording.value = false
        val endTimestamp = System.currentTimeMillis()

        val totalTimeSec = (endTimestamp - startTimestamp) / 1000.0
        totalTimeString = String.format(
            "%02d:%02d:%02d",
            (totalTimeSec / 3600).toInt(),
            ((totalTimeSec % 3600) / 60).toInt(),
            (totalTimeSec % 60).toInt()
        )

        val totalDistanceMeters = locationPoints.zipWithNext()
            .sumOf { (a, b) -> a.distanceTo(b).toDouble() }
        val totalDistanceKm = totalDistanceMeters / 1000.0
        val averageSpeed = if (totalTimeSec > 0) totalDistanceKm / (totalTimeSec / 3600.0) else 0.0

        currentRoute.apply {
            end_date = LocalDateTime.now().toString()
            distance = totalDistanceKm
            average_speed = averageSpeed
            generated_balance = totalDistanceKm.toInt()
        }
    }

    /**
     * Envía la ruta finalizada y los puntos GPS al backend.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun sendRoute(context: Context, onResult: (Boolean, Route?) -> Unit) {
        viewModelScope.launch {
            val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

            val gpsPoints = locationPoints.mapIndexed { index, location ->
                GpsPointDTO(
                    idRouteDTO = null, // aún no tienes el ID, se asignará en el backend
                    idPunt = (index + 1).toLong(), // empieza en 1
                    latitud = BigDecimal(location.latitude),
                    longitud = BigDecimal(location.longitude),
                    timestamp = LocalDateTime.now().format(formatter)
                )
            }

            val routeDTO = RouteDTO(
                idRouteDTO = null,
                startDate = currentRoute.startDate.format(formatter),
                endDate = currentRoute.end_date.format(formatter),
                distance = currentRoute.distance,
                averageSpeed = currentRoute.average_speed,
                totalTime = totalTimeString,
                generatedBalance = currentRoute.generated_balance,
                userEmail = currentRoute.user.mail,
                gpsPoints = gpsPoints
            )

            repository.sendRoute(routeDTO, context) { success, routeDTO ->
                println("view model: " + success)
                onResult(success, routeDTO)
                /*if (success && routeDTO != null) {
                    viewModelScope.launch {
                        val gpsPointsFromBackend = repository.getGpsPointsByRoute(routeDTO, context)
                        _routePoints.value = gpsPointsFromBackend.map {
                            LatLng(it.latitud.toDouble(), it.longitud.toDouble())
                        }
                    }
                }
                onResult(success, routeDTO)

                 */
            }

            _isRecording.value = false
        }
    }

    var locationCallback: LocationCallback? = null
    var fusedClient: FusedLocationProviderClient? = null

    fun stopLocationUpdates() {
        fusedClient?.let { client ->
            locationCallback?.let { callback ->
                client.removeLocationUpdates(callback)
                Log.d("ROUTE", "Localització aturada manualment")
            }
        }
    }

    fun obtenirUbicacioActual(context: Context) {
        if (fusedClient == null) {
            fusedClient = LocationServices.getFusedLocationProviderClient(context)
        }

        val locationRequest = CurrentLocationRequest.Builder()
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .build()

        val cancellationToken = CancellationTokenSource()

        fusedClient?.getCurrentLocation(locationRequest, cancellationToken.token)
            ?.addOnSuccessListener { location ->
                if (location != null) {
                    _currentLocation.value = location
                    Log.d("ROUTE", "Ubicació inicial obtinguda: ${location.latitude}, ${location.longitude}")
                } else {
                    Log.w("ROUTE", "Ubicació inicial nul·la")
                }
            }
            ?.addOnFailureListener {
                Log.e("ROUTE", "Error obtenint ubicació inicial", it)
            }
    }


    fun onLocationResult(result: LocationResult) {
        result.lastLocation?.let {
            _ubicacioActual.value = it

            if (isRecording.value) {
                addLocation(it) // solo guarda si está grabando
            }
        }
    }

    fun resetRoute() {
        locationPoints.clear()
        _routePoints.value = emptyList()
        _isRecording.value = false
        _routeFinished.value = false
        currentRoute = Route()
        totalTimeString = "00:00:00"
    }

}
