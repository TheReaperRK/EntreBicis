package cat.copernic.frontend.auth_management.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cat.copernic.frontend.R
import cat.copernic.frontend.auth_management.data.management.UserSessionViewModel
import cat.copernic.frontend.auth_management.data.source.AuthRetrofitInstance
import cat.copernic.frontend.auth_management.ui.components.ErrorMessageBox
import cat.copernic.frontend.navigation.Screens
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavController, sessionViewModel: UserSessionViewModel) {
    var email by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var word by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF166C4E))
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo
        Image(
            painter = painterResource(id = R.drawable.entrebicislogowb),
            contentDescription = "Logo EntreBicis",
            modifier = Modifier.size(240.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Introdueix el teu correu", color = Color.White) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.White,
                focusedBorderColor = Color.White,
                cursorColor = Color.White,
                unfocusedLabelColor = Color.White,
                focusedLabelColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Contraseña
        OutlinedTextField(
            value = word,
            onValueChange = { word = it },
            label = { Text("Introdueix la teva contrasenya", color = Color.White) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val icon = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = icon, contentDescription = null, tint = Color.White)
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.White,
                focusedBorderColor = Color.White,
                cursorColor = Color.White,
                unfocusedLabelColor = Color.White,
                focusedLabelColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        ErrorMessageBox(message = errorMessage)

        Spacer(modifier = Modifier.height(8.dp))

        // Recuperar contraseña
        TextButton(onClick = {
            //navController.navigate(Screens.Recover.route)
        }) {
            Text("he oblidat la contrasenya", color = Color.White, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Botón login
        Button(
            onClick = {
                coroutineScope.launch {
                    val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")

                    if (email.isBlank() || word.isBlank()) {
                        errorMessage = "Introdueix el teu correu i contrasenya"
                        return@launch
                    }

                    if (!email.matches(emailRegex)) {
                        errorMessage = "El correu introduït no segueix el format establit"
                        return@launch
                    }

                    try {
                        val response = AuthRetrofitInstance.authApi.login(email, word)
                        if (response.isSuccessful) {
                            val loginResponse = response.body()
                            if (loginResponse != null) {
                                val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
                                prefs.edit()
                                    .putString("jwt_token", loginResponse.token)
                                    .putString("user_email", loginResponse.user.mail)
                                    .apply()

                                sessionViewModel.setSession(loginResponse.user, loginResponse.token)

                                navController.navigate(Screens.Home.route) {
                                    popUpTo(Screens.Login.route) { inclusive = true }
                                }
                            }
                        } else {
                            errorMessage = "Credencials incorrectes"
                        }
                    } catch (e: Exception) {
                        errorMessage = "Error de xarxa: ${e.message}"
                        println(e.message)
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF27C08A))
        ) {
            Text("Iniciar sessió", color = Color.White)
        }
    }
}
