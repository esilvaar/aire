package com.example.aire.card

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aire.model.Parque
//define l clase para almacenar la informacion necesaria para la card
data class CalidadAireVisual(
    val colorFondo: Color,
    val descripcion: String,
    val icono: ImageVector,
    val colorIcono: Color
)

@Composable
fun ParqueCard(
    parque: Parque,
    onToggleFavorito: () -> Unit
) {
    val visual = when (parque.calidadAire) {//define variables de color, icono,colores, descripcion, segun el criterio de calidad
        in 0..50 -> CalidadAireVisual(
            Color(0xFFB9F6CA),
            "Buena",
            Icons.Filled.WbSunny,
            Color(0xFF4CAF50)
        )
        in 51..100 -> CalidadAireVisual(
            Color(0xFFFFFF8D),
            "Moderada",
            Icons.Filled.Cloud,
            Color(0xFFFF9800)
        )
        in 101..150 -> CalidadAireVisual(
            Color(0xFFFFAB91),
            "Dañina",
            Icons.Filled.Warning,
            Color(0xFFFF5722)
        )
        else -> CalidadAireVisual(
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
                .background(visual.colorFondo)
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

                Icon(//define como se comportará segun la accion de favorito
                    imageVector = if (parque.favorito) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Favorito",
                    tint = if (parque.favorito) Color.Red else Color.Gray,
                    modifier = Modifier.clickable { onToggleFavorito() }//llama la funcion de home
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
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = "ICA°",
                        fontSize = 12.sp,
                        color = Color.DarkGray
                    )
                }
                Icon(
                    imageVector = visual.icono,
                    tint = visual.colorIcono,
                    modifier = Modifier.size(48.dp),
                    contentDescription = "Ícono de calidad del aire"
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.tertiary)
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = " ${visual.descripcion}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "PM2.5 | ${parque.calidadAire / 3} µg/m³",
                    fontSize = 12.sp,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.tertiary)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
    }
}
