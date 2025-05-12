package cat.copernic.frontend.core.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cat.copernic.frontend.auth_management.data.management.UserSessionViewModel
import cat.copernic.frontend.navigation.Screens

@Composable
fun HomeScreen(navController: NavController, sessionViewModel: UserSessionViewModel) {
    val context = LocalContext.current
    val user by sessionViewModel.userDto.collectAsState()
    val isSessionRestored by sessionViewModel.isSessionRestored.collectAsState()

    LaunchedEffect(isSessionRestored, user) {
        if (!isSessionRestored) return@LaunchedEffect // aún no ha terminado

        if (user != null) {
            navController.navigate(Screens.Rewards.route) {
                popUpTo(0) { inclusive = true }
            }
        } else {
            sessionViewModel.clearSession(context)
            Toast.makeText(context, "Sessió expirada", Toast.LENGTH_SHORT).show()
            navController.navigate(Screens.Login.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    // Muestra pantalla de carga mientras no esté restaurada
    if (!isSessionRestored) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}


