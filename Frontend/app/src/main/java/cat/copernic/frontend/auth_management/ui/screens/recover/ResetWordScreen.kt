package cat.copernic.frontend.auth_management.ui.screens.recover

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.SearchBarDefaults.InputField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cat.copernic.frontend.R
import cat.copernic.frontend.auth_management.data.source.AuthRetrofitInstance
import cat.copernic.frontend.core.ui.theme.PrimaryGreen
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

@Composable
fun ResetWordScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var token by remember { mutableStateOf("") }
    var word by remember { mutableStateOf("") }
    var wordRepeat by remember { mutableStateOf("") }

    var emailError by remember { mutableStateOf<String?>(null) }
    var tokenError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var repeatPasswordError by remember { mutableStateOf<String?>(null) }

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize().background(PrimaryGreen),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(32.dp)) {
            // Botón de regreso
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "tornar", tint = White)
            }

            Image(painter = painterResource(id = R.drawable.entrebicislogowb), "logo", modifier = Modifier.size(100.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text("Recuperar contrasenya", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = White)
            Spacer(modifier = Modifier.height(16.dp))

            // Campo para el correo
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("correu") },
                isError = emailError != null,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            if (emailError != null) {
                Text(text = emailError!!, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo para el token
            OutlinedTextField(
                value = token,
                onValueChange = {
                    if (it.length <= 250) token = it
                },
                label = { Text("token") },
                isError = tokenError != null,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            if (tokenError != null) {
                Text(text = tokenError!!, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(16.dp))

// Campo para la contraseña
            OutlinedTextField(
                value = word,
                onValueChange = {
                    if (it.length <= 60) word = it
                },
                label = { Text("nova contrasenya") },
                visualTransformation = PasswordVisualTransformation(),
                isError = passwordError != null,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            if (passwordError != null) {
                Text(text = passwordError!!, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(16.dp))

// Repetir contraseña
            OutlinedTextField(
                value = wordRepeat,
                onValueChange = {
                    if (it.length <= 60) wordRepeat = it
                },
                label = { Text("Repiteix la contrasenya") },
                visualTransformation = PasswordVisualTransformation(),
                isError = repeatPasswordError != null,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            if (repeatPasswordError != null) {
                Text(text = repeatPasswordError!!, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de recuperación
            Button(
                onClick = {
                    emailError = if (!isValidEmail(email))"invalid email" else null
                    tokenError = if (token.length > 250) "token massa llarg" else null
                    passwordError = if (!isValidPassword(word)) "contrasenya masa debil" else null
                    repeatPasswordError = if (word != wordRepeat) "les contrasenyes no son iguals" else null

                    if (emailError == null && tokenError == null && passwordError == null && repeatPasswordError == null) {
                        coroutineScope.launch {
                            val response = AuthRetrofitInstance.authApi.resetPassword(
                                email.toRequestBody("text/plain".toMediaTypeOrNull()),
                                token.toRequestBody("text/plain".toMediaTypeOrNull()),
                                word.toRequestBody("text/plain".toMediaTypeOrNull())
                            )

                            if (response.isSuccessful) {
                                Toast.makeText(context, "contrasenya canviada amb exit", Toast.LENGTH_LONG).show()
                                navController.navigate("login")
                            } else {
                                val errorBody = response.errorBody()?.string()
                                val errorMessage = try {
                                    JSONObject(errorBody).getString("message")
                                } catch (e: Exception) {
                                   "error inesperat"
                                }
                                emailError = if (errorMessage.contains("correo")) errorMessage else null
                                tokenError = if (errorMessage.contains("token")) errorMessage else null
                                passwordError = if (errorMessage.contains("contraseña")) errorMessage else null
                            }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Resetejar contrasenya")
            }
        }
    }
}


fun isValidEmail(email: String) = email.length <= 60 && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

fun isValidPassword(password: String) =
    password.length >= 8 &&
            password.any { it.isUpperCase() } &&
            password.any { it.isLowerCase() } &&
            password.any { it.isDigit() } &&
            password.any { !it.isLetterOrDigit() }