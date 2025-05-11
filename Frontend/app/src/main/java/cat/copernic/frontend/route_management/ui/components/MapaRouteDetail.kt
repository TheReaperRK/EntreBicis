import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cat.copernic.frontend.core.models.DTO.RouteDTO
import cat.copernic.frontend.core.ui.theme.PrimaryGreen
import cat.copernic.frontend.core.utils.calcularVelocitatsEntrePunts
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField

@RequiresApi(Build.VERSION_CODES.O)
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

        val velocitats = remember(ruta.gpsPoints) {
            calcularVelocitatsEntrePunts(ruta.gpsPoints)
        }

        val velocitatMaxima = velocitats.maxOfOrNull { it.second } ?: 0.0

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

            val circleIcon = remember {
                createCustomMarkerIconSafely(zoomLevel = cameraPositionState.position.zoom)
            }
            val startIcon = remember {
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
            }
            val endIcon = remember {
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
            }

            if (latLngList.isNotEmpty()) {
                Marker(
                    state = MarkerState(position = latLngList.first()),
                    icon = startIcon,
                    title = "Inici"
                )
            }

            if (latLngList.size > 1) {
                Marker(
                    state = MarkerState(position = latLngList.last()),
                    icon = endIcon,
                    title = "Final"
                )
            }

            ruta.gpsPoints.subList(1, ruta.gpsPoints.lastIndex).forEachIndexed { index, gps ->
                val point = LatLng(gps.latitud.toDouble(), gps.longitud.toDouble())
                val velocitat = velocitats.getOrNull(index)?.second ?: 0.0
                Marker(
                    state = MarkerState(position = point),
                    icon = circleIcon,
                    title = "Punt ${index + 1} - %.2f km/h".format(velocitat),
                    snippet = "Lat: %.5f, Lng: %.5f".format(point.latitude, point.longitude)
                )
            }
        }

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

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp, vertical = 24.dp)
                .background(Color.White.copy(alpha = 0.98f), shape = RoundedCornerShape(24.dp))
                .padding(vertical = 24.dp, horizontal = 24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Resum de la ruta",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryGreen
                )

                val formatterSortida = DateTimeFormatter.ofPattern("dd/MM/yyyy · HH:mm")

                val dataRuta = ruta.startDate?.let {
                    try {
                        val data = LocalDateTime.parse(it.replace(" ", "T"))
                        data.format(formatterSortida)
                    } catch (e: Exception) {
                        "Data no disponible"
                    }
                } ?: "Data no disponible"

                InfoRow(label = "Data de la ruta:", value = dataRuta)
                InfoRow(label = "Distància:", value = "${String.format("%.2f", ruta.distance)} km")
                InfoRow(label = "Duració:", value = ruta.totalTime ?: "—")
                InfoRow(
                    label = "Velocitat mitjana:",
                    value = "${String.format("%.2f", ruta.averageSpeed)} km/h"
                )
                InfoRow(
                    label = "Velocitat màxima:",
                    value = "${String.format("%.2f", velocitatMaxima)} km/h",
                    valueColor = if (velocitatMaxima > 35) Color.Red else Color.Unspecified
                )
            }
        }
    }
}

fun createCustomMarkerIconSafely(zoomLevel: Float): BitmapDescriptor {
    val baseSize = 60f
    val size = (baseSize * (zoomLevel / 15f).coerceIn(0.6f, 1.5f)).toInt()

    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    val borderPaint = Paint().apply {
        color = android.graphics.Color.WHITE
        isAntiAlias = true
    }

    val fillPaint = Paint().apply {
        color = android.graphics.Color.parseColor("#FF27C08A")
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

fun calcularDistanciaEntrePunts(
    lat1: Double, lon1: Double,
    lat2: Double, lon2: Double
): Float {
    val resultado = FloatArray(1)
    Location.distanceBetween(lat1, lon1, lat2, lon2, resultado)
    return resultado[0]
}

@Composable
fun InfoRow(label: String, value: String, valueColor: Color = Color.Unspecified) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = label, fontSize = 15.sp, color = Color.Gray)
        Text(text = value, fontSize = 15.sp, fontWeight = FontWeight.Medium, color = valueColor)
    }
}
