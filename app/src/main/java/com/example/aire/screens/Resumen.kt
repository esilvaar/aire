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
import com.example.aire.navigation.Screen
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import androidx.compose.ui.graphics.Color as ComposeColor
import com.github.mikephil.charting.data.BarEntry
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Screen2() {
    var selectedPeriod by remember { mutableStateOf("Diario") }
    val context = LocalContext.current

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
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = null,
                        tint = ComposeColor.Black
                    )
                    Text(
                        "Resumen exposición",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = ComposeColor.Black
                    )
                }
            },
            navigationIcon = {
                IconButton(onClick = { }) {
                    Icon(Icons.Default.Search, contentDescription = "Buscar")
                }
            },
            actions = {
                IconButton(onClick = { }) {
                    Icon(Icons.Default.Person, contentDescription = "Perfil")
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
            // Period selector
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                items(listOf("Diario", "Semanal", "Mensual")) { period ->
                    FilterChip(
                        onClick = { selectedPeriod = period },
                        label = { Text(period) },
                        selected = selectedPeriod == period,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ComposeColor(0xFF6366F1),
                            selectedLabelColor = ComposeColor.White
                        )
                    )
                }
            }

            // Circular progress indicators
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CircularProgressIndicator(
                    progress = 0.75f,
                    color = ComposeColor(0xFF6366F1),
                    backgroundColor = ComposeColor(0xFFE5E7EB),
                    label = ""
                )
                CircularProgressIndicator(
                    progress = 0.45f,
                    color = ComposeColor(0xFFEF4444),
                    backgroundColor = ComposeColor(0xFFE5E7EB),
                    label = ""
                )
                CircularProgressIndicator(
                    progress = 0.90f,
                    color = ComposeColor(0xFF6366F1),
                    backgroundColor = ComposeColor(0xFFE5E7EB),
                    label = "90%"
                )
            }

            // Bar Chart
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(bottom = 24.dp),
                colors = CardDefaults.cardColors(containerColor = ComposeColor.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                AndroidView(
                    factory = {
                        BarChart(context).apply {
                            layoutParams = android.view.ViewGroup.LayoutParams(
                                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                                android.view.ViewGroup.LayoutParams.MATCH_PARENT
                            )

                            // Datos simulados para el gráfico
                            val entries1 = listOf(
                                BarEntry(0f, 30f),
                                BarEntry(1f, 40f),
                                BarEntry(2f, 35f),
                                BarEntry(3f, 50f),
                                BarEntry(4f, 45f),
                                BarEntry(5f, 38f),
                                BarEntry(6f, 42f),
                                BarEntry(7f, 55f),
                                BarEntry(8f, 48f),
                                BarEntry(9f, 52f)
                            )

                            val entries2 = listOf(
                                BarEntry(10f, 35f),
                                BarEntry(11f, 45f),
                                BarEntry(12f, 40f),
                                BarEntry(13f, 55f),
                                BarEntry(14f, 50f),
                                BarEntry(15f, 43f),
                                BarEntry(16f, 47f),
                                BarEntry(17f, 60f),
                                BarEntry(18f, 53f),
                                BarEntry(19f, 57f)
                            )

                            val dataSet1 = BarDataSet(entries1, "").apply {
                                color = ComposeColor(0xFFE5E7EB).toArgb()
                                setDrawValues(false)
                            }

                            val dataSet2 = BarDataSet(entries2, "").apply {
                                color = ComposeColor(0xFFEF4444).toArgb()
                                setDrawValues(false)
                            }

                            val data = BarData(dataSet1, dataSet2)
                            data.barWidth = 0.8f

                            this.data = data
                            description.isEnabled = false
                            legend.isEnabled = false

                            xAxis.apply {
                                position = XAxis.XAxisPosition.BOTTOM
                                setDrawGridLines(false)
                                setDrawAxisLine(false)
                                setDrawLabels(false)
                            }

                            axisLeft.apply {
                                setDrawGridLines(false)
                                setDrawAxisLine(false)
                                setDrawLabels(false)
                            }

                            axisRight.isEnabled = false
                            setTouchEnabled(false)
                            animateY(1000)
                        }
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                )
            }

            // Summary section
            Text(
                "Resumen",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Summary items
            SummaryItem(
                icon = Icons.Default.Info,
                title = "Temperatura",
                value = "•••••"
            )

            SummaryItem(
                icon = Icons.Default.Info,
                title = "Ruido",
                value = "••••••"
            )

            SummaryItem(
                icon = Icons.Default.Info,
                title = "Aire",
                value = "••••••"
            )

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

@Composable
fun SummaryItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        colors = CardDefaults.cardColors(containerColor = ComposeColor.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .padding(end = 12.dp),
                    tint = ComposeColor.Gray
                )
                Text(
                    text = title,
                    fontSize = 16.sp,
                    color = ComposeColor.Black
                )
            }
            Text(
                text = value,
                fontSize = 16.sp,
                color = ComposeColor.Gray
            )
        }
    }
}