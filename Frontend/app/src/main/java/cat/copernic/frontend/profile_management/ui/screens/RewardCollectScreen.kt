package cat.copernic.frontend.profile_management.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cat.copernic.frontend.R
import cat.copernic.frontend.auth_management.data.management.UserSessionViewModel
import cat.copernic.frontend.core.models.Reward
import cat.copernic.frontend.profile_management.ui.components.SwipeToCollect
import cat.copernic.frontend.rewards_management.ui.viewmodels.RewardsViewModel
import coil.compose.rememberAsyncImagePainter
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.ui.window.Dialog
import com.airbnb.lottie.compose.*
import androidx.compose.ui.unit.sp

@Composable
fun RewardCollectScreen(
    rewardId: Long,
    onBackClick: () -> Unit,
    onRecollirClick: () -> Unit,
    rewardsViewModel: RewardsViewModel
) {
    var collected by remember { mutableStateOf(false) }
    val reward by rewardsViewModel.reward.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        rewardsViewModel.loadRewardById(rewardId)
    }

    if (reward == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Carregant recompensa...")
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Box {
            Image(
                painter = rememberAsyncImagePainter(R.drawable.ic_launcher_background),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
            )

            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .padding(8.dp)
                    .background(Color(0xFF166C4E), shape = RoundedCornerShape(50))
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Tornar", tint = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Text(text = "${reward!!.preu} Pts", style = MaterialTheme.typography.headlineSmall, color = Color.Black)
            Text(text = reward!!.nom ?: "", style = MaterialTheme.typography.titleMedium, color = Color.Black)
            Spacer(modifier = Modifier.height(12.dp))
            reward!!.descripcio?.let {
                Text(text = it, style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Divider()
            Spacer(modifier = Modifier.height(16.dp))
            reward!!.direccio?.let {
                Text(text = it, style = MaterialTheme.typography.bodyMedium, color = Color.DarkGray)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            SwipeToCollect(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                onCollected = {
                    rewardsViewModel.recollirRecompensa(rewardId) { error ->
                        if (error == null) {
                            collected = true
                            showDialog = true
                        } else {
                            Log.e("Recompensa", "Error al recollir: $error")
                        }
                    }
                },
                collected = collected
            )
        }
    }

    if (showDialog) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xAA000000)), // fondo oscurecido
            contentAlignment = Alignment.Center
        ) {
            val composition by rememberLottieComposition(LottieCompositionSpec.Asset("confetti.json"))
            val progress by animateLottieCompositionAsState(
                composition = composition,
                iterations = 1,
                isPlaying = true,
                speed = 1.2f
            )

            Box(modifier = Modifier.fillMaxSize()) {
                // 🎉 Animación en toda la pantalla
                LottieAnimation(
                    composition = composition,
                    progress = progress,
                    modifier = Modifier.fillMaxSize()
                )

                // 🔼 Botón de cerrar arriba a la izquierda
                IconButton(
                    onClick = { showDialog = false },
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.TopStart)
                        .background(Color(0xFF166C4E), shape = RoundedCornerShape(50))
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Tancar popup",
                        tint = Color.White
                    )
                }

                // ✅ Texto grande centrado
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Recollida!",
                        color = Color(0xFF27C08A),
                        fontSize = 48.sp,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}