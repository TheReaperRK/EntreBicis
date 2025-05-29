package cat.copernic.frontend.rewards_management.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cat.copernic.frontend.auth_management.data.management.UserSessionViewModel
import cat.copernic.frontend.core.ui.theme.SecondaryGreen
import cat.copernic.frontend.rewards_management.ui.components.RewardListCard
import cat.copernic.frontend.rewards_management.ui.viewmodels.RewardsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserRewardsScreen(
    sessionViewModel: UserSessionViewModel,
    viewModel: RewardsViewModel,
    context: Context,
    navController: NavController
) {
    val rewards by viewModel.rewards.collectAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        sessionViewModel.user.value?.mail?.let { email ->
            viewModel.carregarRecompensesUsuari(email)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = SecondaryGreen)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Historial de recompenses",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Tornar enrere",
                        tint = Color.White
                    )
                }
            },
            colors = androidx.compose.material3.TopAppBarDefaults.topAppBarColors(
                containerColor = SecondaryGreen
            )
        )

        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            rewards.forEach { reward ->
                RewardListCard(reward = reward)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}
