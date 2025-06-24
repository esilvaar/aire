package com.example.aire

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.aire.navigation.Nav
import com.example.aire.ui.theme.AireTheme
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.navigation.compose.*
import com.example.aire.screens.LoginScreen // Assuming this exists
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.IOException
import androidx.compose.material3.Button // Example for theme toggle
import androidx.compose.material3.Text // Example for theme toggle
import androidx.compose.foundation.layout.Column // Example for layout
import androidx.compose.foundation.layout.fillMaxSize // Example for layout
import androidx.compose.ui.Modifier // Example for layout
import androidx.navigation.NavController

// Creates a DataStore instance named "settings"
val ComponentActivity.dataStore by preferencesDataStore(name = "settings")
// Key for the theme preference: true for dark theme
val THEME_KEY = booleanPreferencesKey("dark_theme")

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth // Initializes auth as an instance of FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance() // Initializes auth with a FirebaseAuth instance

        setContent { // Main function to build the user interface
            val scope = rememberCoroutineScope() // Creates a coroutine scope tied to the activity's lifecycle
            val darkThemeFlow: Flow<Boolean> = dataStore.data // Emits the current dark theme state
                .catch { exception -> // Exception handling for DataStore
                    if (exception is IOException) {
                        emit(emptyPreferences())
                    } else {
                        throw exception
                    }
                }
                .map { it[THEME_KEY] ?: false } // Gets the current theme preference value, defaults to false (light)

            val theme by darkThemeFlow.collectAsState(initial = false) // Observes darkThemeFlow and recomposes UI when value changes

            // The AireTheme should wrap the entire content to apply the theme globally
            AireTheme(darkTheme = theme) {
                val navController = rememberNavController() // Creates and remembers a NavController instance
                val user = auth.currentUser // Gets the current user

                // Define the theme toggle action
                val onToggleTheme: () -> Unit = {
                    scope.launch { // Launches a coroutine to update preferences
                        dataStore.edit { prefs -> // Allows modifying preferences
                            val current = prefs[THEME_KEY] ?: false // Gets the current theme preference value
                            prefs[THEME_KEY] = !current // Toggles the theme
                        }
                    }
                }

                NavHost(navController = navController, startDestination = if (user != null) "home" else "login") {
                    composable("login") {
                        // Pass navController and auth to LoginScreen
                        LoginScreen(navController, auth)
                    }
                    composable("home") {
                        // Pass navController, auth, and the onToggleTheme lambda to MainScreen
                        MainScreen(navController = navController, auth = auth, onToggleTheme = onToggleTheme)
                    }
                }
            }
        }
    }
}

// Function that receives navController, auth, and a function to toggle the theme
@Composable
fun MainScreen(navController: NavController, auth: FirebaseAuth, onToggleTheme: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Example: A button to toggle the theme. You would place this in your actual UI.
        Button(onClick = onToggleTheme) {
            Text("Toggle Theme")
        }
        // Call the Nav composable, passing necessary parameters
        Nav(navController = navController, auth = auth, onToggleTheme = onToggleTheme)
    }
}