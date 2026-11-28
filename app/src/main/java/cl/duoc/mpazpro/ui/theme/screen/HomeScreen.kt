package cl.duoc.mpazpro.ui.theme.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duoc.mpazpro.model.Unidad
import cl.duoc.mpazpro.navigation.Screen
import cl.duoc.mpazpro.viewmodel.MainViewModel
import cl.duoc.mpazpro.viewmodel.UnidadUiState
import cl.duoc.mpazpro.viewmodel.UnidadViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    unidadViewModel: UnidadViewModel,
    onNavigateToForm: (Long?) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val uiState by unidadViewModel.uiState.collectAsState()
    var unidadAEliminar by remember { mutableStateOf<Unidad?>(null) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = "MPAZ PRO 2.º",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                HorizontalDivider()
                NavigationDrawerItem(
                    label = { Text("Inicio") },
                    selected = true,
                    onClick = {
                        scope.launch { drawerState.close() }
                        viewModel.navigateTo(Screen.Home.route)
                    },
                    icon = { Icon(Icons.Default.Home, contentDescription = null) }
                )
                NavigationDrawerItem(
                    label = { Text("Perfil de Usuario") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        viewModel.navigateTo(Screen.Profile.route)
                    },
                    icon = { Icon(Icons.Default.Person, contentDescription = null) }
                )
                NavigationDrawerItem(
                    label = { Text("Configuración") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        viewModel.navigateTo(Screen.Settings.route)
                    },
                    icon = { Icon(Icons.Default.Settings, contentDescription = null) }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("MPAZ PRO - Unidades") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menú")
                        }
                    }
                )
            },
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        selected = true,
                        onClick = { viewModel.navigateTo(Screen.Home.route) },
                        icon = { Icon(Icons.Default.Home, contentDescription = null) },
                        label = { Text("Inicio") }
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = { viewModel.navigateTo(Screen.Profile.route) },
                        icon = { Icon(Icons.Default.Person, contentDescription = null) },
                        label = { Text("Perfil") }
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = { viewModel.navigateTo(Screen.Settings.route) },
                        icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                        label = { Text("Ajustes") }
                    )
                }
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { onNavigateToForm(null) }) {
                    Icon(Icons.Default.Add, contentDescription = "Crear unidad")
                }
            }
        ) { innerPadding ->
            BoxWithConstraints(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                val esPantallaAncha = maxWidth >= 600.dp

                when (val estado = uiState) {
                    is UnidadUiState.Cargando -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }

                    is UnidadUiState.Error -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Error: ${estado.mensaje}",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(onClick = { unidadViewModel.cargar() }) {
                                Text("Reintentar")
                            }
                        }
                    }

                    is UnidadUiState.Exito -> {
                        if (estado.unidades.isEmpty()) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text("No hay unidades registradas. Presiona + para crear una.")
                            }
                        } else if (esPantallaAncha) {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(estado.unidades, key = { it.id ?: it.hashCode() }) { unidad ->
                                    UnidadCard(
                                        unidad = unidad,
                                        onEditar = { onNavigateToForm(unidad.id) },
                                        onEliminar = { unidadAEliminar = unidad }
                                    )
                                }
                            }
                        } else {
                            LazyColumn(
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(estado.unidades, key = { it.id ?: it.hashCode() }) { unidad ->
                                    UnidadCard(
                                        unidad = unidad,
                                        onEditar = { onNavigateToForm(unidad.id) },
                                        onEliminar = { unidadAEliminar = unidad }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Diálogo de confirmación antes de eliminar
    unidadAEliminar?.let { unidad ->
        AlertDialog(
            onDismissRequest = { unidadAEliminar = null },
            title = { Text("Eliminar unidad") },
            text = { Text("¿Seguro que deseas eliminar «${unidad.titulo}»?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        unidad.id?.let { unidadViewModel.eliminar(it) }
                        unidadAEliminar = null
                    }
                ) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { unidadAEliminar = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun UnidadCard(
    unidad: Unidad,
    onEditar: () -> Unit,
    onEliminar: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = unidad.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = unidad.asignatura,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Objetivo: ${unidad.oa}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Row {
                IconButton(onClick = onEditar) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar")
                }
                IconButton(onClick = onEliminar) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}