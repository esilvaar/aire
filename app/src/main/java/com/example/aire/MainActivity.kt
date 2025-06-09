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
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.IOException

// DataStore y clave de preferencia
val ComponentActivity.dataStore by preferencesDataStore(name = "settings")
val THEME_KEY = booleanPreferencesKey("dark_theme")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val scope = rememberCoroutineScope()

            // Leer el valor desde DataStore
            val darkThemeFlow: Flow<Boolean> = dataStore.data
                .catch { exception ->
                    if (exception is IOException) emit(emptyPreferences()) else throw exception
                }
                .map { it[THEME_KEY] ?: false }

            val isDarkTheme by darkThemeFlow.collectAsState(initial = false)

            // Aplicar el tema
            AireTheme(darkTheme = isDarkTheme) {
                MainScreen {
                    scope.launch {
                        dataStore.edit { prefs ->
                            val current = prefs[THEME_KEY] ?: false
                            prefs[THEME_KEY] = !current
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MainScreen(onToggleTheme: () -> Unit) {
    Nav(onToggleTheme = onToggleTheme)
}
