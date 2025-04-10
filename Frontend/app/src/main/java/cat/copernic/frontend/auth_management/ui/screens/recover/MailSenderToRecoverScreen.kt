package cat.copernic.frontend.auth_management.ui.screens.recover

import android.graphics.Color
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cat.copernic.frontend.R
import cat.copernic.frontend.auth_management.data.source.AuthRetrofitInstance
import cat.copernic.frontend.core.ui.theme.PrimaryGreen
import cat.copernic.frontend.navigation.Screens
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

@Composable
fun PasswordRecover(navController: NavController) {
    var email by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryGreen),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            // Botón de regreso.
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .padding(start = 16.dp, top = 16.dp)
                    .align(Alignment.Start)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "tornar",
                    tint = White
                )
            }

            Image(
                painter = painterResource(id = R.drawable.entrebicislogowb),
                contentDescription = "logo entre bicis",
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "recover password",
                style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold, color = White)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Campo para el correo
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("enter mail") },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para enviar la solicitud de recuperación
            Button(
                onClick = {
                    coroutineScope.launch {
                        val emailRequestBody: RequestBody = email.toRequestBody("text/plain".toMediaTypeOrNull())
                        val response = AuthRetrofitInstance.authApi.recoverMailSender(email)

                        if (response.isSuccessful) {
                            Toast.makeText(context, "email enviat", Toast.LENGTH_LONG).show()
                            println("correo de recuperacion enviado: " + email)
                            navController.navigate(Screens.Reset.route)
                        } else {
                            Toast.makeText(context, "error amb la petició", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Black)
            ) {
                Text("recover")
            }
        }
    }
}