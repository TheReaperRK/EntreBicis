package cat.copernic.frontend.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import cat.copernic.frontend.auth_management.ui.screens.LoginScreen
import cat.copernic.frontend.core.ui.components.BottomNavigationBar
import cat.copernic.frontend.core.ui.screens.HomeScreen
import cat.copernic.frontend.profile_management.ui.screens.ProfileScreen
import cat.copernic.frontend.rewards_management.ui.screens.RewardsScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val showBottomBar = navController
        .currentBackStackEntryAsState().value?.destination?.route !in listOf(Screens.Login.route)

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screens.Login.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screens.Login.route) { LoginScreen(navController) }
            composable(Screens.Home.route) { HomeScreen() }
            composable(Screens.Rewards.route) { RewardsScreen() }
            composable(Screens.Profile.route) { ProfileScreen() }
        }
    }
}
