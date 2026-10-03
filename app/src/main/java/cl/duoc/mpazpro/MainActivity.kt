package cl.duoc.mpazpro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cl.duoc.mpazpro.ui.HomeScreen
import cl.duoc.mpazpro.ui.theme.MpazPro_PruebaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MpazPro_PruebaTheme {
                HomeScreen()
            }
        }
    }
}