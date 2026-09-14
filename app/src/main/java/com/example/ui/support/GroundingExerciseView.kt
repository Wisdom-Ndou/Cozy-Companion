package com.example.ui.support

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.PanTool
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Yard
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class GroundingStep(
    val count: Int,
    val senseName: String,
    val prompt: String,
    val icon: ImageVector,
    val hints: List<String>,
)

@Composable
fun GroundingExerciseView(
    onComplete: (points: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val steps = remember {
        listOf(
            GroundingStep(
                count = 5,
                senseName = "See",
                prompt = "Look around you. Notice 5 things you can see right now.",
                icon = Icons.Default.Visibility,
                hints = listOf("A spot of sunlight", "A book on the table", "The texture of a wall", "Your own hands", "A plant or window"),
            ),
            GroundingStep(
                count = 4,
                senseName = "Touch",
                prompt = "Feel 4 things you can physically touch or sense with your body.",
                icon = Icons.Default.PanTool,
                hints = listOf("The fabric of your clothes", "The surface of your chair", "The cool air on your skin", "The weight of your phone"),
            ),
            GroundingStep(
                count = 3,
                senseName = "Hear",
                prompt = "Close your eyes for a moment. Listen for 3 distinct sounds.",
                icon = Icons.Default.Hearing,
                hints = listOf("Distant traffic or birds", "A hum of a fan or fridge", "Your own gentle breath"),
            ),
            GroundingStep(
                count = 2,
                senseName = "Smell",
                prompt = "Notice 2 scents in the air, or smell something nearby.",
                icon = Icons.Default.Yard,
                hints = listOf("Fresh air from a window", "Warm coffee or soap scent"),
            ),
            GroundingStep(
                count = 1,
                senseName = "Taste",
                prompt = "Notice 1 taste in your mouth, or take a sip of cool water.",
                icon = Icons.Default.Restaurant,
                hints = listOf("A lingering warm tea or mint", "A fresh sip of cool water"),
            ),
        )
    }

    var currentStepIndex by remember { mutableIntStateOf(0) }
    val checkedItems = remember { mutableStateListOf<Int>() }
    var isFinished by remember { mutableStateOf(value = false) }

    val currentStep = steps[currentStepIndex]
    val progress = (currentStepIndex + (if (isFinished) 1 else 0)) / 5f

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Progress header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "5-4-3-2-1 Grounding",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                text = if (!isFinished) "Step ${currentStepIndex + 1} of 5" else "Complete!",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (!isFinished) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    // Big round icon badge
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = currentStep.icon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(28.dp),
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "${currentStep.count} Things to ${currentStep.senseName}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = currentStep.prompt,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp,
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Helpful checklist items
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        currentStep.hints.take(currentStep.count).forEachIndexed { index, hint ->
                            val isChecked = checkedItems.contains(index)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        if (isChecked) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                                        else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                    )
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Checkbox(
                                    checked = isChecked,
                                    onCheckedChange = { checked ->
                                        if (checked) checkedItems.add(index) else checkedItems.remove(index)
                                    },
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = hint,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            checkedItems.clear()
                            if (currentStepIndex < (steps.size - 1)) {
                                currentStepIndex++
                            } else {
                                isFinished = true
                            }
                        },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("grounding_next_step"),
                    ) {
                        Text(if (currentStepIndex < (steps.size - 1)) "Next Step" else "Finish Grounding")
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null)
                    }
                }
            }
        } else {
            // Completion card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                shape = RoundedCornerShape(20.dp),
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(54.dp),
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "You are here. You are safe.",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "You re-anchored your mind in the present moment. That takes real strength.",
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f),
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = {
                            onComplete(40)
                            currentStepIndex = 0
                            isFinished = false
                        },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.testTag("claim_grounding_reward"),
                    ) {
                        Text("Claim +40 points")
                    }
                }
            }
        }
    }
}
