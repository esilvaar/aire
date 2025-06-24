package com.example.aire.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

// Composable que representa la pantalla de inicio de sesión
@Composable
fun LoginScreen(navController: NavController, auth: FirebaseAuth) {
    // Estado para almacenar el correo electrónico ingresado
    var email by remember { mutableStateOf("") }

    // Estado para almacenar la contraseña ingresada
    var password by remember { mutableStateOf("") }

    // Estado para mostrar mensajes de error (por ejemplo, si falla la autenticación)
    var error by remember { mutableStateOf<String?>(null) }

    // Diseño en columna centrado verticalmente con padding
    Column(
        modifier = Modifier
            .fillMaxSize() // Ocupa todo el alto y ancho disponible
            .padding(24.dp), // Margen interno
        verticalArrangement = Arrangement.Center // Centra verticalmente los elementos
    ) {
        // Campo de texto para ingresar el correo electrónico
        TextField(
            value = email,
            onValueChange = { email = it }, // Actualiza el estado cuando el usuario escribe
            label = { Text("Correo") },
            singleLine = true // Solo permite una línea de texto
        )

        Spacer(modifier = Modifier.height(8.dp)) // Espacio entre los campos

        // Campo de texto para ingresar la contraseña
        TextField(
            value = password,
            onValueChange = { password = it }, // Actualiza el estado cuando el usuario escribe
            label = { Text("Contraseña") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation() // Oculta los caracteres ingresados
        )

        Spacer(modifier = Modifier.height(16.dp)) // Espacio antes del botón

        // Botón para iniciar sesión
        Button(onClick = {
            // Llama a Firebase Authentication para iniciar sesión con correo y contraseña
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { result ->
                    if (result.isSuccessful) {
                        // Si la autenticación fue exitosa, navega a la pantalla "home"
                        navController.navigate("home") {
                            // Elimina "login" del backstack para que no se pueda volver atrás con "back"
                            popUpTo("login") { inclusive = true }
                        }
                    } else {
                        // Si hubo un error, muestra el mensaje en pantalla
                        error = "Autenticación fallida: ${result.exception?.localizedMessage}"
                    }
                }
        }) {
            Text("Iniciar sesión") // Texto del botón
        }

        // Si hay un mensaje de error, lo muestra en pantalla
        error?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(it, color = MaterialTheme.colorScheme.error) // Muestra el error en color rojo
        }
    }
}