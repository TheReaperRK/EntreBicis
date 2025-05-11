package cat.copernic.frontend.core.utils

import android.graphics.BitmapFactory
import android.os.Build
import android.util.Base64
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.Alignment
import calcularDistanciaEntrePunts
import cat.copernic.frontend.core.models.DTO.GpsPointDTO
import cat.copernic.frontend.core.models.enums.RewardStatus
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun base64ToImageBitmap(base64String: String): ImageBitmap? {
    return try {
        val cleanBase64 = base64String.substringAfter("base64,", base64String)
        val decodedBytes = Base64.decode(cleanBase64, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        bitmap?.asImageBitmap()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

@Composable
fun turnBackButton (navController: NavController, modifier: Modifier = Modifier){

    // Botón de regreso.
    IconButton(
        onClick = { navController.popBackStack() },
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "tornar",
            tint = White
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun calcularVelocitatsEntrePunts(punts: List<GpsPointDTO>): List<Pair<Int, Double>> {
    val formatter = DateTimeFormatter.ISO_DATE_TIME
    val resultats = mutableListOf<Pair<Int, Double>>()

    for (i in 1 until punts.size) {
        val p1 = punts[i - 1]
        val p2 = punts[i]

        val lat1 = p1.latitud.toDouble()
        val lon1 = p1.longitud.toDouble()
        val lat2 = p2.latitud.toDouble()
        val lon2 = p2.longitud.toDouble()

        val distanciaMetres = calcularDistanciaEntrePunts(lat1, lon1, lat2, lon2)

        val t1 = LocalDateTime.parse(p1.timestamp, formatter)
        val t2 = LocalDateTime.parse(p2.timestamp, formatter)

        val segons = Duration.between(t1, t2).seconds.coerceAtLeast(1)
        val velocitatMs = distanciaMetres / segons.toDouble()
        val velocitatKmH = velocitatMs * 3.6

        resultats.add(i to velocitatKmH)
    }

    return resultats
}

fun RewardStatus.toCatalanStatus(): String = when (this) {
    RewardStatus.AVAILABLE -> "Disponible"
    RewardStatus.PENDING -> "Pendent"
    RewardStatus.ACCEPTED -> "Acceptada"
    RewardStatus.COLLECTED -> "Recollida"
    RewardStatus.CANCELED -> "Cancel·lada"
}


