package cat.copernic.frontend.rewards_management.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cat.copernic.frontend.R
import cat.copernic.frontend.core.models.Reward
import cat.copernic.frontend.rewards_management.management.RewardRepo
import cat.copernic.frontend.rewards_management.management.RewardRetrofitInstance
import cat.copernic.frontend.rewards_management.ui.components.RewardDetailContent
import cat.copernic.frontend.rewards_management.ui.viewmodels.RewardsViewModel
import cat.copernic.frontend.rewards_management.viewmodels.RewardDetailViewModelFactory

@Composable
fun RewardDetailScreen(
    id: Long,
    navController: NavController,
    repo: RewardRepo = RewardRepo(RewardRetrofitInstance.getInstance(LocalContext.current))
) {
    val viewModel: RewardsViewModel = viewModel(factory = RewardDetailViewModelFactory(repo))
    val reward by viewModel.reward.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadRewardById(id)
    }

    reward?.let {
        RewardDetailContent(it, viewModel) { navController.popBackStack() }
    } ?: run {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}

