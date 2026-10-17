package cl.duoc.mpazpro.navigation

// Sealed class para definir rutas tipo-safe (seguras) en la navegación de MPAZ PRO
sealed class Screen(val route: String) {
    // Rutas principales de la aplicación
    data object Home : Screen(route = "home_page")
    data object Profile : Screen(route = "profile_page")
    data object Settings : Screen(route = "settings_page")
}