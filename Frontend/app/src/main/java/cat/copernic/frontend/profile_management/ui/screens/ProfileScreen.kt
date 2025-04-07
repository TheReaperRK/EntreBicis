package cat.copernic.frontend.profile_management.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cat.copernic.frontend.auth_management.data.management.SessionManager
import cat.copernic.frontend.navigation.Screens
import cat.copernic.frontend.profile_management.management.UserRepo
import cat.copernic.frontend.profile_management.management.UserRetrofitInstance
import cat.copernic.frontend.profile_management.ui.viewmodels.ProfileViewModel
import cat.copernic.frontend.profile_management.ui.viewmodels.ProfileViewModelFactory
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val coroutineScope = rememberCoroutineScope()

    val repository = remember { UserRepo(UserRetrofitInstance.userApi) }
    val viewModel: ProfileViewModel = viewModel(factory = ProfileViewModelFactory(repository))
    val user by viewModel.user.collectAsState()

    // Cargar usuario al iniciar pantalla
    LaunchedEffect(Unit) {
        try {
            val email = sessionManager.getEmail()
            Log.d("ProfileScreen", "Obtenido email de sesión: $email")

            email?.let {
                viewModel.loadUserByEmail(it)
            }
        } catch (e: Exception) {
            Log.e("ProfileScreen", "Error al cargar el usuario", e)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (user != null) {
                Text("Hola, ${user!!.name}")
                Text("Correu: ${user!!.mail}")
                Text("Rol: ${user!!.role}")
                // Puedes seguir con más datos...
            } else {
                CircularProgressIndicator()
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = {
                coroutineScope.launch {
                    println(sessionManager.getEmail())
                    sessionManager.clearSession()
                    Toast.makeText(context, "Sessió tancada", Toast.LENGTH_SHORT).show()
                    navController.navigate(Screens.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }) {
                Text("Tancar sessió")
            }
        }
    }
}

