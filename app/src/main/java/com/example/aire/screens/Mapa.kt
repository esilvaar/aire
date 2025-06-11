package com.example.aire.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Park
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.aire.datastore.ParqueDataStore
import com.google.android.gms.maps.model.*
import com.google.maps.android.compose.*
import com.example.aire.model.Parque

@Composable
fun Screen1(onToggleTheme: () -> Unit) {
    val context = LocalContext.current
    val parqueDataStore = remember { ParqueDataStore(context) }
    val parques by parqueDataStore.parques.collectAsState(initial = emptyList())

    // Estados para filtros y búsqueda
    var expanded by remember { mutableStateOf(false) }
    var nivelSeleccionado by remember { mutableStateOf("Filtrar") }
    var filtroTipo by remember { mutableStateOf("Todos") }
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    var parqueSeleccionado by remember { mutableStateOf<String?>(null) }

    val niveles = listOf("Filtrar", "Buena", "Moderada", "Alta")

    fun filtrarParques(parques: List<Parque>, nivel: String, tipo: String, busqueda: String): List<Parque> {
        var parquesFiltrados = parques

        // Filtrar por nivel de calidad del aire
        parquesFiltrados = when (nivel) {
            "Buena" -> parquesFiltrados.filter { it.calidadAire < 50 }
            "Moderada" -> parquesFiltrados.filter { it.calidadAire in 50..99 }
            "Alta" -> parquesFiltrados.filter { it.calidadAire >= 100 }
            else -> parquesFiltrados
        }

        // Filtrar por tipo (urbano/nacional)
        parquesFiltrados = when (tipo) {
            "Urbano" -> parquesFiltrados.filter { it.tipo == "urbano" }
            "Nacional" -> parquesFiltrados.filter { it.tipo == "nacional" }
            else -> parquesFiltrados
        }

        // Filtrar por búsqueda de nombre
        if (busqueda.isNotEmpty()) {
            parquesFiltrados = parquesFiltrados.filter {
                it.nombre.contains(busqueda, ignoreCase = true)
            }
        }

        return parquesFiltrados
    }

    val parquesFiltrados = filtrarParques(parques, nivelSeleccionado, filtroTipo, searchText.text)

    val defaultLocation = LatLng(
        parquesFiltrados.firstOrNull()?.coordenadaY ?: -33.45,
        parquesFiltrados.firstOrNull()?.coordenadaX ?: -70.6667
    )

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, 11f)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Mapa con marcadores
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            // Agregar marcadores para cada parque filtrado
            parquesFiltrados.forEach { parque ->
                val hue = when {
                    parque.calidadAire < 50 -> BitmapDescriptorFactory.HUE_GREEN
                    parque.calidadAire in 50..99 -> BitmapDescriptorFactory.HUE_YELLOW
                    parque.calidadAire >= 100 -> BitmapDescriptorFactory.HUE_RED
                    else -> BitmapDescriptorFactory.HUE_BLUE
                }

                Marker(
                    state = MarkerState(position = LatLng(parque.coordenadaY, parque.coordenadaX)),
                    title = parque.nombre,
                    snippet = "Calidad del aire: ${parque.calidadAire}",
                    icon = BitmapDescriptorFactory.defaultMarker(hue),
                    onClick = {
                        parqueSeleccionado = parque.nombre
                        true
                    }
                )
            }
        }

        // Controles superiores
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // Barra de búsqueda
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Buscar parques") },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Buscar"
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Fila única con todos los filtros alineados
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Dropdown para filtro de calidad del aire
                Box {
                    Button(
                        onClick = { expanded = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        ),
                        modifier = Modifier.height(36.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                Icons.Default.FilterList,
                                contentDescription = "Filtrar",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                nivelSeleccionado,
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        niveles.forEach { nivel ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        nivel,
                                        color = MaterialTheme.colorScheme.secondary,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                },
                                onClick = {
                                    nivelSeleccionado = nivel
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                // Botón Urbano
                Button(
                    onClick = {
                        filtroTipo = if (filtroTipo == "Urbano") "Todos" else "Urbano"
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (filtroTipo == "Urbano")
                            MaterialTheme.colorScheme.primary
                        else
                            Color.Transparent
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = "Urbano",
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            "Urbano",
                            color = MaterialTheme.colorScheme.secondary,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                // Botón Nacional
                Button(
                    onClick = {
                        filtroTipo = if (filtroTipo == "Nacional") "Todos" else "Nacional"
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (filtroTipo == "Nacional")
                            MaterialTheme.colorScheme.primary
                        else
                            Color.Transparent
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            Icons.Default.Park,
                            contentDescription = "Nacional",
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            "Nacional",
                            color = MaterialTheme.colorScheme.secondary,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        // Mostrar nombre del parque seleccionado
        parqueSeleccionado?.let { nombreParque ->
            Card(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = nombreParque,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { parqueSeleccionado = null },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Text("Cerrar", color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}