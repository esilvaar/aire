package com.example.aire.datastore

import android.content.Context
import com.example.aire.model.Parque
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ParqueDataStore(context: Context) {
    private val _parques = MutableStateFlow(obtenerParquesPredefinidos())
    val parques: StateFlow<List<Parque>> = _parques

    private fun obtenerParquesPredefinidos(): List<Parque> {
        return listOf(
            Parque(
                nombre = "Parque Metropolitano",
                coordenadaX = -70.633,
                coordenadaY = -33.431,
                calidadAire = 85,
                temperatura = 16,
                humedad = 48,
                viento = 3
            ),
            Parque(
                nombre = "Parque Quinta Normal",
                coordenadaX = -70.681,
                coordenadaY = -33.437,
                calidadAire = 42,
                temperatura = 18,
                humedad = 50,
                viento = 4
            ),
            Parque(
                nombre = "Parque O'Higgins",
                coordenadaX = -70.660,
                coordenadaY = -33.460,
                calidadAire = 120,
                temperatura = 21,
                humedad = 45,
                viento = 6
            )
        )
    }
}
