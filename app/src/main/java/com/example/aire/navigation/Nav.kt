package com.example.aire.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import com.example.aire.screens.*

sealed class Screen(//se utiliza para representar jerarquias cerradas, todos los subtipos de screen deben estar en el archivo
    val route: String,//cadena de ruta
    val label: String,//etiqueta para la navegacion
    val icon: androidx.compose.ui.graphics.vector.ImageVector//icono para la navegacion
) {// se definen los objetos con rutas, etiquetas e iconos
    object Home : Screen("home", "Inicio", Icons.Default.Home)
    object Screen1 : Screen("screen1", "Mapa", Icons.Default.LocationOn)
    object Screen2 : Screen("screen2", "Resumen", Icons.Default.Warning)
}//si se agregaran mas pantallas es facil agregar una, creando un objeto nuevo

@Composable
fun Nav(onToggleTheme: () -> Unit) {
    val navController = rememberNavController()//crea y recuerda una instancia de navcontroller

    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(//muestra el destino actual segun la ruta
            navController = navController,
            startDestination = Screen.Home.route,//define el destino inicial
            modifier = Modifier.fillMaxSize()
        ) {//define destinos
            composable(Screen.Home.route) {
                Home(onToggleTheme = onToggleTheme)
            }
            composable(Screen.Screen1.route) {
                Map(onToggleTheme = onToggleTheme)
            }
            composable(Screen.Screen2.route) {
                summary()
            }
        }

        //posicionamiento de la parte inferior
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.weight(1f)) // empuja el BottomBar al fondo
            Bottom(navController = navController)//se le pasa el navcontroller para manejar la navegacion
        }
    }
}