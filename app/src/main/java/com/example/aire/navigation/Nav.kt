package com.example.aire.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHost
import com.example.aire.screens.* // Asegúrate de que todas tus pantallas están aquí
import com.google.firebase.auth.FirebaseAuth // Importa FirebaseAuth
import androidx.navigation.compose.composable

// Asumo que tu composable 'Bottom' está en el mismo paquete o es accesible.
// Si está en otro paquete, por ejemplo 'com.example.aire', necesitarías:
// import com.example.aire.Bottom
// Para este ejemplo, lo mantengo así si está en el mismo paquete 'navigation'.
// Si Bottom está en un archivo separado pero en el mismo paquete 'navigation', no se requiere importación explícita dentro del paquete.
// Si Bottom está definido directamente en este mismo archivo Nav.kt, tampoco se requiere importación.


// Clase sellada para representar la jerarquía de pantallas en tu aplicación.
// Todos los subtipos de Screen deben estar definidos dentro de este archivo.
sealed class Screen(
    val route: String, // La cadena de ruta para la navegación
    val label: String, // Etiqueta para el elemento de navegación (ej. en una Barra Inferior)
    val icon: androidx.compose.ui.graphics.vector.ImageVector // Icono para el elemento de navegación
) {
    // Define tus objetos de pantalla con sus rutas, etiquetas e iconos.
    // Es fácil añadir más pantallas simplemente creando un nuevo objeto aquí.
    object Home : Screen("home_route", "Inicio", Icons.Default.Home)
    object Map : Screen("map_route", "Mapa", Icons.Default.LocationOn)
    object Summary : Screen("summary_route", "Resumen", Icons.Default.Warning)
}

@Composable
fun Nav(
    navController: NavController, // NavController ahora se pasa desde MainActivity
    auth: FirebaseAuth,           // FirebaseAuth ahora se pasa desde MainActivity
    onToggleTheme: () -> Unit     // La función para alternar el tema se pasa desde MainActivity
) {
    // Se elimina rememberNavController() aquí, ya que ahora se pasa desde MainActivity.
    // val navController = rememberNavController()

    Box(modifier = Modifier.fillMaxSize()) {
        NavHost( // Muestra el destino actual según la ruta
            navController = navController,
            startDestination = Screen.Home.route, // Define el destino inicial
            modifier = Modifier.fillMaxSize()
        ) { // Define tus destinos composables
            composable(Screen.Home.route) {
                // Pasa todos los parámetros necesarios a tu pantalla Home
                Home(navController = navController, auth = auth, onToggleTheme = onToggleTheme)
            }
            composable(Screen.Map.route) {
                // Pasa todos los parámetros necesarios a tu pantalla Map
                Map(navController = navController, auth = auth, onToggleTheme = onToggleTheme)
            }
            composable(Screen.Summary.route) {
                // Pasa todos los parámetros necesarios a tu pantalla Summary
                // Summary quizás no necesite onToggleTheme, pero es importante pasar navController y auth si los usa.
                Summary(navController = navController, auth = auth)
            }
        }

        // Posicionamiento de la barra de navegación inferior
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.weight(1f)) // Empuja el BottomBar al fondo
            Bottom(navController = navController) // Se le pasa el navController para manejar la navegación desde el Bottom Bar
        }
    }
}