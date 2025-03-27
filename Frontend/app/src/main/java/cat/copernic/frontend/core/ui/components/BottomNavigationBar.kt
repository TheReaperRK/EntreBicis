package cat.copernic.frontend.core.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import cat.copernic.frontend.navigation.bottomNavItems

@Composable
fun BottomNavigationBar(navController: NavController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    NavigationBar {
        bottomNavItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = { navController.navigate(item.route) },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}