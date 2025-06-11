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
fun Home(onToggleTheme: () -> Unit) {
    val context = LocalContext.current//obtiene el contexto actual para obtener los datos
    val scope = rememberCoroutineScope()//
    val parqueDataStore = remember { ParqueDataStore(context) }//inicializa el parque data store y lo mantiene en memoria
    val parques by parqueDataStore.parques.collectAsState(initial = emptyList())//obtiene los parques desde el data store
    var showMenu by remember { mutableStateOf(false) }//estado booleano que maneja el menu desplegable
    var mostrarFavoritos by remember { mutableStateOf(false) }//estado booleano que maneja si se muestran todos los parques o solo los favoritos
    val parquesMostrados = if (mostrarFavoritos) {//si mostrar favoritos es true, se muestran solo los parques favoritos
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
                    IconButton(onClick = { showMenu = true }) {//al hacer click cambia el estado
                        Icon(
                            Icons.Default.Menu,
                            contentDescription = "Menú",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },//vuelve a false al cerrar
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "Cambiar tema",
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            },
                            onClick = {//al hacer click se cierra y llama a la funcion cambiar tema del mainActivity
                                showMenu = false
                                onToggleTheme()
                            }
                        )
                    }
                },
                actions = {//define la accion de otros botones
                    Row(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(0.85f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { mostrarFavoritos = false },//al hacer click cambia el estado
                            colors = ButtonDefaults.buttonColors(//cambio de color si está seleccionado
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

                        Button(//mismas funciones que el anterior
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
                },//configura colores de topbar
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = androidx.compose.ui.graphics.Color.Transparent,
                    titleContentColor = androidx.compose.ui.graphics.Color.Transparent,
                    navigationIconContentColor = MaterialTheme.colorScheme.secondary,
                    actionIconContentColor = MaterialTheme.colorScheme.secondary
                ),
                modifier = Modifier.fillMaxWidth(),
                scrollBehavior = null
            )
        },//lo que se verá en el espacio restante
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(//renderiza solo lo que se ve
                    modifier = Modifier.fillMaxWidth(),//ajusta el tamaño de la columna
                    contentPadding = PaddingValues(bottom = 104.dp) // Padding para no quedar bajo el nav
                ) {
                    items(parquesMostrados) { parque ->//funcion de lazy column que renderiza cada parque
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