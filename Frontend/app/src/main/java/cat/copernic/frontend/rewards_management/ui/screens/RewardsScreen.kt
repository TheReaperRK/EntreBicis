package cat.copernic.frontend.rewards_management.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cat.copernic.frontend.R
import coil.compose.rememberAsyncImagePainter
import cat.copernic.frontend.core.models.Reward
import cat.copernic.frontend.navigation.Screens
import cat.copernic.frontend.rewards_management.management.RewardRepo
import cat.copernic.frontend.rewards_management.management.RewardRetrofitInstance
import cat.copernic.frontend.rewards_management.ui.viewmodels.RewardsViewModel
import cat.copernic.frontend.rewards_management.viewmodels.RewardsViewModelFactory

@Composable
fun RewardsScreen(navController: NavController) {
    val viewModel: RewardsViewModel = viewModel(factory = RewardsViewModelFactory(RewardRepo(
        RewardRetrofitInstance.rewardApi)))
    val rewards by viewModel.rewards.collectAsState()
    val loading by viewModel.loading.collectAsState()

    var search by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize()) {
        // 🔎 Barra de búsqueda
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF166C4E))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Search, contentDescription = null, tint = Color.White)
            Spacer(Modifier.width(8.dp))
            TextField(
                value = search,
                onValueChange = { search = it },
                placeholder = { Text("Busca recompenses") },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(50)),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White
                )
            )
        }

        // 🏷️ Título
        Text(
            text = "Recompenses",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(start = 16.dp, top = 16.dp)
        )

        if (loading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(rewards.filter {
                    it.nom?.contains(search, ignoreCase = true) == true
                }) { reward ->
                    RewardCard(reward = reward) {
                        navController.navigate("reward_detail/${reward.nom}/${reward.preu}/${reward.descripcio}/${reward.comerc}/${reward.direccio}")
                    }
                }
            }
        }
    }
}

@Composable
fun RewardCard(reward: Reward, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color.LightGray,
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
fun RewardDetailScreen(
    nom: String?,
    preu: Double?,
    descripcio: String?,
    comerc: String?,
    direccio: String?,
    onBack: () -> Unit = {},
    onSolicitarClick: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Header con color de fondo
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
            // Imagen (placeholder de momento)
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background), // cambia si usas imagen real
                contentDescription = "Recompensa",
                modifier = Modifier
                    .size(180.dp)
                    .align(Alignment.BottomCenter)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Text(text = "${preu?.toInt()} Pts", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = nom ?: "", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = descripcio ?: "", style = MaterialTheme.typography.bodyMedium)
            Divider(modifier = Modifier.padding(vertical = 16.dp))
            Text(text = comerc ?: "", style = MaterialTheme.typography.bodyMedium)
            Text(text = direccio ?: "", style = MaterialTheme.typography.bodySmall)
        }

        Spacer(modifier = Modifier.weight(1f))

        // Botón solicitar
        Button(
            onClick = { onSolicitarClick() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF166C4E)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .height(56.dp),
            shape = RoundedCornerShape(50)
        ) {
            Text("Sol·licitar", color = Color.White)
        }
    }
}
