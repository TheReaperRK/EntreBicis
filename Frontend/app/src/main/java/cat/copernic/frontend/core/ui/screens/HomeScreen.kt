package cat.copernic.frontend.core.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Pantalla de inicio")
            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = {
                sessionViewModel.clearSession(context)
                Toast.makeText(context, "Sessió tancada", Toast.LENGTH_SHORT).show()
                navController.navigate(Screens.Login.route) {
                    popUpTo(0) { inclusive = true }
                }
            }) {
                Text("Tancar sessió")
            }
        }
    }
}
