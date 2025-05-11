package cat.copernic.frontend.profile_management.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cat.copernic.frontend.auth_management.data.management.UserSessionViewModel
import cat.copernic.frontend.navigation.Screens
import cat.copernic.frontend.core.utils.base64ToImageBitmap
import cat.copernic.frontend.profile_management.management.UserRetrofitInstance
import cat.copernic.frontend.profile_management.ui.components.ReservaActivaCard

@Composable
fun ProfileScreen(navController: NavController, sessionViewModel: UserSessionViewModel, ) {
    val context = LocalContext.current
    val user by sessionViewModel.user.collectAsState()

    LaunchedEffect(true) {
        sessionViewModel.refreshUserData(context)
    }

    user?.let { userData ->
        val profileBitmap = base64ToImageBitmap(userData.image)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp, 0.dp, 0.dp, 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // CABECERA CON FONDO
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                color = Color(0xFF166C4E),
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${userData.name} ${userData.surnames}",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )
                    Text(
                        text = "${userData.balance} pts",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )
                }
            }

            println("Base64 length: ${userData.image?.length}")

            // IMAGEN Y BOTONES
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (profileBitmap != null) {
                    Image(
                        bitmap = profileBitmap,
                        contentDescription = "Foto perfil",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text("No es pot carregar la imatge")
                }

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    ProfileButton("editar") { navController.navigate(Screens.EditUser.route) }
                    ProfileButton("reserves") { navController.navigate(Screens.RewardsListByUser.route) }
                    ProfileButton("tancar sessió") {
                        navController.navigate(Screens.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // INFO
            ProfileInfoField(userData.mail)
            ProfileInfoField(userData.population)
            ProfileInfoField(userData.phone_number)

            Divider(modifier = Modifier.padding(vertical = 24.dp))

            val reservaActiva = userData.reward.firstOrNull { it.estat?.name == "ACCEPTED" && it.nom != null && it.direccio != null }

            if (reservaActiva != null) {
                Text("Reserva activa", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                ReservaActivaCard(reservaActiva) {
                    navController.navigate("reward_detail/${reservaActiva.id}")
                }
            } else  {
                Text("No tens cap reserva activa o per recollir", style = MaterialTheme.typography.titleMedium)
            }
        }
    } ?: run {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun ProfileInfoField(text: String) {

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(10.dp),
        shadowElevation = 4.dp,
        color = Color(0xFFF2F7F5)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(12.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun ProfileButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF166C4E)),
        shape = RoundedCornerShape(50),
        modifier = Modifier
            .width(140.dp)
            .height(38.dp)
    ) {
        Text(text = text, color = Color.White)
    }
}




