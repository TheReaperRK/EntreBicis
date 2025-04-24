package cat.copernic.frontend.route_management.ui.components

import android.location.Location
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cat.copernic.frontend.core.ui.theme.PrimaryGreen
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun FinalRouteScreen(routePoints: List<LatLng>, ubicacioFinal: Location?) {
    val cameraPositionState = rememberCameraPositionState()

    LaunchedEffect(ubicacioFinal) {
        ubicacioFinal?.let {
            cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude), 17f))
        }
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        if (routePoints.isNotEmpty()) {
            Polyline(
                points = routePoints,
                color = PrimaryGreen,
                width = 5f
            )

            routePoints.forEachIndexed { index, point ->
                Marker(
                    state = MarkerState(position = point),
                    title = "Punt ${index + 1}"
                )
            }
        }

        ubicacioFinal?.let {
            Marker(
                state = MarkerState(position = LatLng(it.latitude, it.longitude)),
                title = "Final"
            )
        }
    }
}
