package cat.copernic.frontend.route_management.ui.components

import android.location.Location
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.Polyline

@Composable
fun MapaRuta(location: Location?, routePoints: List<LatLng>) {
    val cameraPositionState = rememberCameraPositionState()

    // Mueve la cámara cuando se actualiza la ubicación
    LaunchedEffect(location) {
        location?.let {
            val update = CameraUpdateFactory.newLatLngZoom(
                LatLng(it.latitude, it.longitude), 15f
            )
            cameraPositionState.move(update) // o animate(update) si prefieres animado
        }
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        location?.let {
            Marker(
                state = MarkerState(position = LatLng(it.latitude, it.longitude)),
                title = "Ubicació actual"
            )
        }
        // Dibujar la línea de la ruta
        if (routePoints.size >= 2) {
            Polyline(points = routePoints)
        }
    }
}
