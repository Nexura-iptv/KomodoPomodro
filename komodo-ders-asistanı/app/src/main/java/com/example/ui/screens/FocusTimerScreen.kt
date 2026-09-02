package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.SmartDisplay
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MascotState
import com.example.data.model.Subject
import com.example.ui.components.KomodoMascotHeader
import com.example.ui.theme.KomodoAmber
import com.example.ui.theme.KomodoCoral
import com.example.ui.theme.KomodoEmerald
import com.example.ui.theme.KomodoMint
import com.example.ui.theme.KomodoTeal
import com.example.ui.theme.KomodoTealDark
import com.example.ui.viewmodel.FocusViewModel
import com.example.ui.viewmodel.SessionPhase
import com.example.ui.viewmodel.TimerPreset
import com.example.util.AmbientSound
import java.util.Locale

@Composable
fun FocusTimerScreen(
    viewModel: FocusViewModel,
    onNavigateToChannels: (Subject?) -> Unit,
    onOpenEvolutionDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    val timerState by viewModel.timerState.collectAsState()
    val totalFocusMins by viewModel.totalFocusMinutes.collectAsState()
    val mascotState by viewModel.mascotState.collectAsState()

    var showCustomTimerDialog by remember { mutableStateOf(false) }
    var showSoundDialog by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Mascot Banner Card
        KomodoMascotHeader(
            totalMinutes = totalFocusMins,
            mascotState = mascotState,
            activeSubject = timerState.selectedSubject.displayName,
            onOpenEvolutionDialog = onOpenEvolutionDialog
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Subject Selector Chips
        Text(
            text = "Çalışılacak Ders",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 2.dp)
        ) {
            items(Subject.entries) { subject ->
                val isSelected = timerState.selectedSubject == subject
                FilterChip(
                    selected = isSelected,
                    onClick = { viewModel.setSelectedSubject(subject) },
                    label = {
                        Text(
                            text = subject.displayName,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = subject.color.copy(alpha = 0.25f),
                        selectedLabelColor = if (isSelected) subject.color else MaterialTheme.colorScheme.onSurface
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = isSelected,
                        borderColor = if (isSelected) subject.color else MaterialTheme.colorScheme.outlineVariant
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Large Circular Focus Timer
        CircularTimerDisplay(
            timeRemainingSeconds = timerState.timeRemainingSeconds,
            totalPhaseSeconds = timerState.totalPhaseSeconds,
            phase = timerState.phase,
            isRunning = timerState.isRunning,
            subject = timerState.selectedSubject,
            onStartClick = { viewModel.startTimer() },
            onPauseClick = { viewModel.pauseTimer() },
            onResetClick = { viewModel.resetTimer() }
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Preset Selector Row
        Text(
            text = "Odaklanma Modları",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(TimerPreset.entries) { preset ->
                val isSelected = timerState.preset == preset
                Card(
                    modifier = Modifier
                        .clickable {
                            if (preset == TimerPreset.CUSTOM) {
                                showCustomTimerDialog = true
                            } else {
                                viewModel.setTimerPreset(preset)
                            }
                        },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected)
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ),
                    border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary) else null
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = preset.title,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = preset.description,
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 11.sp,
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Question Tracker & Ambient Sound Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Solved Questions Card
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = KomodoEmerald,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Çözülen Soru",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "${timerState.solvedQuestionsInSession}",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = KomodoEmerald
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FilledTonalButton(
                            onClick = { viewModel.addSolvedQuestions(-1) },
                            modifier = Modifier.size(36.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text("-1", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                        FilledTonalButton(
                            onClick = { viewModel.addSolvedQuestions(1) },
                            modifier = Modifier.size(36.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text("+1", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                        FilledTonalButton(
                            onClick = { viewModel.addSolvedQuestions(5) },
                            modifier = Modifier.size(36.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text("+5", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }

            // Ambient Sound Card
            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable { showSoundDialog = true },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (timerState.ambientSound != AmbientSound.NONE)
                        KomodoTeal.copy(alpha = 0.2f)
                    else
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ),
                border = if (timerState.ambientSound != AmbientSound.NONE)
                    androidx.compose.foundation.BorderStroke(1.5.dp, KomodoMint)
                else null
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Headphones,
                            contentDescription = null,
                            tint = KomodoMint,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Odak Sesi",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = timerState.ambientSound.icon,
                        fontSize = 32.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = timerState.ambientSound.displayName,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Değiştirmek için dokun",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Direct Video Channels Link Button for active subject
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onNavigateToChannels(timerState.selectedSubject) },
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = timerState.selectedSubject.color.copy(alpha = 0.15f)
            ),
            border = androidx.compose.foundation.BorderStroke(1.dp, timerState.selectedSubject.color.copy(alpha = 0.4f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFFFF0000),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.SmartDisplay,
                                contentDescription = "Video Dersler",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "${timerState.selectedSubject.displayName} Video Kanalları",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "YouTube & Google'da ders anlatımlarını izle",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = timerState.selectedSubject.color.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = "Aç",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = timerState.selectedSubject.color,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    // Custom Timer Dialog
    if (showCustomTimerDialog) {
        var focusMinutesInput by remember { mutableFloatStateOf(30f) }
        var breakMinutesInput by remember { mutableFloatStateOf(5f) }

        AlertDialog(
            onDismissRequest = { showCustomTimerDialog = false },
            title = {
                Text(
                    text = "Özel Odaklanma Süresi Ayarla",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = "Ders Çalışma Süresi: ${focusMinutesInput.toInt()} dakika",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Slider(
                        value = focusMinutesInput,
                        onValueChange = { focusMinutesInput = it },
                        valueRange = 5f..180f,
                        steps = 34
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Mola Süresi: ${breakMinutesInput.toInt()} dakika",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Slider(
                        value = breakMinutesInput,
                        onValueChange = { breakMinutesInput = it },
                        valueRange = 2f..30f,
                        steps = 13
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.setTimerPreset(
                            TimerPreset.CUSTOM,
                            customFocusMins = focusMinutesInput.toInt(),
                            customBreakMins = breakMinutesInput.toInt()
                        )
                        showCustomTimerDialog = false
                    }
                ) {
                    Text("Uygula")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCustomTimerDialog = false }) {
                    Text("İptal")
                }
            }
        )
    }

    // Ambient Sound Selection Dialog
    if (showSoundDialog) {
        AlertDialog(
            onDismissRequest = { showSoundDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Headphones,
                        contentDescription = null,
                        tint = KomodoMint
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Odaklanma Ambiyans Sesi", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AmbientSound.entries.forEach { sound ->
                        val isSelected = timerState.ambientSound == sound
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.setAmbientSound(sound)
                                    showSoundDialog = false
                                },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected)
                                    MaterialTheme.colorScheme.primaryContainer
                                else
                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = sound.icon, fontSize = 24.sp)
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = sound.displayName,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showSoundDialog = false }) {
                    Text("Kapat")
                }
            }
        )
    }

    // Celebration Dialog when session finishes
    if (timerState.showCompletionCelebration) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissCelebration() },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🎉 Tebrikler! Seans Tamamlandı", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "🐲 Komodo seninle gurur duyuyor!",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = KomodoMint
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${timerState.preset.focusMinutes} dakikalık ${timerState.selectedSubject.displayName} odaklanma seansını başarıyla bitirdin. Şimdi ☕ ${timerState.preset.breakMinutes} dakikalık mola zamanı!",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                    if (timerState.solvedQuestionsInSession > 0) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "✨ Bu seansta ${timerState.solvedQuestionsInSession} soru çözdün!",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = KomodoEmerald
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.dismissCelebration()
                        viewModel.startTimer() // start break timer
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = KomodoEmerald)
                ) {
                    Text("Molayı Başlat ☕")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.dismissCelebration() }) {
                    Text("Kapat")
                }
            }
        )
    }
}

