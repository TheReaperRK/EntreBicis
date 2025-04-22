package cat.copernic.frontend.route_management.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import cat.copernic.frontend.route_management.ui.viewmodels.RouteViewModel
import com.google.android.gms.location.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StartRouteScreen(navController: NavController, routeViewModel: RouteViewModel = viewModel(), sessionViewModel: UserSessionViewModel) {
    val context = LocalContext.current
    val fusedClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var isRecording by remember { mutableStateOf(false) }
    var startTime by remember { mutableStateOf(0L) }
    var timerText by remember { mutableStateOf("00:00") }
    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    var timerJob by remember { mutableStateOf<Job?>(null) }

    val user by sessionViewModel.user.collectAsState()


    LaunchedEffect(true) {
        sessionViewModel.refreshUserData(context)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted
        if (!granted) {
            Log.e("ROUTE", "Permís rebutjat per l'usuari")
            println("PErmis rebutjat")
        }
    }

    fun onStartRoutePressed() {
        if (!hasPermission) {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            user?.let { routeViewModel.startRoute(it.mail) }
            isRecording = true
        }
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
        }
    }

    if (isRecording && hasPermission) {
        val callback = remember {
            object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    result.lastLocation?.let {
                        routeViewModel.addLocation(it)
                        Log.d("ROUTE", "Punt capturat: ${it.latitude}, ${it.longitude}")
                        println("Punt capturat: ${it.latitude}, ${it.longitude}")
                    }
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Ruta en curs", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Duració: $timerText")
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (!isRecording) {
                    onStartRoutePressed()
                } else {
                    isRecording = false
                    routeViewModel.stopLocationUpdates()
                    routeViewModel.stopRoute()
                    routeViewModel.sendRoute(context) { success ->
                        snackbarMessage = if (success) {
                            "Ruta enviada correctament"
                        } else {
                            "Error en enviar la ruta"
                        }
                        showSnackbar = true
                        navController.popBackStack()
                    }
                    println(snackbarMessage)
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF27C08A))
        ) {
            Text(if (isRecording) "Finalitzar Ruta" else "Iniciar Ruta", color = Color.White)
        }

        if (showSnackbar) {
            Snackbar(
                modifier = Modifier.padding(top = 16.dp),
                containerColor = Color.Gray
            ) {
                Text(text = snackbarMessage)
            }
        }
    }
}
