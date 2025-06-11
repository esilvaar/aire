package com.example.aire.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable//funcion responsable de renderizar la parte inferior de la pantalla
fun Bottom(navController: NavController) {//recibe navcontroller para gestionar navegacion
    val navBackStackEntry = navController.currentBackStackEntryAsState().value//obtiene el ultimno elemento de la pila
    val currentRoute = navBackStackEntry?.destination?.route//obtiene la ruta actual
    val items = listOf(Screen.Home, Screen.Screen1, Screen.Screen2)//lista de objetos screen

    Surface(//orden de la pantalla
        modifier = Modifier
            .fillMaxWidth()
            .height(88.dp),
        color = MaterialTheme.colorScheme.tertiary,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 4.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { screen ->//itera sobre la lista items
                val selected = currentRoute == screen.route//verifica que el seleccionado sea la ruta actual
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    IconButton(
                        onClick = {//define la accion al hacer click
                            navController.navigate(screen.route) {//navega al screen seleccionado
                                launchSingleTop = true//asegura que solo haya una instancia del screen en la pila
                                restoreState = true//restaura el estado del screen para otra visita
                                popUpTo(navController.graph.startDestinationId) {//limpia la pila de navegacion hasta ruta en especifico
                                    saveState = true//guarda el estado del screen
                                }
                            }
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = screen.icon,
                            contentDescription = screen.label,
                            modifier = Modifier.size(22.dp),
                            tint = if (selected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary//opciones de cambio de tema
                        )
                    }

                    Text(
                        text = screen.label,
                        fontSize = 10.sp,
                        fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal,
                        color = if (selected) MaterialTheme.colorScheme.secondary else Color(0xFF9E9E9E),
                        maxLines = 2,
                        lineHeight = 11.sp,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }
    }
}