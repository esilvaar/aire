package com.example.aire.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.aire.card.ParqueCard
import com.example.aire.datastore.ParqueDataStore
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onToggleTheme: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val parqueDataStore = remember { ParqueDataStore(context) }
    val parques by parqueDataStore.parques.collectAsState(initial = emptyList())

    var showMenu by remember { mutableStateOf(false) }
    var mostrarFavoritos by remember { mutableStateOf(false) }

    val parquesMostrados = if (mostrarFavoritos) {
        parques.filter { it.favorito }
    } else {
        parques
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    // Sin título
                },
                navigationIcon = {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(
                            Icons.Default.Menu,
                            contentDescription = "Menú",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "Cambiar tema",
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            },
                            onClick = {
                                showMenu = false
                                onToggleTheme()
                            }
                        )
                    }
                },
                actions = {
                    Row(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(0.85f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { mostrarFavoritos = false },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (!mostrarFavoritos)
                                    MaterialTheme.colorScheme.primary
                                else
                                    androidx.compose.ui.graphics.Color.Transparent
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp)
                        ) {
                            Text("Todos", color = MaterialTheme.colorScheme.secondary)
                        }

                        Button(
                            onClick = { mostrarFavoritos = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (mostrarFavoritos)
                                    MaterialTheme.colorScheme.primary
                                else
                                    androidx.compose.ui.graphics.Color.Transparent
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Favoritos", color = MaterialTheme.colorScheme.secondary)
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = androidx.compose.ui.graphics.Color.Transparent,
                    titleContentColor = androidx.compose.ui.graphics.Color.Transparent,
                    navigationIconContentColor = MaterialTheme.colorScheme.secondary,
                    actionIconContentColor = MaterialTheme.colorScheme.secondary
                ),
                modifier = Modifier.fillMaxWidth(),
                scrollBehavior = null
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(bottom = 104.dp) // Padding para el bottom navigation
                ) {
                    items(parquesMostrados) { parque ->
                        ParqueCard(
                            parque = parque,
                            onToggleFavorito = {
                                scope.launch {
                                    parqueDataStore.toggleFavorito(parque.nombre)
                                }
                            }
                        )
                    }
                }
            }
        }
    )
}