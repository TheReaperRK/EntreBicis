package cat.copernic.frontend.navigation

import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cat.copernic.frontend.auth_management.data.management.UserSessionViewModel
import cat.copernic.frontend.auth_management.ui.screens.LoginScreen
import cat.copernic.frontend.auth_management.ui.screens.RegisterScreen
import cat.copernic.frontend.core.ui.components.BottomNavigationBar
import cat.copernic.frontend.core.ui.screens.HomeScreen
import cat.copernic.frontend.profile_management.ui.screens.ProfileScreen
import cat.copernic.frontend.rewards_management.ui.screens.RewardDetailScreen
import cat.copernic.frontend.rewards_management.ui.screens.RewardsScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val userSessionViewModel: UserSessionViewModel = viewModel()

    // Restaurar sesión al iniciar app
    val context = LocalContext.current // ✅ permitido aquí

    LaunchedEffect(Unit) {
        userSessionViewModel.restoreSession(context)
    }

    // ✅ Comprobamos si hay token almacenado
    val prefs = remember {
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    }
    val hasToken = !prefs.getString("jwt_token", null).isNullOrBlank()

    // ✅ Elegimos la pantalla inicial según sesión activa
    val initialDestination = if (hasToken) Screens.Home.route else Screens.Login.route

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
            startDestination = initialDestination, // 🔁 Aquí se elige la ruta inicial
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screens.Login.route) { LoginScreen(navController, userSessionViewModel) }
            composable(Screens.Register.route) { RegisterScreen() }
            composable(Screens.Home.route) { HomeScreen(navController, userSessionViewModel) }
            composable(Screens.Rewards.route) { RewardsScreen(navController) }
            composable(Screens.Profile.route) { ProfileScreen(navController, userSessionViewModel) }
            composable(
                route = "api/rewards/{id}",
                arguments = listOf(navArgument("id") {
                    type = NavType.LongType
                })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getLong("id") ?: return@composable
                RewardDetailScreen(id = id, navController = navController)
            }
        }
    }
}

