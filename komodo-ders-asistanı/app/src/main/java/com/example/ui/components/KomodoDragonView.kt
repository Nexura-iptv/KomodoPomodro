package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.KomodoEvolutions
import com.example.data.model.KomodoRank
import com.example.data.model.MascotState
import com.example.ui.theme.KomodoAmber
import com.example.ui.theme.KomodoCoral
import com.example.ui.theme.KomodoEmerald
import com.example.ui.theme.KomodoMint
import com.example.ui.theme.KomodoTeal
import com.example.ui.theme.KomodoTealDark
import kotlin.random.Random

@Composable
fun KomodoMascotHeader(
    totalMinutes: Int,
    mascotState: MascotState,
    activeSubject: String?,
    onOpenEvolutionDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentRank = KomodoEvolutions.getRankForMinutes(totalMinutes)
    val nextRank = KomodoEvolutions.getNextRank(totalMinutes)

    var currentQuoteIndex by remember { mutableStateOf(0) }
    val quote = remember(currentQuoteIndex, mascotState) {
        when (mascotState) {
            MascotState.BREAK_TIME -> KomodoEvolutions.breakQuotes[currentQuoteIndex % KomodoEvolutions.breakQuotes.size]
            else -> KomodoEvolutions.studyQuotes[currentQuoteIndex % KomodoEvolutions.studyQuotes.size]
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                currentQuoteIndex++
                onOpenEvolutionDialog()
            },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(68.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(KomodoMint.copy(alpha = 0.4f), KomodoTealDark)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        KomodoAvatarCanvas(
                            state = mascotState,
                            level = currentRank.level,
                            modifier = Modifier.size(54.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${currentRank.emoji} ${currentRank.title}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = KomodoEmerald.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = "Lv. ${currentRank.level}",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = KomodoEmerald,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Text(
                            text = when (mascotState) {
                                MascotState.FOCUSING -> "🔥 Odaklanıyor: ${activeSubject ?: "Ders Çalışma"}"
                                MascotState.BREAK_TIME -> "☕ Mola Zamanı: Dinlen ve yenilen!"
                                MascotState.CELEBRATING -> "🎉 Harika! Hedef tamamlandı!"
                                MascotState.IDLE -> "😴 Hazır ve motive bekliyor"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = when (mascotState) {
                                MascotState.FOCUSING -> KomodoAmber
                                MascotState.BREAK_TIME -> KomodoMint
                                else -> MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        )
                    }
                }

                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = "Komodo Rütbesi",
                            tint = KomodoAmber,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Speech bubble
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = null,
                        tint = KomodoAmber,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = quote,
                        style = MaterialTheme.typography.bodySmall,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            if (nextRank != null) {
                Spacer(modifier = Modifier.height(10.dp))
                val currentBase = currentRank.minMinutes
                val nextTarget = nextRank.minMinutes
                val progress = ((totalMinutes - currentBase).toFloat() / (nextTarget - currentBase).toFloat()).coerceIn(0f, 1f)
                val remainingMins = nextTarget - totalMinutes

                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Sonraki Rütbe: ${nextRank.emoji} ${nextRank.title}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "$remainingMins dk kaldı",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = KomodoMint
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(CircleShape),
                        color = KomodoEmerald,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun KomodoAvatarCanvas(
    state: MascotState,
    level: Int,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "mascot")
    
    // Breathing motion
    val breatheScale by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathe"
    )

    // Eye blinking
    val eyeHeightFactor by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "blink"
    )

    Canvas(
        modifier = modifier.scale(breatheScale)
    ) {
        val w = size.width
        val h = size.height

        // Dragon Head Base
        val headColor = Color(0xFF1B806A)
        val snoutColor = Color(0xFF25A186)
        val bellyColor = Color(0xFF70E000)
        val cheekColor = Color(0xFFFF758F)

        // Dragon Scales / Crest on top
        val crestColor = Color(0xFF0F4C3A)
        drawCircle(
            color = crestColor,
            radius = w * 0.12f,
            center = Offset(w * 0.35f, h * 0.22f)
        )
        drawCircle(
            color = crestColor,
            radius = w * 0.15f,
            center = Offset(w * 0.50f, h * 0.16f)
        )
        drawCircle(
            color = crestColor,
            radius = w * 0.12f,
            center = Offset(w * 0.65f, h * 0.22f)
        )

        // Head Oval
        drawRoundRect(
            color = headColor,
            topLeft = Offset(w * 0.20f, h * 0.25f),
            size = Size(w * 0.60f, h * 0.55f),
            cornerRadius = CornerRadius(w * 0.25f, h * 0.25f)
        )

        // Snout
        drawRoundRect(
            color = snoutColor,
            topLeft = Offset(w * 0.25f, h * 0.48f),
            size = Size(w * 0.50f, h * 0.35f),
            cornerRadius = CornerRadius(w * 0.18f, h * 0.18f)
        )

        // Nostrils
        drawCircle(
            color = Color(0xFF0B3B2C),
            radius = w * 0.03f,
            center = Offset(w * 0.40f, h * 0.62f)
        )
        drawCircle(
            color = Color(0xFF0B3B2C),
            radius = w * 0.03f,
            center = Offset(w * 0.60f, h * 0.62f)
        )

        // Cheeks
        drawCircle(
            color = cheekColor.copy(alpha = 0.6f),
            radius = w * 0.07f,
            center = Offset(w * 0.26f, h * 0.55f)
        )
        drawCircle(
            color = cheekColor.copy(alpha = 0.6f),
            radius = w * 0.07f,
            center = Offset(w * 0.74f, h * 0.55f)
        )

        // Eyes
        val eyeRadius = w * 0.07f
        val eyeH = eyeRadius * (if (state == MascotState.IDLE) 0.3f else eyeHeightFactor)

        // Left eye
        drawOval(
            color = Color.White,
            topLeft = Offset(w * 0.32f - eyeRadius, h * 0.40f - eyeH),
            size = Size(eyeRadius * 2, eyeH * 2)
        )
        drawOval(
            color = Color(0xFF0C1618),
            topLeft = Offset(w * 0.33f - eyeRadius * 0.6f, h * 0.40f - eyeH * 0.7f),
            size = Size(eyeRadius * 1.2f, eyeH * 1.4f)
        )
        // Pupil shine
        drawCircle(
            color = Color.White,
            radius = eyeRadius * 0.3f,
            center = Offset(w * 0.31f, h * 0.38f)
        )

        // Right eye
        drawOval(
            color = Color.White,
            topLeft = Offset(w * 0.68f - eyeRadius, h * 0.40f - eyeH),
            size = Size(eyeRadius * 2, eyeH * 2)
        )
        drawOval(
            color = Color(0xFF0C1618),
            topLeft = Offset(w * 0.67f - eyeRadius * 0.6f, h * 0.40f - eyeH * 0.7f),
            size = Size(eyeRadius * 1.2f, eyeH * 1.4f)
        )
        // Pupil shine
        drawCircle(
            color = Color.White,
            radius = eyeRadius * 0.3f,
            center = Offset(w * 0.66f, h * 0.38f)
        )

        // Glasses for Study Mode or Level >= 2
        if (state == MascotState.FOCUSING || level >= 2) {
            val glassColor = Color(0xFFFFBE0B)
            // Left lens
            drawCircle(
                color = glassColor,
                radius = eyeRadius * 1.35f,
                center = Offset(w * 0.32f, h * 0.40f),
                style = Stroke(width = 3.dp.toPx())
            )
            // Right lens
            drawCircle(
                color = glassColor,
                radius = eyeRadius * 1.35f,
                center = Offset(w * 0.68f, h * 0.40f),
                style = Stroke(width = 3.dp.toPx())
            )
            // Bridge
            drawLine(
                color = glassColor,
                start = Offset(w * 0.42f, h * 0.40f),
                end = Offset(w * 0.58f, h * 0.40f),
                strokeWidth = 3.dp.toPx()
            )
        }

        // Graduation Cap for High Rank (Level >= 5)
        if (level >= 5) {
            val capColor = Color(0xFF1F2421)
            val capPath = Path().apply {
                moveTo(w * 0.5f, h * 0.04f)
                lineTo(w * 0.85f, h * 0.16f)
                lineTo(w * 0.5f, h * 0.26f)
                lineTo(w * 0.15f, h * 0.16f)
                close()
            }
            drawPath(capPath, capColor)
            // Tassel
            drawLine(
                color = Color(0xFFFFD166),
                start = Offset(w * 0.5f, h * 0.15f),
                end = Offset(w * 0.82f, h * 0.30f),
                strokeWidth = 2.dp.toPx()
            )
        }

        // Smile / Mouth
        val smilePath = Path().apply {
            if (state == MascotState.FOCUSING) {
                // Determined focus line
                moveTo(w * 0.42f, h * 0.72f)
                lineTo(w * 0.58f, h * 0.72f)
            } else if (state == MascotState.BREAK_TIME || state == MascotState.CELEBRATING) {
                // Wide happy smile
                moveTo(w * 0.36f, h * 0.68f)
                quadraticTo(w * 0.50f, h * 0.80f, w * 0.64f, h * 0.68f)
            } else {
                // Calm sweet smile
                moveTo(w * 0.40f, h * 0.70f)
                quadraticTo(w * 0.50f, h * 0.76f, w * 0.60f, h * 0.70f)
            }
        }
        drawPath(
            path = smilePath,
            color = Color(0xFF0B3B2C),
            style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}
