package com.example.aire.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.graphics.vector.ImageVector

// Clase sellada para representar la jerarquía de pantallas en tu aplicación.
// Todos los subtipos de Screen deben estar definidos dentro de este archivo.
sealed class Screen(
    val route: String, // La cadena de ruta para la navegación
    val label: String, // Etiqueta para el elemento de navegación (ej. en una Barra Inferior)
    val icon: ImageVector // Icono para el elemento de navegación
) {
    // Define tus objetos de pantalla con sus rutas, etiquetas e iconos.
    object Home : Screen("home_route", "Inicio", Icons.Default.Home)
    object Map : Screen("map_route", "Mapa", Icons.Default.LocationOn)
    object Summary : Screen("summary_route", "Resumen", Icons.Default.Warning)
}

// El composable 'Nav' se elimina para evitar NavHosts anidados.
// La barra de navegación inferior (Bottom) se añade directamente en el Scaffold de cada pantalla.