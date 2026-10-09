package cl.duoc.mpazpro.ui.theme.screen

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import cl.duoc.mpazpro.ui.utils.obtenerWindowSizeClass

@Composable
fun HomeScreen2() {
    // 1. Obtenemos la clasificación de tamaño del dispositivo actual
    val windowSizeClass = obtenerWindowSizeClass()

    // 2. Evaluamos el ancho y mostramos la pantalla correspondiente
    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> HomeScreenCompacta()
        WindowWidthSizeClass.Medium -> HomeScreenMediana()
        WindowWidthSizeClass.Expanded -> HomeScreenExpandida()
        else -> HomeScreenCompacta()
    }
}