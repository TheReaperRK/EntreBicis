package cat.copernic.frontend.route_management.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cat.copernic.frontend.route_management.ui.components.MapaRouteDetall
import cat.copernic.frontend.route_management.ui.components.MapaRuta
import cat.copernic.frontend.route_management.ui.viewmodels.RouteDetailViewModel

@Composable
fun DetallRouteScreen(routeId: Long, context: Context, navController: NavController) {
    val viewModel: RouteDetailViewModel = viewModel()
    val rutaState = viewModel.ruta.collectAsState()
    val ruta = rutaState.value

    LaunchedEffect(routeId) {
        viewModel.carregarRuta(routeId, context)
    }

    ruta?.let {
        MapaRouteDetall(ruta, context = context, navController)
    } ?: run {
        CircularProgressIndicator(modifier = Modifier.fillMaxSize())
    }

}
