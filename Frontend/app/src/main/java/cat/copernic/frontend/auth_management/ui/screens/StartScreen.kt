package cat.copernic.frontend.auth_management.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import cat.copernic.frontend.auth_management.data.management.SessionManager
import cat.copernic.frontend.navigation.Screens

@Composable
fun StartScreen(navController: NavController) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    val email by sessionManager.email.collectAsState(initial = null)
    val sessionKey by sessionManager.sessionKey.collectAsState(initial = null)

    LaunchedEffect(email, sessionKey) {
        if (!email.isNullOrBlank() && !sessionKey.isNullOrBlank()) {
            // Hay sesión -> ir a Home
            navController.navigate(Screens.Home.route) {
                popUpTo(0) { inclusive = true }
            }
        } else {
            // No hay sesión -> ir a Login
            navController.navigate(Screens.Login.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    // Pantalla de carga mientras se determina la navegación
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
