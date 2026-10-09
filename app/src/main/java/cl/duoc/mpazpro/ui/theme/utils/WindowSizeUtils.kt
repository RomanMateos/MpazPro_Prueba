package cl.duoc.mpazpro.ui.utils

import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable

// Función utilitaria que mide la pantalla del dispositivo
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun obtenerWindowSizeClass(): WindowSizeClass {
    return calculateWindowSizeClass(LocalActivity.current as Activity)
}