package com.example.ui.support

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CompanionType
import com.example.data.model.CustomItem
import com.example.ui.companion.CompanionMoodState
import com.example.ui.companion.CompanionView

@Composable
fun RelapseReflectionScreen(
    companionName: String,
    companionType: CompanionType,
    equippedItems: List<CustomItem>,
    onDismiss: () -> Unit,
    onSaveReflection: (whatHappened: String, feelings: String, trigger: String, needsNow: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var whatHappened by remember { mutableStateOf("") }
    var feelings by remember { mutableStateOf("") }
    var trigger by remember { mutableStateOf("") }
    var needsNow by remember { mutableStateOf("") }
    var isSubmitted by remember { mutableStateOf(value = false) }

    // Immersive calming background
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF9F5F0)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Close button at top
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            // Comforting Companion View
            CompanionView(
                companionType = companionType,
                moodState = CompanionMoodState.COMFORTING,
                equippedItems = equippedItems,
                bounceTrigger = 0L,
                size = 140.dp,
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "It’s okay. I’m still here.",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "One difficult moment does not erase your courage, your growth, or all the times you showed up. Your little home and progress are safe. Take a deep breath.",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp,
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (!isSubmitted) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        Text(
                            text = "Gentle Reflection (All optional)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                        )

                        OutlinedTextField(
                            value = whatHappened,
                            onValueChange = { whatHappened = it },
                            label = { Text("What happened?") },
                            placeholder = { Text("Take your time, no judgment...") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("relapse_input_what_happened"),
                            shape = RoundedCornerShape(12.dp),
                        )

                        OutlinedTextField(
                            value = feelings,
                            onValueChange = { feelings = it },
                            label = { Text("What were you feeling beforehand?") },
                            placeholder = { Text("e.g. Overwhelmed, lonely, exhausted...") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("relapse_input_feelings"),
                            shape = RoundedCornerShape(12.dp),
                        )

                        OutlinedTextField(
                            value = trigger,
                            onValueChange = { trigger = it },
                            label = { Text("What do you think triggered it?") },
                            placeholder = { Text("e.g. Work stress, sudden news, habit cue...") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("relapse_input_trigger"),
                            shape = RoundedCornerShape(12.dp),
                        )

                        OutlinedTextField(
                            value = needsNow,
                            onValueChange = { needsNow = it },
                            label = { Text("What do you need right now?") },
                            placeholder = { Text("e.g. A cup of tea, rest, a walk, gentleness...") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("relapse_input_needs"),
                            shape = RoundedCornerShape(12.dp),
                        )

                        Button(
                            onClick = {
                                onSaveReflection(whatHappened, feelings, trigger, needsNow)
                                isSubmitted = true
                            },
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("relapse_save_reflection_button"),
                        ) {
                            Icon(imageVector = Icons.Default.Favorite, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Save Private Reflection")
                        }
                    }
                }
            } else {
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
                            imageVector = Icons.Default.Healing,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(48.dp),
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "We can try again together.",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "You learned something about yourself today. You had the bravery to pause and write it down. Tomorrow is another fresh chance, and $companionName is right beside you.",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.9f),
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp,
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(
                            onClick = onDismiss,
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.testTag("relapse_return_home_button"),
                        ) {
                            Text("Return Home")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
