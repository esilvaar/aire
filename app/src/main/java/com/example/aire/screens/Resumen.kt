package com.example.aire.screens

import android.graphics.Color
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.aire.datastore.ParqueDataStore
import com.example.aire.model.Parque
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import androidx.compose.ui.graphics.Color as ComposeColor
import com.github.mikephil.charting.data.BarEntry
import kotlinx.coroutines.flow.map
import kotlin.math.cos
import kotlin.math.sin

data class CircularProgressData(
    val progress: Float,
    val color: ComposeColor,
    val label: String,
    val title: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Screen2() {
    var selectedPeriod by remember { mutableStateOf("Todos") }
    val context = LocalContext.current
    val parqueDataStore = remember { ParqueDataStore(context) }

    // Obtener parques según el filtro seleccionado
    val parques = if (selectedPeriod == "Favoritos") {
        parqueDataStore.parques.map { lista -> lista.filter { it.favorito } }
    } else {
        parqueDataStore.parques
    }.collectAsState(initial = emptyList())

    // Generar datos de los gráficos circulares organizados por criterio
    val circularProgressDataByCriterio = remember(parques.value) {
        if (parques.value.isNotEmpty()) {
            mapOf(
                "Calidad del Aire" to parques.value.map { parque ->
                    CircularProgressData(
                        progress = (parque.calidadAire / 150f).coerceIn(0f, 1f),
                        color = when {
                            parque.calidadAire <= 50 -> ComposeColor(0xFF10B981)
                            parque.calidadAire <= 100 -> ComposeColor(0xFFF59E0B)
                            else -> ComposeColor(0xFFEF4444)
                        },
                        label = "${parque.calidadAire}",
                        title = parque.nombre
                    )
                },
                "Temperatura" to parques.value.map { parque ->
                    CircularProgressData(
                        progress = (parque.temperatura / 40f).coerceIn(0f, 1f),
                        color = ComposeColor(0xFF6366F1),
                        label = "${parque.temperatura}°C",
                        title = parque.nombre
                    )
                },
                "Humedad" to parques.value.map { parque ->
                    CircularProgressData(
                        progress = (parque.humedad / 100f).coerceIn(0f, 1f),
                        color = ComposeColor(0xFF06B6D4),
                        label = "${parque.humedad}%",
                        title = parque.nombre
                    )
                },
                "Viento" to parques.value.map { parque ->
                    CircularProgressData(
                        progress = (parque.viento / 20f).coerceIn(0f, 1f),
                        color = ComposeColor(0xFF8B5CF6),
                        label = "${parque.viento}",
                        title = parque.nombre
                    )
                }
            )
        } else {
            emptyMap()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ComposeColor(0xFFF5F5F5))
    ) {
        // Header
        TopAppBar(
            title = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Resumen Parques Registrados",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    // Period selector moved to header
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        FilterChip(
                            onClick = { selectedPeriod = "Todos" },
                            label = { Text("Todos", fontSize = 14.sp) },
                            selected = selectedPeriod == "Todos",
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.secondary,
                                selectedLabelColor = MaterialTheme.colorScheme.primary
                            )
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        FilterChip(
                            onClick = { selectedPeriod = "Favoritos" },
                            label = { Text("Favoritos", fontSize = 14.sp) },
                            selected = selectedPeriod == "Favoritos",
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.secondary,
                                selectedLabelColor = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = ComposeColor.White
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {

            // Circular progress indicators organizados por criterio
            if (circularProgressDataByCriterio.isNotEmpty()) {
                circularProgressDataByCriterio.forEach { (criterio, datosParques) ->
                    // Título del criterio
                    Text(
                        text = criterio,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ComposeColor.Black,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )

                    // Fila horizontal con todos los parques para este criterio
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp),
                        modifier = Modifier.padding(bottom = 24.dp)
                    ) {
                        items(datosParques) { data ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.width(100.dp)
                            ) {
                                CircularProgressIndicator(
                                    progress = data.progress,
                                    color = data.color,
                                    backgroundColor = ComposeColor(0xFFE5E7EB),
                                    label = data.label
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = data.title,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = ComposeColor.Gray,
                                    textAlign = TextAlign.Center,
                                    maxLines = 2
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(80.dp)) // Space for bottom navigation
        }
    }
}

@Composable
fun CircularProgressIndicator(
    progress: Float,
    color: ComposeColor,
    backgroundColor: ComposeColor,
    label: String,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(80.dp)
    ) {
        Canvas(modifier = Modifier.size(80.dp)) {
            val strokeWidth = 8.dp.toPx()
            val radius = (size.minDimension - strokeWidth) / 2

            // Background circle
            drawCircle(
                color = backgroundColor,
                radius = radius,
                style = Stroke(strokeWidth, cap = StrokeCap.Round)
            )

            // Progress arc
            val sweepAngle = 360 * progress
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(strokeWidth, cap = StrokeCap.Round),
                size = Size(radius * 2, radius * 2),
                topLeft = Offset(
                    (size.width - radius * 2) / 2,
                    (size.height - radius * 2) / 2
                )
            )
        }

        if (label.isNotEmpty()) {
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}