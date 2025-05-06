package cat.copernic.frontend.route_management.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cat.copernic.frontend.core.ui.theme.PrimaryGreen
import cat.copernic.frontend.route_management.ui.components.RouteCard
import cat.copernic.frontend.route_management.ui.viewmodels.RouteViewModel
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.navigation.NavController


@Composable
fun LlistaRutesScreen(
    viewModel: RouteViewModel,
    context: Context,
    navController: NavController
) {
    val routes by viewModel.userRoutes.collectAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.carregarRutesUsuari(context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = PrimaryGreen)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Text(
            text = "Les meves rutes",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp),
            color = Color.White
        )

        routes.forEach { route ->
            RouteCard(route = route) {
                println(route.id)
                navController.navigate("detall_ruta/${route.id}")
            }
            Spacer(modifier = Modifier.height(12.dp)) // ✅ separador entre tarjetas
        }
    }
}
