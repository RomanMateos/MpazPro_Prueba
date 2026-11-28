package cl.duoc.mpazpro.ui.theme.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cl.duoc.mpazpro.viewmodel.UnidadUiState
import cl.duoc.mpazpro.viewmodel.UnidadViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnidadFormScreen(
    unidadId: Long?,
    unidadViewModel: UnidadViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by unidadViewModel.uiState.collectAsState()
    val esEdicion = unidadId != null

    var titulo by remember { mutableStateOf("") }
    var asignatura by remember { mutableStateOf("") }
    var oa by remember { mutableStateOf("") }

    var tituloTocado by remember { mutableStateOf(false) }
    var asignaturaTocada by remember { mutableStateOf(false) }
    var oaTocado by remember { mutableStateOf(false) }
    var menuExpandido by remember { mutableStateOf(false) }
    var datosCargados by remember { mutableStateOf(false) }

    // Precarga los datos si estamos editando una unidad existente
    LaunchedEffect(unidadId, uiState) {
        if (esEdicion && !datosCargados && uiState is UnidadUiState.Exito) {
            val existente = (uiState as UnidadUiState.Exito).unidades.find { it.id == unidadId }
            if (existente != null) {
                titulo = existente.titulo
                asignatura = existente.asignatura
                oa = existente.oa
                datosCargados = true
            }
        }
    }

    val errorTitulo = UnidadViewModel.validarTitulo(titulo)
    val errorAsignatura = UnidadViewModel.validarAsignatura(asignatura)
    val errorOa = UnidadViewModel.validarOa(oa)
    val formularioValido = UnidadViewModel.esFormularioValido(titulo, asignatura, oa)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (esEdicion) "Editar Unidad" else "Nueva Unidad") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Campo Título
            OutlinedTextField(
                value = titulo,
                onValueChange = {
                    titulo = it
                    tituloTocado = true
                },
                label = { Text("Título de la unidad") },
                singleLine = true,
                isError = tituloTocado && errorTitulo != null,
                trailingIcon = {
                    if (tituloTocado && errorTitulo != null) {
                        Icon(Icons.Default.Error, contentDescription = "Error", tint = MaterialTheme.colorScheme.error)
                    }
                },
                supportingText = {
                    if (tituloTocado && errorTitulo != null) {
                        Text(errorTitulo)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            // Selector de Asignatura (4 opciones válidas)
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = asignatura,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Asignatura") },
                    isError = asignaturaTocada && errorAsignatura != null,
                    trailingIcon = {
                        if (asignaturaTocada && errorAsignatura != null) {
                            Icon(Icons.Default.Error, contentDescription = "Error", tint = MaterialTheme.colorScheme.error)
                        } else {
                            Icon(
                                imageVector = if (menuExpandido) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                                contentDescription = "Desplegar asignaturas"
                            )
                        }
                    },
                    supportingText = {
                        if (asignaturaTocada && errorAsignatura != null) {
                            Text(errorAsignatura)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable {
                            asignaturaTocada = true
                            menuExpandido = true
                        }
                )
                DropdownMenu(
                    expanded = menuExpandido,
                    onDismissRequest = { menuExpandido = false }
                ) {
                    UnidadViewModel.ASIGNATURAS_VALIDAS.forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion) },
                            onClick = {
                                asignatura = opcion
                                asignaturaTocada = true
                                menuExpandido = false
                            }
                        )
                    }
                }
            }

            // Campo OA (formato OA + 2 dígitos)
            OutlinedTextField(
                value = oa,
                onValueChange = {
                    oa = it
                    oaTocado = true
                },
                label = { Text("Objetivo de Aprendizaje (ej. OA03)") },
                singleLine = true,
                isError = oaTocado && errorOa != null,
                trailingIcon = {
                    if (oaTocado && errorOa != null) {
                        Icon(Icons.Default.Error, contentDescription = "Error", tint = MaterialTheme.colorScheme.error)
                    }
                },
                supportingText = {
                    if (oaTocado && errorOa != null) {
                        Text(errorOa)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (esEdicion && unidadId != null) {
                        unidadViewModel.actualizar(unidadId, titulo, asignatura, oa, onExito = onNavigateBack)
                    } else {
                        unidadViewModel.crear(titulo, asignatura, oa, onExito = onNavigateBack)
                    }
                },
                enabled = formularioValido,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (esEdicion) "Guardar cambios" else "Crear unidad")
            }
        }
    }
}