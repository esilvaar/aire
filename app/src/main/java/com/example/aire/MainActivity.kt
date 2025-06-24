package com.example.aire

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.aire.navigation.Screen // Importa Screen
import com.example.aire.ui.theme.AireTheme
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.navigation.compose.*
import com.example.aire.screens.LoginScreen
import com.example.aire.screens.RegisterScreen // Importa RegisterScreen
import com.example.aire.screens.Home
import com.example.aire.screens.Map
import com.example.aire.screens.Summary
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.IOException

// Crea una instancia de DataStore llamada "settings"
val ComponentActivity.dataStore by preferencesDataStore(name = "settings")
// Clave para la preferencia del tema: true para tema oscuro
val THEME_KEY = booleanPreferencesKey("dark_theme")

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

        setContent {
            val scope = rememberCoroutineScope()
            val darkThemeFlow: Flow<Boolean> = dataStore.data
                .catch { exception ->
                    if (exception is IOException) {
                        emit(emptyPreferences())
                    } else {
                        throw exception
                    }
                }
                .map { it[THEME_KEY] ?: false }

            val theme by darkThemeFlow.collectAsState(initial = false)

            AireTheme(darkTheme = theme) {
                val navController = rememberNavController()
                val user = auth.currentUser

                val onToggleTheme: () -> Unit = {
                    scope.launch {
                        dataStore.edit { prefs ->
                            val current = prefs[THEME_KEY] ?: false
                            prefs[THEME_KEY] = !current
                        }
                    }
                }

                // El único NavHost para toda la aplicación
                NavHost(
                    navController = navController,
                    startDestination = if (user != null) "home_main_graph" else "login"
                ) {
                    composable("login") {
                        LoginScreen(navController, auth)
                    }
                    composable("register") { // <--- AÑADE ESTA RUTA
                        RegisterScreen(navController, auth)
                    }
                    navigation(startDestination = Screen.Home.route, route = "home_main_graph") {
                        composable(Screen.Home.route) {
                            Home(navController = navController, auth = auth, onToggleTheme = onToggleTheme)
                        }
                        composable(Screen.Map.route) {
                            Map(navController = navController, auth = auth, onToggleTheme = onToggleTheme)
                        }
                        composable(Screen.Summary.route) {
                            Summary(navController = navController, auth = auth)
                        }
                    }
                }
            }
        }
    }
}