package cat.copernic.frontend.navigation

sealed class Screens(val route: String) {
    object Login : Screens("login")
    object Home : Screens("home")
    object Profile : Screens("profile")
    object Rewards : Screens("rewards")
    object Register : Screens("register")
    object RewardDetail : Screens("reward_detail/{id}/"){
        fun createRoute(id: Long): String = "reward_detail/$id"
    }
    object Recover : Screens("recover")
    object Reset : Screens("reset")
    object EditUser : Screens("edit_user")
    object Route : Screens("start_route")
    object RouteList : Screens("route_list")
    object RewardsListByUser : Screens("rewards_by_user_list")
}
