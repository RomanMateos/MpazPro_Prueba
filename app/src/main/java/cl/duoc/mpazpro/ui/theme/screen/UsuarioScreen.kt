package cl.duoc.mpazpro.ui.theme.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cl.duoc.mpazpro.viewmodel.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsuarioScreen(
    viewModel: UsuarioViewModel,
    onNavigateToSummary: (String, String, String, String) -> Unit
) {
    val estado by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "MPAZ PRO - Acceso de Usuarios") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Registro e Identificación del Usuario",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            // Campo RUT / ID Ficticio
            OutlinedTextField(
                value = estado.rut,
                onValueChange = { viewModel.onRutCambiado(it) },
                label = { Text("RUT / ID Ficticio") },
                leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null) },
                isError = estado.errores.containsKey("rut"),
                modifier = Modifier.fillMaxWidth()
            )
            estado.errores["rut"]?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            // Campo Nombre
            OutlinedTextField(
                value = estado.nombre,
                onValueChange = { viewModel.onNombreCambiado(it) },
                label = { Text("Nombre Completo") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                isError = estado.errores.containsKey("nombre"),
                modifier = Modifier.fillMaxWidth()
            )
            estado.errores["nombre"]?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            // Campo Correo
            OutlinedTextField(
                value = estado.correo,
                onValueChange = { viewModel.onCorreoCambiado(it) },
                label = { Text("Correo Institucional") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                isError = estado.errores.containsKey("correo"),
                modifier = Modifier.fillMaxWidth()
            )
            estado.errores["correo"]?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            // Seleccionar Rol (Estudiante, Docente, UTP)
            Text("Seleccione Perfil / Rol:", modifier = Modifier.align(Alignment.Start))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("Estudiante", "Docente", "UTP").forEach { rol ->
                    FilterChip(
                        selected = (estado.rolSeleccionado == rol),
                        onClick = { viewModel.onRolCambiado(rol) },
                        label = { Text(rol) }
                    )
                }
            }

            // Botón Ingresar
            Button(
                onClick = {
                    onNavigateToSummary(estado.rut, estado.nombre, estado.correo, estado.rolSeleccionado)
                },
                enabled = estado.esValido,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text(text = "Confirmar Registro y Continuar")
            }
        }
    }
}