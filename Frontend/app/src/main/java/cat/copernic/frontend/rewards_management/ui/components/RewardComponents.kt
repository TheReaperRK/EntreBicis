package cat.copernic.frontend.rewards_management.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cat.copernic.frontend.R
import cat.copernic.frontend.auth_management.data.management.UserSessionViewModel
import cat.copernic.frontend.core.models.Reward
import cat.copernic.frontend.rewards_management.ui.viewmodels.RewardsViewModel

@Composable
fun RewardCard(reward: Reward, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFF166C4E),
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        ) {
            // Placeholder de imagen
        }

        Spacer(Modifier.height(8.dp))

        Text("${reward.preu.toInt()} Pts", fontWeight = FontWeight.Bold)
        Text(reward.nom ?: "Sense nom", style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun RewardDetailContent(reward: Reward, viewModel: RewardsViewModel, onBack: () -> Unit) {

    val showDialog = remember { mutableStateOf(false) }
    val showErrorDialog = remember { mutableStateOf(false) }
    val errorMessage = remember { mutableStateOf("") }
    val userSessionViewModel: UserSessionViewModel = viewModel()
    // Restaurar sesión al iniciar app
    val context = LocalContext.current // ✅ permitido aquí

    Column(modifier = Modifier.fillMaxSize()) {

        // 🔝 Header con color de fondo
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(Color(0xFF166C4E))
        ) {
            IconButton(
                onClick = { onBack() },
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopStart)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Enrere",
                    tint = Color.White
                )
            }

            // Imagen (por ahora placeholder)
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background), // sustituir si usas una imagen real
                contentDescription = "Recompensa",
                modifier = Modifier
                    .size(180.dp)
                    .align(Alignment.BottomCenter)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ℹ️ Info del detalle
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Text(text = "${reward.preu.toInt()} Pts", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = reward.nom ?: "", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = reward.descripcio ?: "", style = MaterialTheme.typography.bodyMedium)
            Divider(modifier = Modifier.padding(vertical = 16.dp))
            Text(text = reward.comerc ?: "", style = MaterialTheme.typography.bodyMedium)
            Text(text = reward.direccio ?: "", style = MaterialTheme.typography.bodySmall)
        }

        Spacer(modifier = Modifier.weight(1f))

        // 🔘 Botón solicitar
        Button(
            onClick = { showDialog.value = true },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF166C4E)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .height(56.dp)
        ) {
            Text("Sol·licitar", color = Color.White)
        }
        if (showDialog.value) {
            AlertDialog(
                onDismissRequest = { showDialog.value = false },
                title = { Text("Confirmar bescanvi") },
                text = {
                    Text("Vols bescanviar ${reward.preu.toInt()} punts per aquesta recompensa?")
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDialog.value = false
                            viewModel.solicitarRecompensa(reward.id) { error ->
                                if (error != null) {
                                    errorMessage.value = error
                                    showErrorDialog.value = true
                                } else {
                                    onBack()
                                }
                            }
                        }
                    ) {
                        Text("Confirmar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog.value = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
    if (showErrorDialog.value) {
        AlertDialog(
            onDismissRequest = { showErrorDialog.value = false },
            title = { Text("Error") },
            text = { Text(errorMessage.value) },
            confirmButton = {
                TextButton(onClick = { showErrorDialog.value = false }) {
                    Text("Entès")
                }
            }
        )
    }
}