package com.example.aire.model
//define la estructura de los datos de parque
data class Parque(
    val nombre: String,
    val coordenadaX: Double,
    val coordenadaY: Double,
    val calidadAire: Int,
    val temperatura: Int = 14,
    val humedad: Int = 49,
    val viento: Int = 0,
    val favorito: Boolean,
    val tipo: String
)