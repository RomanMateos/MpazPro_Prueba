package cl.duoc.mpazpro.ui.theme.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import cl.duoc.mpazpro.R
import cl.duoc.mpazpro.navigation.Screen
import cl.duoc.mpazpro.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: MainViewModel
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = "MPAZ PRO",
                    modifier = Modifier.padding(all = 16.dp),
                    style = MaterialTheme.typography.titleLarge
                )
                NavigationDrawerItem(
                    label = { Text(text = "Inicio") },
                    selected = true,
                    onClick = {
                        scope.launch { drawerState.close() }
                        viewModel.navigateTo(Screen.Home.route)
                    },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") }
                )
                NavigationDrawerItem(
                    label = { Text(text = "Perfil") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        viewModel.navigateTo(Screen.Profile.route)
                    },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") }
                )
                NavigationDrawerItem(
                    label = { Text(text = "Configuración") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        viewModel.navigateTo(Screen.Settings.route)
                    },
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Configuración") }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "MPAZ PRO - Principal") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menú lateral")
                        }
                    }
                )
            },
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        selected = true,
                        onClick = { viewModel.navigateTo(Screen.Home.route) },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                        label = { Text(text = "Inicio") }
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = { viewModel.navigateTo(Screen.Profile.route) },
                        icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
                        label = { Text(text = "Perfil") }
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = { viewModel.navigateTo(Screen.Settings.route) },
                        icon = { Icon(Icons.Default.Settings, contentDescription = "Configuración") },
                        label = { Text(text = "Configuración") }
                    )
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(paddingValues = innerPadding)
                    .fillMaxSize()
                    .padding(all = 16.dp),
                verticalArrangement = Arrangement.spacedBy(space = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "¡Bienvenido a MPAZ PRO!",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge
                )
                Button(onClick = { viewModel.navigateTo(Screen.Profile.route) }) {
                    Text(text = "Ver Perfil")
                }
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo App",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height = 150.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}