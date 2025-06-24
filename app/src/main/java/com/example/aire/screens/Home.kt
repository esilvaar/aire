package com.example.aire.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.navigation.NavController
import com.example.aire.card.ParqueCard
import com.example.aire.datastore.ParqueDataStore
import com.example.aire.navigation.Bottom
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@Composable
fun Home(
    navController: NavController,
    auth: FirebaseAuth,
    onToggleTheme: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val parqueDataStore = remember { ParqueDataStore(context) }
    val parques by parqueDataStore.parques.collectAsState(initial = emptyList())

    var showMenu by remember { mutableStateOf(false) }
    var mostrarFavoritos by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf(TextFieldValue("")) }

    val parquesFiltrados = remember(parques, mostrarFavoritos, searchText.text) {
        parques.filter { parque ->
            val matchesSearch = if (searchText.text.isBlank()) true
            else parque.nombre.contains(searchText.text, ignoreCase = true)

            val matchesFavorite = if (mostrarFavoritos) parque.favorito else true

            matchesSearch && matchesFavorite
        }
    }

    Scaffold(
        topBar = {
            CustomTopBar(
                showMenu = showMenu,
                onMenuClick = { showMenu = true },
                onDismissMenu = { showMenu = false },
                onToggleTheme = onToggleTheme,
                searchText = searchText,
                onSearchChange = { searchText = it },
                onLogout = {
                    auth.signOut()
                    navController.navigate("login") {
                        popUpTo("home_main_graph") { inclusive = true }
                    }
                },
                mostrarFavoritos = mostrarFavoritos,
                onTodosClick = { mostrarFavoritos = false },
                onFavoritosClick = { mostrarFavoritos = true }
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(bottom = 104.dp)
                ) {
                    items(parquesFiltrados) { parque ->
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
        },
        bottomBar = {
            Bottom(navController = navController)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopBar(
    showMenu: Boolean,
    onMenuClick: () -> Unit,
    onDismissMenu: () -> Unit,
    onToggleTheme: () -> Unit,
    searchText: TextFieldValue,
    onSearchChange: (TextFieldValue) -> Unit,
    onLogout: () -> Unit,
    mostrarFavoritos: Boolean,
    onTodosClick: () -> Unit,
    onFavoritosClick: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
        shadowElevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            // Primera fila: Menú, buscador, logout
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box {
                    IconButton(onClick = onMenuClick) {
                        Icon(
                            Icons.Default.Menu,
                            contentDescription = "Menú",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = onDismissMenu
                    ) {
                        DropdownMenuItem(
                            text = { Text("Cambiar tema", color = MaterialTheme.colorScheme.secondary) },
                            onClick = {
                                onDismissMenu()
                                onToggleTheme()
                            }
                        )
                    }
                }

                OutlinedTextField(
                    value = searchText,
                    onValueChange = onSearchChange,
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Buscar")
                    },
                    placeholder = { Text("Buscar parque...") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        unfocusedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        focusedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                        focusedLabelColor = MaterialTheme.colorScheme.secondary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        cursorColor = MaterialTheme.colorScheme.secondary,
                    )
                )

                IconButton(onClick = onLogout) {
                    Icon(
                        Icons.Default.Logout,
                        contentDescription = "Cerrar sesión",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Segunda fila: botones Todos y Favoritos
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onTodosClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (!mostrarFavoritos)
                            MaterialTheme.colorScheme.primary
                        else
                            Color.Transparent
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp)
                ) {
                    Text("Todos", color = MaterialTheme.colorScheme.secondary)
                }

                Button(
                    onClick = onFavoritosClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (mostrarFavoritos)
                            MaterialTheme.colorScheme.primary
                        else
                            Color.Transparent
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp)
                ) {
                    Text("Favoritos", color = MaterialTheme.colorScheme.secondary)
                }
            }
        }
    }
}

