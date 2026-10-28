package cl.duoc.mpazpro

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cl.duoc.mpazpro.navigation.NavigationEvent
import cl.duoc.mpazpro.navigation.Screen
import cl.duoc.mpazpro.repository.UserPreferencesRepository
import cl.duoc.mpazpro.ui.theme.MpazPro_PruebaTheme
import cl.duoc.mpazpro.ui.theme.screen.HomeScreen
import cl.duoc.mpazpro.ui.theme.screen.ProfileScreen
import cl.duoc.mpazpro.ui.theme.screen.ResumenScreen
import cl.duoc.mpazpro.ui.theme.screen.SettingsScreen
import cl.duoc.mpazpro.ui.theme.screen.UsuarioScreen
import cl.duoc.mpazpro.viewmodel.MainViewModel
import cl.duoc.mpazpro.viewmodel.UsuarioViewModel

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    private val usuarioViewModel: UsuarioViewModel by viewModels {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val repository = UserPreferencesRepository(applicationContext)
                return UsuarioViewModel(repository) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MpazPro_PruebaTheme {
                val navController = rememberNavController()

                LaunchedEffect(key1 = true) {
                    mainViewModel.navigationEvent.collect { event ->
                        when (event) {
                            is NavigationEvent.NavigateTo -> {
                                navController.navigate(event.route) {
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                            is NavigationEvent.NavigateBack -> {
                                navController.popBackStack()
                            }
                        }
                    }
                }

                NavHost(
                    navController = navController,
                    startDestination = Screen.Usuario.route
                ) {
                    composable(route = Screen.Home.route) {
                        HomeScreen(viewModel = mainViewModel)
                    }
                    composable(route = Screen.Profile.route) {
                        ProfileScreen(
                            viewModel = usuarioViewModel,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                    composable(route = Screen.Settings.route) {
                        SettingsScreen(onNavigateBack = { mainViewModel.navigateBack() })
                    }

                    composable(route = Screen.Usuario.route) {
                        UsuarioScreen(
                            viewModel = usuarioViewModel,
                            onNavigateToSummary = { rut, nombre, correo, rol ->
                                val encodedRut = Uri.encode(rut)
                                val encodedNombre = Uri.encode(nombre)
                                val encodedCorreo = Uri.encode(correo)
                                val encodedRol = Uri.encode(rol)
                                val rutaDestino = "resumen_page/$encodedRut/$encodedNombre/$encodedCorreo/$encodedRol"
                                navController.navigate(rutaDestino)
                            }
                        )
                    }

                    composable(
                        route = "resumen_page/{rut}/{nombre}/{correo}/{rol}",
                        arguments = listOf(
                            navArgument("rut") { type = NavType.StringType },
                            navArgument("nombre") { type = NavType.StringType },
                            navArgument("correo") { type = NavType.StringType },
                            navArgument("rol") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val rut = Uri.decode(backStackEntry.arguments?.getString("rut") ?: "")
                        val nombre = Uri.decode(backStackEntry.arguments?.getString("nombre") ?: "")
                        val correo = Uri.decode(backStackEntry.arguments?.getString("correo") ?: "")
                        val rol = Uri.decode(backStackEntry.arguments?.getString("rol") ?: "")

                        ResumenScreen(
                            rut = rut,
                            nombre = nombre,
                            correo = correo,
                            rol = rol,
                            onNavigateToHome = {
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(Screen.Usuario.route) { inclusive = true }
                                }
                            },
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}