package cl.duoc.mpazpro.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.duoc.mpazpro.R

// Pantalla principal estructurada con Scaffold y Jetpack Compose
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    // Scaffold nos da la estructura base de la pantalla (TopBar y contenedor de contenido)
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi App Kotlin - MPAZ PRO") }
            )
        }
    ) { innerPadding ->
        // Contenedor vertical (Column) con alineación centrada y espaciado de 20dp entre elementos
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Elemento de texto con estilo de título de MaterialTheme
            Text(
                text = "¡Bienvenido!",
                style = MaterialTheme.typography.headlineMedium
            )

            // Botón interactivo básico
            Button(
                onClick = { /* Acción futura */ }
            ) {
                Text("Presióname")
            }

            // Componente de imagen que carga logo.png desde la carpeta /res/drawable
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo App",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}

// Vista previa del diseño sin necesidad de ejecutar la app completa
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}