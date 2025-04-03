package cat.copernic.frontend.navigation

sealed class Screens(val route: String) {
    object Login : Screens("login")
    object Home : Screens("home")
    object Profile : Screens("profile")
    object Rewards : Screens("rewards")
    object Register : Screens("register")
    object Start : Screens("start")
}