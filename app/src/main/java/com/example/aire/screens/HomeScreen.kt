package com.example.aire.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )
    {Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(
            onClick = { /* TODO: Acción para "Todos" */ },
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Todos",
                style = MaterialTheme.typography.bodySmall
            )
        }

        TextButton(
            onClick = { /* TODO: Acción para "Favoritos" */ },
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Favoritos",
                style = MaterialTheme.typography.bodySmall,
                textAlign = androidx.compose.ui.text.style.TextAlign.End
            )
        }
    }
        Text("Bienvenido a AIRE", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Esta es la pantalla de inicio.")
    }
}
