package com.example.aire.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.aire.model.Parque
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "parques_prefs")//define el nombre del archivo de preferencias

class ParqueDataStore(private val context: Context) {

    private val FAVORITOS_KEY = stringSetPreferencesKey("favoritos_parques")//define la clave de preferencia

    // Lista de parques predefinidos(privada)
    private fun obtenerParquesPredefinidos() = listOf(
        Parque(
            nombre = "Parque Metropolitano",
            coordenadaX = -70.633,
            coordenadaY = -33.431,
            calidadAire = 85,
            temperatura = 16,
            humedad = 48,
            viento = 3,
            favorito = false,
            tipo = "urbano"
        ),
        Parque(
            nombre = "Parque Quinta Normal",
            coordenadaX = -70.681,
            coordenadaY = -33.437,
            calidadAire = 42,
            temperatura = 18,
            humedad = 50,
            viento = 4,
            favorito = false,
            tipo = "nacional"
        ),
        Parque(
            nombre = "Parque O'Higgins",
            coordenadaX = -70.660,
            coordenadaY = -33.460,
            calidadAire = 120,
            temperatura = 21,
            humedad = 45,
            viento = 6,
            favorito = false,
            tipo="urbano"
        ),
        Parque(
            nombre = "Parque Quinta Normal 1",
            coordenadaX = -73.681,
            coordenadaY = -33.437,
            calidadAire = 42,
            temperatura = 18,
            humedad = 50,
            viento = 4,
            favorito = false,
            tipo = "urbano"
        ),
        Parque(
            nombre = "Parque Quinta Normal 2",
            coordenadaX = -75.681,
            coordenadaY = -33.437,
            calidadAire = 42,
            temperatura = 18,
            humedad = 50,
            viento = 4,
            favorito = false,
            tipo = "urbano"
        )
    )

    // Flow que emite la lista de parques con favoritos según prefs
    val parques: Flow<List<Parque>> = context.dataStore.data.map { preferences ->
        val favoritosSet = preferences[FAVORITOS_KEY] ?: emptySet()
        obtenerParquesPredefinidos().map { parque ->
            parque.copy(favorito = favoritosSet.contains(parque.nombre))
        }
    }

    // Cambia favorito guardando en DataStore
    suspend fun toggleFavorito(nombreParque: String) {
        context.dataStore.edit { preferences ->
            val actuales = preferences[FAVORITOS_KEY] ?: emptySet()
            val mutableSet = actuales.toMutableSet()
            if (mutableSet.contains(nombreParque)) {
                mutableSet.remove(nombreParque)
            } else {
                mutableSet.add(nombreParque)
            }
            preferences[FAVORITOS_KEY] = mutableSet
        }
    }
}
