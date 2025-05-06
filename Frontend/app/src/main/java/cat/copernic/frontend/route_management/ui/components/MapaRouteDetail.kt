package cat.copernic.frontend.route_management.ui.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cat.copernic.frontend.core.models.DTO.RouteDTO
import cat.copernic.frontend.core.ui.theme.PrimaryGreen
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun MapaRouteDetall(ruta: RouteDTO, context: Context, navController: NavController) {
    val cameraPositionState = rememberCameraPositionState()
    val latLngList = ruta.gpsPoints.map { LatLng(it.latitud.toDouble(), it.longitud.toDouble()) }

    LaunchedEffect(Unit) {
        MapsInitializer.initialize(context)
        if (latLngList.isNotEmpty()) {
            cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(latLngList.first(), 17f))
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            if (latLngList.size > 1) {
                Polyline(
                    points = latLngList,
                    color = PrimaryGreen,
                    width = 6f
                )
            }

            // Iconos personalizados
            val circleIcon = remember {
                createCustomMarkerIconSafely(zoomLevel = cameraPositionState.position.zoom)
            }
            val startIcon = remember {
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
            }
            val endIcon = remember {
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
            }

            // Inicio
            if (latLngList.isNotEmpty()) {
                Marker(
                    state = MarkerState(position = latLngList.first()),
                    icon = startIcon,
                    title = "Inici"
                )
            }

            // Fin
            if (latLngList.size > 1) {
                Marker(
                    state = MarkerState(position = latLngList.last()),
                    icon = endIcon,
                    title = "Final"
                )
            }

            // Puntos intermedios
            if (latLngList.size > 2) {
                latLngList.subList(1, latLngList.lastIndex).forEach { point ->
                    Marker(
                        state = MarkerState(position = point),
                        icon = circleIcon,
                        title = null
                    )
                }
            }
        }

        // Botón de retroceso
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
                .background(Color.White.copy(alpha = 0.9f), shape = CircleShape)
                .size(42.dp)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Tornar enrere", tint = Color.Black)
        }

        // Isla inferior de resumen
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp, vertical = 24.dp)
                .background(Color.White.copy(alpha = 0.98f), shape = RoundedCornerShape(24.dp))
                .padding(vertical = 20.dp, horizontal = 24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Distància recorreguda",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(
                    text = "${String.format("%.2f", ruta.distance)} km",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Duració total: ${ruta.totalTime}",
                    fontSize = 16.sp,
                    color = Color.DarkGray
                )
                Text(
                    text = "Velocitat mitjana: ${String.format("%.2f", ruta.averageSpeed)} km/h",
                    fontSize = 16.sp,
                    color = Color.DarkGray
                )
            }
        }
    }
}



/**
 * Crea un marcador circular verde con borde blanco.
 */
fun createCustomMarkerIconSafely(zoomLevel: Float): BitmapDescriptor {
    val baseSize = 60f  // <-- Más grande
    val size = (baseSize * (zoomLevel / 15f).coerceIn(0.6f, 1.5f)).toInt()

    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    val borderPaint = Paint().apply {
        color = android.graphics.Color.WHITE
        isAntiAlias = true
    }

    val fillPaint = Paint().apply {
        color = android.graphics.Color.parseColor("#FF27C08A") // PrimaryGreen
        isAntiAlias = true
    }

    canvas.drawCircle(size / 2f, size / 2f, size / 2f, borderPaint)
    canvas.drawCircle(size / 2f, size / 2f, size / 2.5f, fillPaint)

    return try {
        BitmapDescriptorFactory.fromBitmap(bitmap)
    } catch (e: Exception) {
        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
    }
}


