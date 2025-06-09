package com.example.aire.card

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aire.model.Parque

@Composable
fun ParqueCard(parque: Parque) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(16.dp)
        ) {
            // Sección izquierda: nombre y calidad de aire
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(end = 8.dp)
            ) {
                Text(text = parque.nombre, fontSize = 18.sp, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Calidad aire: ${parque.calidadAire}", fontSize = 14.sp)
            }

            // Sección derecha: coordenadas
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(start = 8.dp)
            ) {
                Text(text = "📍 Coordenadas:", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "X: ${String.format("%.4f", parque.coordenadaX)}", fontSize = 14.sp)
                Text(text = "Y: ${String.format("%.4f", parque.coordenadaY)}", fontSize = 14.sp)
            }
        }
    }
}
