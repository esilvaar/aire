package com.example.aire.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.aire.datastore.ParqueDataStore
import com.google.android.gms.maps.model.*
import com.google.maps.android.compose.*
import com.example.aire.model.Parque

@Composable
fun Screen1(onToggleTheme: () -> Unit) {
    val context = LocalContext.current
    val parqueDataStore = remember { ParqueDataStore(context) }
    val parques by parqueDataStore.parques.collectAsState()

    var expanded by remember { mutableStateOf(false) }
    var nivelSeleccionado by remember { mutableStateOf("Todos") }

    val niveles = listOf("Todos", "Buena", "Moderada", "Alta")

    fun filtrarParques(parques: List<Parque>, nivel: String): List<Parque> {
        return when (nivel) {
            "Buena" -> parques.filter { it.calidadAire < 50 }
            "Moderada" -> parques.filter { it.calidadAire in 50..99 }
            "Alta" -> parques.filter { it.calidadAire >= 100 }
            else -> parques
        }
    }

    val parquesFiltrados = filtrarParques(parques, nivelSeleccionado)

    val defaultLocation = LatLng(
        parquesFiltrados.firstOrNull()?.coordenadaY ?: -33.45,
        parquesFiltrados.firstOrNull()?.coordenadaX ?: -70.6667
    )

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, 11f)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Mapa ocupa todo el tamaño
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
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
                    snippet = "ICA: ${parque.calidadAire}, Temp: ${parque.temperatura}°C",
                    icon = BitmapDescriptorFactory.defaultMarker(hue)
                )
            }
        }

        // Filtro flotante en esquina superior izquierda
        Column(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
                .wrapContentSize()
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f))
                .padding(8.dp)
        ) {
            Text("Nivel seleccionado: $nivelSeleccionado", style = MaterialTheme.typography.labelMedium)

            Box {
                Button(
                    onClick = { expanded = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)) // Verde
                ) {
                    Text("Filtrar", color = Color.White)
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    niveles.forEach { nivel ->
                        DropdownMenuItem(
                            text = { Text(nivel) },
                            onClick = {
                                nivelSeleccionado = nivel
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}
