package cat.copernic.frontend.route_management.ui.components

import android.location.Location
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cat.copernic.frontend.core.ui.theme.PrimaryGreen
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.*


@Composable
fun MapaRuta(
    ubicacioActual: Location?,
    routePoints: List<LatLng>
) {
    val cameraPositionState = rememberCameraPositionState()

    // Centra el mapa si hay ubicación
    LaunchedEffect(ubicacioActual) {
        ubicacioActual?.let {
            val novaPos = LatLng(it.latitude, it.longitude)
            cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(novaPos, 18f))
        }
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        // Marcador actual
        ubicacioActual?.let {
            Marker(
                state = MarkerState(LatLng(it.latitude, it.longitude)),
                title = "La teva ubicació"
            )
        }

        // Polyline de la ruta (aunque esté finalizada)
        if (routePoints.size > 1) {
            Polyline(
                points = routePoints,
                color = PrimaryGreen,
                width = 5f
            )
        }

        // Marcadores para cada punto (opcional)
        routePoints.forEachIndexed { i, point ->
            Marker(
                state = MarkerState(point),
                title = "Punt ${i + 1}"
            )
        }
    }
}




