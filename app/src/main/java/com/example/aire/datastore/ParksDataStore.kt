package com.example.aire.datastore

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.example.aire.model.Parque

private val Context.dataStore by preferencesDataStore(name = "parques_prefs")

class ParqueDataStore(private val context: Context) {

    private fun stringKey(name: String) = stringPreferencesKey(name)
    private fun intKey(name: String) = intPreferencesKey(name)

    private val dataStore = context.dataStore

    // Lista fija predefinida
    private val PARQUES_INICIALES = listOf(
        Parque("Parque Nacional Torres del Paine", -73.4080, -50.9423, 42),
        Parque("Parque Metropolitano de Santiago", -70.6340, -33.4378, 30),
        Parque("Parque Nacional Conguillío", -71.7886, -38.6736, 55),
        Parque("Parque Urbano Forestal", -70.6533, -33.4420, 25)
    )

    init {
        CoroutineScope(Dispatchers.IO).launch {
            val current = parques.firstOrNull()
            if (current == null || current.isEmpty()) {
                saveParques(PARQUES_INICIALES)
            }
        }
    }

    suspend fun saveParques(lista: List<Parque>) {
        dataStore.edit { prefs ->
            prefs.clear()
            prefs[intKey("count")] = lista.size
            lista.forEachIndexed { index, parque ->
                prefs[stringKey("nombre_$index")] = parque.nombre
                prefs[stringKey("coordX_$index")] = parque.coordenadaX.toString()
                prefs[stringKey("coordY_$index")] = parque.coordenadaY.toString()
                prefs[intKey("calidad_$index")] = parque.calidadAire
            }
        }
    }

    val parques: Flow<List<Parque>> = dataStore.data.map { prefs ->
        val count = prefs[intKey("count")] ?: 0
        (0 until count).mapNotNull { i ->
            val nombre = prefs[stringKey("nombre_$i")] ?: return@mapNotNull null
            val coordXStr = prefs[stringKey("coordX_$i")] ?: return@mapNotNull null
            val coordYStr = prefs[stringKey("coordY_$i")] ?: return@mapNotNull null
            val calidad = prefs[intKey("calidad_$i")] ?: return@mapNotNull null

            val coordX = coordXStr.toDoubleOrNull() ?: 0.0
            val coordY = coordYStr.toDoubleOrNull() ?: 0.0

            Parque(nombre, coordX, coordY, calidad)
        }
    }
}
