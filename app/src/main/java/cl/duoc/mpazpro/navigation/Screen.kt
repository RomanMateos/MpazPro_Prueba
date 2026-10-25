package cl.duoc.mpazpro.navigation

sealed class Screen(val route: String) {
    data object Home : Screen(route = "home_page")
    data object Profile : Screen(route = "profile_page")
    data object Settings : Screen(route = "settings_page")
    data object Usuario : Screen(route = "usuario_page")
    data object Resumen : Screen(route = "resumen_page/{rut}/{nombre}/{correo}/{rol}")
}