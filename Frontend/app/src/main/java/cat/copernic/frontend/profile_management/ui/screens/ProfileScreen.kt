package cat.copernic.frontend.profile_management.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cat.copernic.frontend.auth_management.data.management.SessionManager
import cat.copernic.frontend.navigation.Screens
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Pantalla de perfil")

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = {
                coroutineScope.launch {
                    sessionManager.clearSession()
                    Toast.makeText(context, "Sesión cerrada", Toast.LENGTH_SHORT).show()
                    navController.navigate(Screens.Login.route) {
                        popUpTo(0) { inclusive = true } // Elimina el backstack
                    }
                }
            }) {
                Text("Tancar sessió")
            }
        }
    }
}
