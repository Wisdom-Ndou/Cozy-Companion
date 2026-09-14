package com.example.ui.support

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

enum class BreathingPattern(
    val title: String,
    @Suppress("unused") val description: String,
    val inhaleSeconds: Int,
    val holdSeconds: Int,
    val exhaleSeconds: Int,
    val postHoldSeconds: Int = 0,
) {
    RELAXING("4-4-6 Relaxing", "Calms the sympathetic nervous system", 4, 4, 6),
    DEEP_CALM("4-7-8 Deep Calm", "Powerful relaxation to release tension", 4, 7, 8),
    BOX("4-4-4-4 Box Breathing", "Used by professionals to restore focus", 4, 4, 4, 4)
}

enum class BreathPhase {
    INHALE,
    HOLD_IN,
    EXHALE,
    HOLD_OUT
}

@Composable
fun BreathingExerciseView(
    onComplete: (points: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedPattern by remember { mutableStateOf(BreathingPattern.RELAXING) }
    var isActive by remember { mutableStateOf(value = false) }
    var currentPhase by remember { mutableStateOf(BreathPhase.INHALE) }
    var secondsInPhase by remember { mutableIntStateOf(0) }
    var completedCycles by remember { mutableIntStateOf(0) }

    val circleScale = remember { Animatable(0.4f) }

    // Breathing animation & timer loop
    LaunchedEffect(isActive, selectedPattern) {
        if (!isActive) {
            circleScale.snapTo(0.45f)
            return@LaunchedEffect
        }

        while (isActive) {
            // 1. Inhale
            currentPhase = BreathPhase.INHALE
            secondsInPhase = selectedPattern.inhaleSeconds
            circleScale.animateTo(
                targetValue = 1.0f,
                animationSpec = tween(
                    durationMillis = selectedPattern.inhaleSeconds * 1000,
                    easing = LinearEasing,
                ),
            )

            if (!isActive) break

            // 2. Hold in
            if (selectedPattern.holdSeconds > 0) {
                currentPhase = BreathPhase.HOLD_IN
                secondsInPhase = selectedPattern.holdSeconds
                delay((selectedPattern.holdSeconds * 1000L).milliseconds)
            }

            if (!isActive) break

            // 3. Exhale
            currentPhase = BreathPhase.EXHALE
            secondsInPhase = selectedPattern.exhaleSeconds
            circleScale.animateTo(
                targetValue = 0.45f,
                animationSpec = tween(
                    durationMillis = selectedPattern.exhaleSeconds * 1000,
                    easing = LinearEasing,
                ),
            )

            if (!isActive) break

            // 4. Hold out (if box breathing)
            if (selectedPattern.postHoldSeconds > 0) {
                currentPhase = BreathPhase.HOLD_OUT
                secondsInPhase = selectedPattern.postHoldSeconds
                delay((selectedPattern.postHoldSeconds * 1000L).milliseconds)
            }

            completedCycles++
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Pattern Selector Chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            BreathingPattern.entries.forEach { pattern ->
                val isSelected = pattern == selectedPattern
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (isSelected) MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.surfaceVariant,
                        )
                        .clickable {
                            if (!isActive) {
                                selectedPattern = pattern
                            }
                        }
                        .padding(vertical = 10.dp, horizontal = 4.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = pattern.title,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                        else MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Large Animated Breathing Circle
        Box(
            modifier = Modifier
                .size(240.dp)
                .padding(12.dp),
            contentAlignment = Alignment.Center,
        ) {
            Canvas(modifier = Modifier.size(240.dp)) {
                val center = Offset(size.width / 2f, size.height / 2f)
                val baseRadius = (size.width / 2f) - 12f

                // Outer guide ring
                drawCircle(
                    color = Color(0x2281B29A),
                    radius = baseRadius,
                    center = center,
                    style = Stroke(width = 3f),
                )

                // Expanding breathing orb
                val animatedRadius = baseRadius * circleScale.value
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xAAE07A5F),
                            Color(0x77F4A261),
                            Color(0x3381B29A),
                        ),
                        center = center,
                        radius = animatedRadius,
                    ),
                    radius = animatedRadius,
                    center = center,
                )
                drawCircle(
                    color = Color(0xFFE07A5F),
                    radius = animatedRadius,
                    center = center,
                    style = Stroke(width = 2.5f),
                )
            }

            // Central instruction
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = when (currentPhase) {
                        BreathPhase.INHALE -> "Inhale..."
                        BreathPhase.HOLD_IN -> "Hold gently..."
                        BreathPhase.EXHALE -> "Exhale slowly..."
                        BreathPhase.HOLD_OUT -> "Rest..."
                    },
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                if (isActive) {
                    Text(
                        text = "Completed: $completedCycles",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp),
                    )
                } else {
                    Text(
                        text = "Ready when you are",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Play / Pause / Reset Controls
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Button(
                onClick = { isActive = !isActive },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                ),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.testTag("breathing_toggle_button"),
            ) {
                Icon(
                    imageVector = if (isActive) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isActive) "Pause" else "Start Breathing",
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (isActive) "Pause" else "Start Breathing")
            }

            if ((completedCycles > 0) || isActive) {
                IconButton(
                    onClick = {
                        isActive = false
                        completedCycles = 0
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Reset",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }

        // Completion reward button
        if (completedCycles >= 2) {
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                onClick = {
                    onComplete(40)
                    isActive = false
                    completedCycles = 0
                },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.testTag("claim_breathing_points"),
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Finished Breathing (+40 points)")
            }
        }
    }
}
