package cl.duoc.mpazpro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cl.duoc.mpazpro.navigation.NavigationEvent
import cl.duoc.mpazpro.navigation.Screen
import cl.duoc.mpazpro.ui.theme.MpazPro_PruebaTheme
import cl.duoc.mpazpro.ui.theme.screen.HomeScreen
import cl.duoc.mpazpro.ui.theme.screen.ProfileScreen
import cl.duoc.mpazpro.ui.theme.screen.SettingsScreen
import cl.duoc.mpazpro.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MpazPro_PruebaTheme {
                val navController = rememberNavController()

                // Escucha de eventos de navegación emitidos por el ViewModel
                LaunchedEffect(key1 = true) {
                    viewModel.navigationEvent.collect { event ->
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

                // NavHost que administra la renderización de pantallas
                NavHost(
                    navController = navController,
                    startDestination = Screen.Home.route
                ) {
                    composable(route = Screen.Home.route) {
                        HomeScreen(viewModel = viewModel)
                    }
                    composable(route = Screen.Profile.route) {
                        ProfileScreen(onNavigateBack = { viewModel.navigateBack() })
                    }
                    composable(route = Screen.Settings.route) {
                        SettingsScreen(onNavigateBack = { viewModel.navigateBack() })
                    }
                }
            }
        }
    }
}