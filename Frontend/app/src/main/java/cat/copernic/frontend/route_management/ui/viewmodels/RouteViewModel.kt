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
import cat.copernic.frontend.core.models.DTO.RouteDtoClear
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

/**
 * ViewModel encarregat de gestionar la lògica de gravació, finalització i enviament de rutes GPS.
 *
 * Controla l'estat d'una ruta activa, calcula distància, temps i velocitat, i envia les dades al backend.
 * També proporciona accés a les ubicacions actuals, actualitzacions de localització i historial de rutes.
 *
 * @constructor Crea una instància de RouteViewModel amb accés a l'Application.
 */
class RouteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = RouteRepository()

    /** Llista de rutes de l'usuari autenticat */
    private val _userRoutes = MutableStateFlow<List<RouteDtoClear>>(emptyList())
    val userRoutes: StateFlow<List<RouteDtoClear>> get() = _userRoutes

    /** Llista observable dels punts GPS en format LatLng per visualitzar la ruta */
    private val _routePoints = MutableStateFlow<List<LatLng>>(emptyList())
    val routePoints: StateFlow<List<LatLng>> = _routePoints

    /** Estat que indica si la gravació d'una ruta està activa */
    private val _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> get() = _isRecording

    /** Llista interna de punts de localització recollits (amb altitud i temps) */
    private var locationPoints = mutableListOf<Location>()

    /** Timestamp en mil·lisegons del moment d'inici de la ruta */
    private var startTimestamp: Long = 0

    /** Estat que indica si la ruta ha estat finalitzada */
    private val _routeFinished = MutableStateFlow(false)
    val routeFinished: StateFlow<Boolean> = _routeFinished

    /** Última ubicació obtinguda per Google Location API */
    private val _ubicacioActual = MutableStateFlow<Location?>(null)
    val ubicacioActual: StateFlow<Location?> = _ubicacioActual

    /** Ubicació actual del dispositiu per mostrar en la UI */
    private val _currentLocation = MutableStateFlow<Location?>(null)
    val currentLocation: StateFlow<Location?> get() = _currentLocation

    /** Ruta actual en procés de gravació */
    private var currentRoute: Route = Route()

    /** Temps total de la ruta en format HH:mm:ss */
    private var totalTimeString: String = "00:00:00"

    init {
        // Neteja inicial de punts de localització
        locationPoints.clear()
    }

    /**
     * Inicia una nova ruta associada a l'usuari.
     *
     * @param userEmail Correu electrònic de l'usuari per associar la ruta.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun startRoute(userEmail: String) {
        _isRecording.value = true
        startTimestamp = System.currentTimeMillis()

        currentRoute = Route().apply {
            startDate = LocalDateTime.now().toString()
            user = repository.getDummyUser(userEmail)
        }
    }

    /**
     * Afegeix una ubicació capturada a la ruta actual.
     *
     * @param location Objecte [Location] amb latitud, longitud i hora.
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
     * Finalitza la ruta actual, calcula el temps total, distància i velocitat mitjana.
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
     * Construeix i envia la ruta finalitzada amb els punts GPS al backend.
     *
     * @param context Context per accedir a l'API amb token.
     * @param onResult Callback que rep un booleà d'èxit i la ruta retornada.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun sendRoute(context: Context, onResult: (Boolean, Route?) -> Unit) {
        viewModelScope.launch {
            val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

            val gpsPoints = locationPoints.mapIndexed { index, location ->
                val timestamp = java.time.Instant.ofEpochMilli(location.time)
                    .atZone(java.time.ZoneId.systemDefault())
                    .toLocalDateTime()
                    .format(formatter)

                GpsPointDTO(
                    idRouteDTO = null,
                    idPunt = (index + 1).toLong(),
                    latitud = BigDecimal(location.latitude),
                    longitud = BigDecimal(location.longitude),
                    timestamp = timestamp
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
                onResult(success, routeDTO)
            }

            _isRecording.value = false
        }
    }

    /**
     * Carrega totes les rutes de l'usuari actual des del repositori.
     *
     * @param context Context per accedir a l'API amb el token.
     */
    fun carregarRutesUsuari(context: Context) {
        viewModelScope.launch {
            val rutes = repository.getAllRoutesByUser(context)
            _userRoutes.value = rutes
        }
    }

    /** Callback i client de localització utilitzats per rebre actualitzacions */
    var locationCallback: LocationCallback? = null
    var fusedClient: FusedLocationProviderClient? = null

    /**
     * Atura manualment les actualitzacions de localització.
     */
    fun stopLocationUpdates() {
        fusedClient?.let { client ->
            locationCallback?.let { callback ->
                client.removeLocationUpdates(callback)
                Log.d("ROUTE", "Localització aturada manualment")
            }
        }
    }

    /**
     * Obté la ubicació actual un cop des del sistema de localització.
     *
     * @param context Context per accedir a LocationServices.
     */
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

    /**
     * S'executa quan s'obté una nova ubicació del sistema.
     *
     * @param result Objecte amb la nova localització.
     */
    fun onLocationResult(result: LocationResult) {
        result.lastLocation?.let {
            _ubicacioActual.value = it

            if (isRecording.value) {
                addLocation(it)
            }
        }
    }
}

