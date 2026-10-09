package cl.duoc.mpazpro.ui.theme.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenExpandida() {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "MPAZ PRO - Vista Expandida") })
        }
    ) { innerPadding ->
        Row(
            modifier = Modifier
                .padding(paddingValues = innerPadding)
                .fillMaxSize()
                .padding(all = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(space = 32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo App",
                modifier = Modifier
                    .weight(1f)
                    .height(height = 250.dp),
                contentScale = ContentScale.Fit
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "¡Vista Expandida Multicolumna!",
                    color = MaterialTheme.colorScheme.tertiary,
                    style = MaterialTheme.typography.headlineMedium
                )
                Button(onClick = { /* Acción futura */ }) {
                    Text(text = "Acción Principal")
                }
            }
        }
    }
}

@Preview(name = "Expanded", widthDp = 1000, heightDp = 800, showBackground = true)
@Composable
fun PreviewExpanded() {
    HomeScreenExpandida()
}