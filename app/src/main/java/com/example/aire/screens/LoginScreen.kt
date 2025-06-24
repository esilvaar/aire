package com.example.aire.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.ui.graphics.Color

// Composable que representa la pantalla de inicio de sesión
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, auth: FirebaseAuth) {
    // Estado para almacenar el correo electrónico ingresado
    var email by remember { mutableStateOf("") }

    // Estado para almacenar la contraseña ingresada
    var password by remember { mutableStateOf("") }

    // Estado para mostrar mensajes de error (por ejemplo, si falla la autenticación)
    var error by remember { mutableStateOf<String?>(null) }

    // Usamos un Box para centrar todo el contenido en la pantalla y aplicar un fondo
    Box(
        modifier = Modifier
            .fillMaxSize() // Ocupa todo el alto y ancho disponible
            .background(MaterialTheme.colorScheme.background) // Usa el color de fondo del tema
            .padding(24.dp), // Margen interno general
        contentAlignment = Alignment.Center // Centra el contenido de la columna
    ) {
        // Tarjeta para contener los campos de login, dándole un aspecto elevado
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(), // Ajusta la altura al contenido
            shape = RoundedCornerShape(16.dp), // Esquinas redondeadas
            elevation = CardDefaults.cardElevation(8.dp), // Sombra para dar profundidad
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface) // Color de fondo de la tarjeta
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp) // Relleno dentro de la tarjeta
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally, // Centra los elementos horizontalmente
                verticalArrangement = Arrangement.spacedBy(16.dp) // Espacio entre los elementos de la columna
            ) {
                // Título de la pantalla
                Text(
                    text = "Iniciar Sesión",
                    style = MaterialTheme.typography.headlineMedium, // Estilo de texto más grande
                    color = MaterialTheme.colorScheme.primary, // Color del texto del tema
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Campo de texto para ingresar el correo electrónico
                OutlinedTextField( // Usa OutlinedTextField para un diseño más limpio
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo Electrónico") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary, // Color del borde cuando está enfocado
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline, // Color del borde cuando no está enfocado
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        cursorColor = MaterialTheme.colorScheme.primary
                    )
                )

                // Campo de texto para ingresar la contraseña
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it }, // Actualiza el estado cuando el usuario escribe
                    label = { Text("Contraseña") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(), // Oculta los caracteres ingresados
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), // Teclado específico para contraseña
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        cursorColor = MaterialTheme.colorScheme.primary
                    )
                )

                Spacer(modifier = Modifier.height(8.dp)) // Espacio antes del botón

                // Botón para iniciar sesión
                Button(
                    onClick = {
                        // Llama a Firebase Authentication para iniciar sesión con correo y contraseña
                        auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener { result ->
                                if (result.isSuccessful) {
                                    // Si la autenticación fue exitosa, navega a la pantalla "home_main_graph"
                                    navController.navigate("home_main_graph") {
                                        // Elimina "login" del backstack para que no se pueda volver atrás con "back"
                                        popUpTo("login") { inclusive = true }
                                    }
                                } else {
                                    // Si hubo un error, muestra el mensaje en pantalla
                                    error = "Autenticación fallida: ${result.exception?.localizedMessage}"
                                }
                            }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary, // Color de fondo del botón
                        contentColor = MaterialTheme.colorScheme.onPrimary // Color del texto del botón
                    ),
                    contentPadding = PaddingValues(vertical = 12.dp) // Relleno del contenido del botón
                ) {
                    Text("Iniciar sesión", style = MaterialTheme.typography.titleMedium) // Texto del botón
                }

                // Si hay un mensaje de error, lo muestra en pantalla
                error?.let {
                    Text(
                        it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 8.dp)
                    ) // Muestra el error en color rojo
                }

                // Nuevo botón para ir a RegisterScreen
                TextButton(onClick = { navController.navigate("register") }) {
                    Text(
                        "¿No tienes cuenta? Regístrate aquí",
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}