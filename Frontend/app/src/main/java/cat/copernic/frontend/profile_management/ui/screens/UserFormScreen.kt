package cat.copernic.frontend.profile_management.ui.screens

import android.util.Log
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cat.copernic.frontend.R
import cat.copernic.frontend.core.utils.base64ToImageBitmap
import cat.copernic.frontend.core.utils.turnBackButton
import cat.copernic.frontend.profile_management.management.UserRepo
import cat.copernic.frontend.profile_management.management.UserRetrofitInstance
import cat.copernic.frontend.profile_management.ui.viewmodels.ProfileViewModel
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody

@Composable
fun EditProfileScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val email = prefs.getString("user_email", null)
        if (email != null) {
            profileViewModel.loadUserByEmail(email)
        }
    }

    val user by profileViewModel.user.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    // Estados locales para los campos editables
    var name by remember { mutableStateOf("") }
    var surnames by remember { mutableStateOf("") }
    var population by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var observations by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        imageUri = it
    }

    // Inicializar campos si es la primera vez
    LaunchedEffect(user) {
        user?.let {
            name = it.name ?: ""
            surnames = it.surnames ?: ""
            population = it.population ?: ""
            phone = it.phone_number ?: ""
            observations = it.observations ?: ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF166C4E))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(modifier = Modifier.fillMaxWidth()) {
            turnBackButton(
                navController,
                modifier = Modifier.align(Alignment.CenterStart).padding(top = 8.dp, start = 8.dp)
            )
        }

// Imagen circular
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.Gray)
                .clickable { imagePickerLauncher.launch("image/*") }
        ) {
            when {
                imageUri != null -> {
                    Image(
                        painter = rememberAsyncImagePainter(imageUri),
                        contentDescription = "Nova imatge seleccionada",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                user?.image?.isNotBlank() == true -> {
                    base64ToImageBitmap(user!!.image)?.let { bitmap ->
                        Image(
                            bitmap = bitmap,
                            contentDescription = "Imatge existent",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                else -> {
                    Image(
                        painter = painterResource(id = R.drawable.entrebicislogowb),
                        contentDescription = "Logo per defecte",
                        modifier = Modifier.size(80.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        ProfileTextField(value = name, label = "Nom", onValueChange = { name = it })
        ProfileTextField(value = surnames, label = "Cognoms", onValueChange = { surnames = it })
        ProfileTextField(value = population, label = "Població", onValueChange = { population = it })
        ProfileTextField(value = phone, label = "Telèfon", onValueChange = { phone = it })

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (name.isBlank() || surnames.isBlank() || population.isBlank() || phone.length != 9) {
                    Toast.makeText(context, "Comprova que tots els camps siguin vàlids", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                coroutineScope.launch {
                    try {
                        val repo = UserRepo(UserRetrofitInstance.getApi(context)) // O desde Hilt si usas DI
                        val imagePart = imageUri?.let {
                            val inputStream = context.contentResolver.openInputStream(it)!!
                            val bytes = inputStream.readBytes()
                            inputStream.close()

                            val request = RequestBody.create("image/*".toMediaTypeOrNull(), bytes)
                            MultipartBody.Part.createFormData("image", "profile.jpg", request)
                        }

                        Log.d("EDIT_PROFILE", "Enviando datos al servidor...")
                        val response = repo.updateUser(
                            email = user?.mail.orEmpty(),
                            name = name,
                            surnames = surnames,
                            population = population,
                            phone = phone,
                            observations = observations,
                            image = imagePart
                        )

                        if (response.isSuccessful) {
                            Log.d("EDIT_PROFILE", "Actualización exitosa")
                            navController.navigate("profile") {
                                popUpTo("editProfile") { inclusive = true }
                            }
                        } else {
                            val errorBody = response.errorBody()?.string()
                            Log.e("EDIT_PROFILE", "Error al guardar. Código: ${response.code()}, Cuerpo: $errorBody")
                            Toast.makeText(context, "Error al guardar", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Log.e("EDIT_PROFILE", "Excepción durante la actualización", e)
                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF27C08A))
        ) {
            Text("Guardar canvis", color = Color.White)
        }
    }
}

@Composable
fun ProfileTextField(value: String, label: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Color.White) },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.White,
            unfocusedBorderColor = Color.White,
            cursorColor = Color.White,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedLabelColor = Color.White,
            unfocusedLabelColor = Color.White
        )
    )
}
