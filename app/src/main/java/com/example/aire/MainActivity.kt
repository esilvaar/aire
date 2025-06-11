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


val ComponentActivity.dataStore by preferencesDataStore(name = "settings")//crea una instancia de datastore llamada settings
val THEME_KEY = booleanPreferencesKey("dark_theme")//clave para la preferencia de tema, en este caso true para oscuro

class MainActivity : ComponentActivity() {//define la clase MainActivity que hereda de ComponentActivity que es la clase base para jetpack compose
    override fun onCreate(savedInstanceState: Bundle?) {//llama al metodo onCreate cuando se crea
        super.onCreate(savedInstanceState)//llama al metodo onCreate de la clase padre

        setContent {//funcion principal para construir la interfaz de usuario
            val scope = rememberCoroutineScope()//crea una corrutina mientras la actividad no sea destruida o reconstruida
            val darkThemeFlow: Flow<Boolean> = dataStore.data//emite el estado actual del tema oscuro
                .catch { exception ->//manejo de excepciones
                    if (exception is IOException) emit(emptyPreferences()) else throw exception
                }
                .map { it[THEME_KEY] ?: false }//obtiene el valor actual de la preferencia de tema, por defecto claro
            val theme by darkThemeFlow.collectAsState(initial = false)//observa el darkThemeFlow y recompone el ui cuando cambia el valor

            AireTheme(darkTheme = theme) {//recibe el parametro de tema y aplica segun corresponda
                MainScreen {
                    scope.launch {//lanza la corutina
                        dataStore.edit { prefs ->//permite modificar preferencias
                            val current = prefs[THEME_KEY] ?: false//obtiene el valor actual de la preferencia de tema
                            prefs[THEME_KEY] = !current
                        }
                    }
                }
            }
        }
    }
}
//funcion que recibe una funcion sin parametros y valores de retorno, para cambiar el tema
@Composable
fun MainScreen(onToggleTheme: () -> Unit) {
    Nav(onToggleTheme = onToggleTheme)//llama a la funcion Nav que contiene la navegacion
}