@Composable
fun CircularTimerDisplay(
    timeRemainingSeconds: Int,
    totalPhaseSeconds: Int,
    phase: SessionPhase,
    isRunning: Boolean,
    subject: Subject,
    onStartClick: () -> Unit,
    onPauseClick: () -> Unit,
    onResetClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val progress = (timeRemainingSeconds.toFloat() / totalPhaseSeconds.toFloat().coerceAtLeast(1f)).coerceIn(0f, 1f)

    val minutes = timeRemainingSeconds / 60
    val seconds = timeRemainingSeconds % 60
    val formattedTime = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isRunning) 1.03f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    val ringColor = if (phase == SessionPhase.BREAK) KomodoMint else subject.color
    val trackColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)

    Box(
        modifier = modifier
            .size(280.dp)
            .scale(pulseScale),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 14.dp.toPx()
            val diameter = size.minDimension - strokeWidth
            val topLeft = Offset(strokeWidth / 2, strokeWidth / 2)
            val arcSize = Size(diameter, diameter)

            // Background Track
            drawArc(
                color = trackColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Progress Arc
            drawArc(
                brush = Brush.sweepGradient(
                    listOf(ringColor.copy(alpha = 0.7f), ringColor, ringColor.copy(alpha = 0.7f))
                ),
                startAngle = -90f,
                sweepAngle = 360f * progress,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (phase == SessionPhase.FOCUS)
                    subject.color.copy(alpha = 0.2f)
                else
                    KomodoMint.copy(alpha = 0.2f)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (phase == SessionPhase.FOCUS) Icons.Default.LocalFireDepartment else Icons.Default.Coffee,
                        contentDescription = null,
                        tint = if (phase == SessionPhase.FOCUS) subject.color else KomodoMint,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (phase == SessionPhase.FOCUS) "ODAK" else "MOLA",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (phase == SessionPhase.FOCUS) subject.color else KomodoMint
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = formattedTime,
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Black,
                fontSize = 52.sp,
                color = MaterialTheme.colorScheme.onSurface,
                letterSpacing = 2.sp
            )

            Text(
                text = if (phase == SessionPhase.FOCUS) subject.displayName else "Dinlenme Vakti",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Reset Button
                IconButton(
                    onClick = onResetClick,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Sıfırla",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Play / Pause Button
                Button(
                    onClick = {
                        if (isRunning) onPauseClick() else onStartClick()
                    },
                    modifier = Modifier
                        .height(50.dp)
                        .width(100.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isRunning) KomodoCoral else ringColor
                    )
                ) {
                    Icon(
                        imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isRunning) "Durdur" else "Başlat",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isRunning) "Dur" else "Başla",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
