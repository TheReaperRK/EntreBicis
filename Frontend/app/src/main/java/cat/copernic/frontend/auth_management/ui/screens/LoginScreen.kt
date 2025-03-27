package cat.copernic.frontend.auth_management.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cat.copernic.frontend.R
import cat.copernic.frontend.auth_management.data.management.SessionManager
import cat.copernic.frontend.auth_management.data.source.AuthRetrofitInstance
import cat.copernic.frontend.navigation.Screens
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var word by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(value = email, onValueChange = { email = it }, label = { Text("Correo") })
        Spacer(modifier = Modifier.height(16.dp))
        TextField(value = word, onValueChange = { word = it }, label = { Text("Contraseña") })
        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            coroutineScope.launch {
                if (email.isBlank() || word.isBlank()) {
                    Toast.makeText(context, "Introduce email y contraseña", Toast.LENGTH_SHORT).show()
                    return@launch
                }
                println("Intentando login con: $email")
                try {
                    println("pulsado")
                    val response = AuthRetrofitInstance.authApi.login(email, word)
                    println(response)
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        if (loginResponse != null) {
                            sessionManager.saveSession(
                                email = loginResponse.email,
                                sessionKey = loginResponse.sessionKey
                            )

                            navController.navigate(Screens.Home.route) {
                                popUpTo(Screens.Login.route) { inclusive = true }
                            }
                        }
                    } else {
                        Toast.makeText(context, "Error de autenticación", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    println(e.message)
                    Toast.makeText(context, "Error de red: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }) {
            Text("Iniciar sesión")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = {
            navController.navigate("register")
        }) {
            Text("Registrar-se")
        }
    }
}
