package cl.duoc.mpazpro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cl.duoc.mpazpro.navigation.NavigationEvent
import cl.duoc.mpazpro.navigation.Screen
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
    private val usuarioViewModel: UsuarioViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MpazPro_PruebaTheme {
                val navController = rememberNavController()

                // Escucha de eventos de navegación emitidos por MainViewModel
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

                // NavHost que administra el flujo completo
                NavHost(
                    navController = navController,
                    startDestination = Screen.Usuario.route
                ) {
                    composable(route = Screen.Home.route) {
                        HomeScreen(viewModel = mainViewModel)
                    }
                    composable(route = Screen.Profile.route) {
                        ProfileScreen(onNavigateBack = { mainViewModel.navigateBack() })
                    }
                    composable(route = Screen.Settings.route) {
                        SettingsScreen(onNavigateBack = { mainViewModel.navigateBack() })
                    }

                    // Destino: Formulario de Usuario (Caso MPAZ PRO con 4 parámetros)
                    composable(route = Screen.Usuario.route) {
                        UsuarioScreen(
                            viewModel = usuarioViewModel,
                            onNavigateToSummary = { rut, nombre, correo, rol ->
                                val rutaDestino = "resumen_page/$rut/$nombre/$correo/$rol"
                                navController.navigate(rutaDestino)
                            }
                        )
                    }

                    // Destino: Resumen con recepción de 4 argumentos
                    composable(
                        route = "resumen_page/{rut}/{nombre}/{correo}/{rol}",
                        arguments = listOf(
                            navArgument("rut") { type = NavType.StringType },
                            navArgument("nombre") { type = NavType.StringType },
                            navArgument("correo") { type = NavType.StringType },
                            navArgument("rol") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val rut = backStackEntry.arguments?.getString("rut") ?: ""
                        val nombre = backStackEntry.arguments?.getString("nombre") ?: ""
                        val correo = backStackEntry.arguments?.getString("correo") ?: ""
                        val rol = backStackEntry.arguments?.getString("rol") ?: ""

                        ResumenScreen(
                            nombre = "$nombre ($rol)",
                            correo = correo,
                            direccion = "RUT / ID: $rut",
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}