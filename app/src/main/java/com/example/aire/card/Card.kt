package com.example.aire.card

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
    val (colorFondo, descripcion) = when (parque.calidadAire) {
        in 0..50 -> Color(0xFFB9F6CA) to "Buena"
        in 51..100 -> Color(0xFFFFFF8D) to "Moderada"
        in 101..150 -> Color(0xFFFFAB91) to "Dañina"
        else -> Color(0xFFFF8A80) to "Peligrosa"
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
                    .background(colorFondo)
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
                    contentDescription = null,
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
                        text = "ICA° US",
                        fontSize = 12.sp,
                        color = Color.DarkGray
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = descripcion,
                        fontSize = 18.sp,
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text("🌡 ${parque.temperatura}°", fontSize = 14.sp)
                Text("💧 ${parque.humedad}%", fontSize = 14.sp)
                Text("🌬 ${parque.viento} km/h", fontSize = 14.sp)
            }
        }
    }
}
