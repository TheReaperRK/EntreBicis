package cat.copernic.frontend.profile_management.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import cat.copernic.frontend.R
import coil.compose.rememberAsyncImagePainter
import cat.copernic.frontend.core.models.Reward

@Composable
fun ReservaActivaCard(reward: Reward) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 6.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .padding(horizontal = 24.dp),
        color = Color.White
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Fondo verdoso con nombre de la recompensa
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color(0xFF166C4E), shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                contentAlignment = Alignment.CenterStart
            ) {
                reward.nom?.let {
                    Text(
                        text = it,
                        color = Color.White,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }

            // Contenido negro en la parte inferior
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "${reward.preu.toInt()} Pts",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Black
                )
                reward.comerc?.let {
                    Text(text = it, color = Color.DarkGray)
                }
                reward.direccio?.let {
                    Text(text = it, color = Color.DarkGray)
                }
            }
        }
    }
}

