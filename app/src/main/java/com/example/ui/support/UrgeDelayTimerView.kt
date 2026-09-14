package com.example.ui.support

import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds
import java.util.Locale

@Composable
fun UrgeDelayTimerView(
    onUrgeVictory: (durationMinutes: Int, points: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val totalSeconds = 300 // 5 minutes
    var secondsRemaining by remember { mutableIntStateOf(totalSeconds) }
    var isRunning by remember { mutableStateOf(value = false) }

    val comfortingTips = remember {
        listOf(
            "Urges are like ocean waves. They rise, reach a peak, and naturally fade away.",
            "You don't need to fight this feeling. You can just let it exist while you wait with me.",
            "Every second you pause is physically reshaping your habit pathways.",
            "Unclench your jaw. Drop your shoulders away from your ears. Take a soft breath.",
            "You don't have to decide about tomorrow. Just stay here for these 5 minutes.",
            "You are in control of your hands and your feet, even when cravings shout.",
            "I'm sitting right here with you. We have plenty of time.",
        )
    }

    val currentTipIndex = ((totalSeconds - secondsRemaining) / 45) % comfortingTips.size
    val currentTip = comfortingTips[currentTipIndex]

    LaunchedEffect(isRunning) {
        while (isRunning && (secondsRemaining > 0)) {
            delay(1000L.milliseconds)
            secondsRemaining--
        }
        if (secondsRemaining == 0) {
            isRunning = false
        }
    }

    val minutes = secondsRemaining / 60
    val seconds = secondsRemaining % 60
    val timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    val progress = (totalSeconds - secondsRemaining).toFloat() / totalSeconds.toFloat()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "5-Minute Urge Delay",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = "Create space between the impulse and the action",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Timer Arc
        Box(
            modifier = Modifier.size(200.dp),
            contentAlignment = Alignment.Center,
        ) {
            Canvas(modifier = Modifier.size(200.dp)) {
                val strokeWidth = 12f
                val diameter = size.width - strokeWidth
                val topLeft = Offset(strokeWidth / 2, strokeWidth / 2)
                val arcSize = Size(diameter, diameter)

                // Background track
                drawArc(
                    color = Color(0x22E07A5F),
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth),
                )

                // Active progress arc
                drawArc(
                    color = Color(0xFFE07A5F),
                    startAngle = -90f,
                    sweepAngle = 360f * progress,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = timeFormatted,
                    fontWeight = FontWeight.Bold,
                    fontSize = 36.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Text(
                    text = if (secondsRemaining == 0) "Wave Surfed!" else if (isRunning) "Surfing the wave" else "Ready to pause",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Rotating comforting tip
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
            shape = RoundedCornerShape(16.dp),
        ) {
            Text(
                text = "“$currentTip”",
                fontSize = 13.sp,
                lineHeight = 19.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Timer Controls
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Button(
                onClick = { isRunning = !isRunning },
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.testTag("urge_timer_toggle"),
            ) {
                Icon(
                    imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = null,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (isRunning) "Pause Timer" else if (secondsRemaining < totalSeconds) "Resume" else "Start 5 Min Pause")
            }

            if (secondsRemaining < totalSeconds) {
                IconButton(
                    onClick = {
                        isRunning = false
                        secondsRemaining = totalSeconds
                    },
                ) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = "Reset")
                }
            }
        }

        // Victory recording button (enabled if user paused for at least 1 minute or completed)
        val elapsedMinutes = ((totalSeconds - secondsRemaining) / 60).coerceAtLeast(1)
        if (secondsRemaining <= 180) {
            Spacer(modifier = Modifier.height(18.dp))
            Button(
                onClick = {
                    onUrgeVictory(elapsedMinutes, 50)
                    isRunning = false
                    secondsRemaining = totalSeconds
                },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.testTag("record_urge_victory_button"),
            ) {
                Icon(imageVector = Icons.Default.Celebration, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("I Waited It Out! Record Victory (+50 pts)")
            }
        }
    }
}
