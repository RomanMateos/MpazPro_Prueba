package cl.duoc.mpazpro.navigation

sealed class Screen(val route: String) {
    data object Home : Screen(route = "home_page")
    data object Profile : Screen(route = "profile_page")
    data object Settings : Screen(route = "settings_page")
    data object Usuario : Screen(route = "usuario_page")
    data object Resumen : Screen(route = "resumen_page/{rut}/{nombre}/{correo}/{rol}")
    data object Novedades : Screen(route = "novedades_page")
    data object UnidadForm : Screen(route = "unidad_form_page?unidadId={unidadId}") {
        fun createRoute(unidadId: Long? = null): String =
            if (unidadId != null) "unidad_form_page?unidadId=$unidadId" else "unidad_form_page"
    }
}