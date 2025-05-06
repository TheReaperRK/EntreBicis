package cat.copernic.frontend.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

val bottomNavItems = listOf(
    BottomNavItem(Screens.RouteList.route, Icons.Filled.Home, "Inicio"),
    BottomNavItem(Screens.Rewards.route, Icons.Filled.Star, "Recompensas"),
    BottomNavItem(Screens.Profile.route, Icons.Filled.Person, "Perfil"),
    BottomNavItem(Screens.Route.route, Icons.Filled.Timeline, "Ruta")
)
