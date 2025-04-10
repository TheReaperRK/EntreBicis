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
import androidx.compose.ui.platform.LocalContext
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
import cat.copernic.frontend.rewards_management.ui.components.RewardCard
import cat.copernic.frontend.rewards_management.ui.viewmodels.RewardsViewModel
import cat.copernic.frontend.rewards_management.viewmodels.RewardsViewModelFactory

@Composable
fun RewardsScreen(navController: NavController) {
    val viewModel: RewardsViewModel = viewModel(
        factory = RewardsViewModelFactory(
            RewardRepo(RewardRetrofitInstance.getInstance(LocalContext.current))
        )
    )
    val rewards by viewModel.rewards.collectAsState()
    val loading by viewModel.loading.collectAsState()

    // Llama solo una vez
    LaunchedEffect(Unit) {
        viewModel.loadRewards()
    }

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
                        navController.navigate("api/rewards/${reward.id}")
                    }
                }
            }
        }
    }
}

