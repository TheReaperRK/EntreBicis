package cat.copernic.frontend.route_management.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cat.copernic.frontend.auth_management.data.management.UserSessionViewModel
import cat.copernic.frontend.core.viewmodels.UiVisibilityViewModel
import cat.copernic.frontend.navigation.Screens
import cat.copernic.frontend.route_management.ui.components.MapaRuta
import cat.copernic.frontend.route_management.ui.viewmodels.RouteViewModel
import com.google.android.gms.location.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StartRouteScreen(
    navController: NavController,
    sessionViewModel: UserSessionViewModel,
    uiVisibilityViewModel: UiVisibilityViewModel
) {
    val context = LocalContext.current
    val routeViewModel: RouteViewModel = viewModel()
    val fusedClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val user by sessionViewModel.user.collectAsState()
    val location by routeViewModel.currentLocation.collectAsState()
    val routePoints by routeViewModel.routePoints.collectAsState()
    val ubicacioActual by routeViewModel.ubicacioActual.collectAsState()

    val routeFinished by routeViewModel.routeFinished.collectAsState()
    var isRecording by remember { mutableStateOf(false) }
    var startTime by remember { mutableStateOf(0L) }
    var timerText by remember { mutableStateOf("00:00") }
    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED
        )
    }
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    var timerJob by remember { mutableStateOf<Job?>(null) }

    LaunchedEffect(Unit) {
        sessionViewModel.refreshUserData(context)
        if (hasPermission) {
            routeViewModel.obtenirUbicacioActual(context)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted
        if (granted) {
            routeViewModel.obtenirUbicacioActual(context)
        } else {
            Log.e("ROUTE", "Permís rebutjat per l'usuari")
        }
    }

    fun onStartRoutePressed() {
        if (!hasPermission) {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            user?.let { routeViewModel.startRoute(it.mail) }
            isRecording = true
            uiVisibilityViewModel.hideBar() // 👈 OCULTAR barra al iniciar
        }
    }

    fun resetScreen() {
        navController.navigate(Screens.Route.route)
    }

    LaunchedEffect(isRecording) {
        if (isRecording) {
            startTime = System.currentTimeMillis()
            timerJob = coroutineScope.launch {
                while (true) {
                    delay(1000)
                    val elapsed = System.currentTimeMillis() - startTime
                    val minutes = (elapsed / 1000) / 60
                    val seconds = (elapsed / 1000) % 60
                    timerText = "%02d:%02d".format(minutes, seconds)
                }
            }
        } else {
            timerJob?.cancel()
            uiVisibilityViewModel.showBar() // 👈 MOSTRAR barra al parar
        }
    }

    if (hasPermission) {
        val callback = remember {
            object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    routeViewModel.onLocationResult(result)
                }
            }
        }

        DisposableEffect(Unit) {
            routeViewModel.fusedClient = fusedClient
            routeViewModel.locationCallback = callback
            fusedClient.requestLocationUpdates(
                LocationRequest.create().apply {
                    interval = 2000
                    fastestInterval = 1000
                    priority = Priority.PRIORITY_HIGH_ACCURACY
                },
                callback,
                Looper.getMainLooper()
            )

            onDispose {
                routeViewModel.stopLocationUpdates()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        MapaRuta(
            ubicacioActual = ubicacioActual,
            routePoints = routePoints
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp)
                .background(color = Color.White, shape = RoundedCornerShape(24.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (!routeFinished) "Ruta en curs" else "Ruta finalitzada",
                style = MaterialTheme.typography.labelLarge,
                color = Color.Gray
            )
            Text(timerText, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            if (!routeFinished) {
                Button(
                    onClick = {
                        if (!isRecording) {
                            onStartRoutePressed()
                        } else {
                            routeViewModel.stopRoute()
                            user?.let { _ ->
                                routeViewModel.sendRoute(context) { success, _ ->
                                    snackbarMessage = if (success) {
                                        "Ruta enviada correctament"
                                    } else {
                                        "Error en enviar la ruta"
                                    }
                                    showSnackbar = true
                                    uiVisibilityViewModel.showBar() // 👈 volver a mostrar barra tras envío
                                }
                            }
                            isRecording = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF27C08A)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (isRecording) "Finalitzar Ruta" else "Iniciar Ruta",
                        color = Color.White
                    )
                }
            } else {
                Button(
                    onClick = {
                        uiVisibilityViewModel.showBar() // 👈 aseguramos que se muestra
                        resetScreen()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF27C08A)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Continuar",
                        color = Color.White
                    )
                }
            }

            if (showSnackbar) {
                Spacer(modifier = Modifier.height(12.dp))
                Snackbar(containerColor = Color.Gray) {
                    Text(text = snackbarMessage)
                }
            }
        }
    }
}
