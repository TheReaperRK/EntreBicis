package cat.copernic.frontend.auth_management.ui.screens.recover

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.material3.SearchBarDefaults.InputField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cat.copernic.frontend.R
import cat.copernic.frontend.auth_management.data.source.AuthRetrofitInstance
import cat.copernic.frontend.auth_management.ui.components.ErrorMessageBox
import cat.copernic.frontend.core.ui.theme.PrimaryGreen
import cat.copernic.frontend.core.ui.theme.SecondaryGreen
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
    var passwordVisible by remember { mutableStateOf(false) }

    var emailError by remember { mutableStateOf("") }
    var tokenError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var repeatPasswordError by remember { mutableStateOf("") }
    var generalError by remember { mutableStateOf("") }

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
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.align(Alignment.Start)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Enrere", tint = Color.White)
        }

        Image(
            painter = painterResource(id = R.drawable.entrebicislogowb),
            contentDescription = "Logo EntreBicis",
            modifier = Modifier.size(240.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correu electrònic", color = Color.White) },
            singleLine = true,
            isError = emailError.isNotEmpty(),
            modifier = Modifier.fillMaxWidth(),
            colors = textFieldColors()
        )
        ErrorMessageBox(message = emailError)

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = token,
            onValueChange = { if (it.length <= 250) token = it },
            label = { Text("Token", color = Color.White) },
            singleLine = true,
            isError = tokenError.isNotEmpty(),
            modifier = Modifier.fillMaxWidth(),
            colors = textFieldColors()
        )
        ErrorMessageBox(message = tokenError)

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = word,
            onValueChange = { if (it.length <= 60) word = it },
            label = { Text("Nova contrasenya", color = Color.White) },
            singleLine = true,
            isError = passwordError.isNotEmpty(),
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val icon = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(icon, contentDescription = null, tint = Color.White)
                }
            },
            colors = textFieldColors()
        )
        ErrorMessageBox(message = passwordError)

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = wordRepeat,
            onValueChange = { if (it.length <= 60) wordRepeat = it },
            label = { Text("Repeteix la contrasenya", color = Color.White) },
            singleLine = true,
            isError = repeatPasswordError.isNotEmpty(),
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val icon = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(icon, contentDescription = null, tint = Color.White)
                }
            },
            colors = textFieldColors()
        )
        ErrorMessageBox(message = repeatPasswordError)

        ErrorMessageBox(message = generalError)

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                emailError = ""
                tokenError = ""
                passwordError = ""
                repeatPasswordError = ""
                generalError = ""

                if (email.isBlank() || token.isBlank() || word.isBlank() || wordRepeat.isBlank()) {
                    generalError = "Tots els camps són obligatoris"
                    return@Button
                }

                if (!isValidEmail(email)) emailError = "Correu no vàlid"
                if (token.length > 250) tokenError = "Token massa llarg"
                if (!isValidPassword(word)) passwordError = "Contrasenya massa dèbil"
                if (word != wordRepeat) repeatPasswordError = "Les contrasenyes no coincideixen"

                if (emailError.isEmpty() && tokenError.isEmpty()
                    && passwordError.isEmpty() && repeatPasswordError.isEmpty()
                ) {
                    coroutineScope.launch {
                        val response = AuthRetrofitInstance.authApi.resetPassword(
                            email.toRequestBody("text/plain".toMediaTypeOrNull()),
                            token.toRequestBody("text/plain".toMediaTypeOrNull()),
                            word.toRequestBody("text/plain".toMediaTypeOrNull())
                        )

                        if (response.isSuccessful) {
                            Toast.makeText(context, "Contrasenya canviada amb èxit", Toast.LENGTH_LONG).show()
                            navController.navigate("login")
                        } else {
                            val message = try {
                                JSONObject(response.errorBody()?.string()).getString("message")
                            } catch (e: Exception) {
                                "Error inesperat"
                            }
                            generalError = message
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF27C08A))
        ) {
            Text("Resetejar contrasenya", color = Color.White)
        }
    }
}

@Composable
private fun textFieldColors() = OutlinedTextFieldDefaults.colors(
    unfocusedBorderColor = Color.White,
    focusedBorderColor = Color.White,
    cursorColor = Color.White,
    unfocusedLabelColor = Color.White,
    focusedLabelColor = Color.White,
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    errorBorderColor = Color(0xFFEF6C00),
    errorLabelColor = Color(0xFFEF6C00),
    errorCursorColor = Color(0xFFEF6C00),
    errorLeadingIconColor = Color(0xFFEF6C00),
    errorTrailingIconColor = Color(0xFFEF6C00),
    errorTextColor = Color.White
)

fun isValidEmail(email: String) =
    email.length <= 60 && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

fun isValidPassword(password: String) =
    password.length >= 8 &&
            password.any { it.isUpperCase() } &&
            password.any { it.isLowerCase() } &&
            password.any { it.isDigit() } &&
            password.any { !it.isLetterOrDigit() }
