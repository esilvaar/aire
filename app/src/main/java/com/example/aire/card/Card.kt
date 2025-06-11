package com.example.aire.card

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aire.model.Parque

@Composable
fun ParqueCard(
    parque: Parque,
    onToggleFavorito: () -> Unit
) {
    val (colorFondo, descripcion, icono, colorIcono) = when (parque.calidadAire) {
        in 0..50 -> Quadruple(
            Color(0xFFB9F6CA),
            "Buena",
            Icons.Filled.WbSunny,
            Color(0xFF4CAF50)
        )
        in 51..100 -> Quadruple(
            Color(0xFFFFFF8D),
            "Moderada",
            Icons.Filled.Cloud,
            Color(0xFFFF9800)
        )
        in 101..150 -> Quadruple(
            Color(0xFFFFAB91),
            "Dañina",
            Icons.Filled.Warning,
            Color(0xFFFF5722)
        )
        else -> Quadruple(
            Color(0xFFFF8A80),
            "Peligrosa",
            Icons.Filled.Dangerous,
            Color(0xFFD32F2F)
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .heightIn(min = 140.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .background(colorFondo)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, start = 16.dp, end = 16.dp, bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = parque.nombre,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Icon(
                    imageVector = if (parque.favorito) Icons.Filled.Favorite else Icons.Outlined.Favorite,
                    contentDescription = "Favorito",
                    tint = if (parque.favorito) Color.Red else Color.Gray,
                    modifier = Modifier.clickable { onToggleFavorito() }
                )
            }

            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "${parque.calidadAire}",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "ICA°",
                        fontSize = 12.sp,
                        color = Color.DarkGray
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = icono,
                        contentDescription = "Calidad del aire: $descripcion",
                        tint = colorIcono,
                        modifier = Modifier.size(48.dp)
                    )
                    Text(
                        text = descripcion,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.DarkGray
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Temperatura: ${parque.temperatura}°C",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "PM2.5 | ${parque.calidadAire / 3} µg/m³",
                    fontSize = 12.sp,
                    modifier = Modifier
                        .background(Color.White)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
    }
}

// Helper data class to simulate destructuring of 4 values
data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
