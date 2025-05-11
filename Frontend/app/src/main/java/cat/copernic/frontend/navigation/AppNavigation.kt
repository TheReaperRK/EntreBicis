package cat.copernic.frontend.navigation

import android.content.Context
import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cat.copernic.frontend.auth_management.data.management.UserSessionViewModel
import cat.copernic.frontend.auth_management.ui.screens.LoginScreen
import cat.copernic.frontend.auth_management.ui.screens.recover.PasswordRecover
import cat.copernic.frontend.auth_management.ui.screens.recover.ResetWordScreen
import cat.copernic.frontend.core.models.Reward
import cat.copernic.frontend.core.ui.components.BottomNavigationBar
import cat.copernic.frontend.core.ui.screens.HomeScreen
import cat.copernic.frontend.profile_management.management.UserRepo
import cat.copernic.frontend.profile_management.management.UserRetrofitInstance
import cat.copernic.frontend.profile_management.ui.screens.EditProfileScreen
import cat.copernic.frontend.profile_management.ui.screens.ProfileScreen
import cat.copernic.frontend.profile_management.ui.screens.RewardCollectScreen
import cat.copernic.frontend.profile_management.ui.viewmodels.ProfileViewModel
import cat.copernic.frontend.profile_management.ui.viewmodels.ProfileViewModelFactory
import cat.copernic.frontend.rewards_management.management.RewardRepo
import cat.copernic.frontend.rewards_management.management.RewardRetrofitInstance
import cat.copernic.frontend.rewards_management.ui.screens.RewardDetailScreen
import cat.copernic.frontend.rewards_management.ui.screens.RewardsScreen
import cat.copernic.frontend.rewards_management.ui.screens.UserRewardsScreen
import cat.copernic.frontend.rewards_management.ui.viewmodels.RewardsViewModel
import cat.copernic.frontend.rewards_management.viewmodels.RewardsViewModelFactory
import cat.copernic.frontend.route_management.ui.components.FinalRouteScreen
import cat.copernic.frontend.route_management.ui.screens.DetallRouteScreen
import cat.copernic.frontend.route_management.ui.screens.LlistaRutesScreen
import cat.copernic.frontend.route_management.ui.screens.StartRouteScreen
import cat.copernic.frontend.route_management.ui.viewmodels.RouteViewModel
import com.google.android.gms.maps.model.LatLng
import okhttp3.internal.platform.android.ConscryptSocketAdapter.Companion.factory

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Restaurar sesión al iniciar app
    val context = LocalContext.current // ✅ permitido aquí

    val userSessionViewModel: UserSessionViewModel = viewModel()
    val rewardsFactory = RewardsViewModelFactory(RewardRepo(RewardRetrofitInstance.getInstance(context)))
    val rewardsViewModel: RewardsViewModel = viewModel(factory = rewardsFactory)

    val routeViewModel: RouteViewModel = viewModel()


    val factory = ProfileViewModelFactory(UserRepo(UserRetrofitInstance.getApi(context)))
    val profileViewModel: ProfileViewModel = viewModel(factory = factory)
    val sessionViewModel: ProfileViewModel = viewModel(factory = factory)


    LaunchedEffect(Unit) {
        userSessionViewModel.restoreSession(context)
    }

    // ✅ Comprobamos si hay token almacenado
    val prefs = remember {
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    }
    val hasToken = !prefs.getString("jwt_token", null).isNullOrBlank()

    // ✅ Elegimos la pantalla inicial según sesión activa
    val initialDestination = if (hasToken) Screens.Home.route else Screens.Login.route

    val showBottomBar = navController
        .currentBackStackEntryAsState().value?.destination?.route !in listOf(
        Screens.Login.route,
        Screens.Recover.route,
        Screens.Reset.route
    )
    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = initialDestination, // 🔁 Aquí se elige la ruta inicial
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screens.Login.route) { LoginScreen(navController, userSessionViewModel) }
            composable(Screens.Home.route) { HomeScreen(navController, userSessionViewModel) }
            composable(Screens.Rewards.route) { RewardsScreen(navController) }
            composable(Screens.Profile.route) { ProfileScreen(navController, userSessionViewModel) }
            composable(
                route = "api/rewards/{id}",
                arguments = listOf(navArgument("id") {
                    type = NavType.LongType
                })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getLong("id") ?: return@composable
                RewardDetailScreen(id = id, navController = navController)
            }
            composable(Screens.Recover.route) {
                PasswordRecover(navController)}

            composable(Screens.Reset.route) {
                ResetWordScreen(navController)}

            composable(Screens.EditUser.route) {
                EditProfileScreen(navController, profileViewModel = profileViewModel)
            }

            composable(Screens.Route.route) {
                StartRouteScreen(navController, userSessionViewModel)
            }

            composable("final_route") {
                val navBackStackEntry = rememberNavController().currentBackStackEntry
                val points = navBackStackEntry?.savedStateHandle?.get<List<LatLng>>("points")
                val finalLocation = navBackStackEntry?.savedStateHandle?.get<Location>("finalLocation")

                if (points != null && finalLocation != null) {
                    FinalRouteScreen(routePoints = points, ubicacioFinal = finalLocation)
                }
            }

            composable(Screens.RouteList.route) {
                LlistaRutesScreen(
                    routeViewModel,
                    context = context,
                    navController
                )
            }

            composable("detall_ruta/{id}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")?.toLongOrNull()
                id?.let {
                    DetallRouteScreen(routeId = it, context, navController)
                }
            }

            composable("reward_detail/{id}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")?.toLongOrNull()
                id?.let {
                    RewardCollectScreen (
                        rewardId = it,
                        onBackClick = { navController.popBackStack() },
                        onRecollirClick = {

                        },
                        rewardsViewModel
                    )
                }
            }

            composable(Screens.RewardsListByUser.route) {
                UserRewardsScreen(
                    sessionViewModel = userSessionViewModel,
                    viewModel = rewardsViewModel, // o viewModel() según tu DI
                    context = LocalContext.current,
                    navController = navController
                )
            }
        }
    }
}

