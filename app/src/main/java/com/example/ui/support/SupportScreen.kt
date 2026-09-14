package com.example.ui.support

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.example.ui.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
    @Suppress("UNUSED_PARAMETER") onBackToHome: () -> Unit = {},
) {
    val context = LocalContext.current
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("Breathing", "Grounding", "Urge Wave", "Helplines")
    val tabIcons = listOf(
        Icons.Default.Spa,
        Icons.Default.SelfImprovement,
        Icons.Default.Timer,
        Icons.Default.HealthAndSafety,
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp),
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Calm & Immediate Support",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
            )
        },
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
        ) {
            // Tab Selector
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary,
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = title,
                                fontSize = 12.sp,
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                            )
                        },
                        icon = {
                            Icon(
                                imageVector = tabIcons[index],
                                contentDescription = title,
                                modifier = Modifier.size(18.dp),
                            )
                        },
                        modifier = Modifier.testTag("support_tab_$index"),
                    )
                }
            }

            // Scrollable Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
            ) {
                when (selectedTabIndex) {
                    0 -> BreathingExerciseView(
                        onComplete = { points ->
                            viewModel.recordUrgeVictory(
                                toolName = "Breathing Exercise",
                                durationMinutes = 3,
                                notes = "Completed mindful breathing exercise",
                                points = points,
                            )
                        },
                    )
                    1 -> GroundingExerciseView(
                        onComplete = { points ->
                            viewModel.recordUrgeVictory(
                                toolName = "5-4-3-2-1 Grounding",
                                durationMinutes = 4,
                                notes = "Grounding exercise to reconnect with physical senses",
                                points = points,
                            )
                        },
                    )
                    2 -> UrgeDelayTimerView(
                        onUrgeVictory = { durationMinutes, points ->
                            viewModel.recordUrgeVictory(
                                toolName = "5-Minute Urge Delay",
                                durationMinutes = durationMinutes,
                                notes = "Waited out the craving wave",
                                points = points,
                            )
                        },
                    )
                    3 -> CrisisSupportView(
                        onCallHotline = { number ->
                            val intent = Intent(Intent.ACTION_DIAL, "tel:$number".toUri())
                            context.startActivity(intent)
                        },
                        onSendSms = { number ->
                            val intent = Intent(Intent.ACTION_SENDTO, "smsto:$number".toUri())
                            context.startActivity(intent)
                        },
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun CrisisSupportView(
    onCallHotline: (String) -> Unit,
    onSendSms: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "You are never alone.",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "This companion app is a gentle emotional aid, not a clinical care provider. If you feel overwhelmed, in distress, or experiencing severe urges, compassionate counselors across South Africa are available 24/7.",
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.9f),
                )
            }
        }

        Text(
            text = "South African 24/7 Helplines & Resources",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onBackground,
        )

        // SADAG Suicide Crisis Helpline
        HelplineCard(
            name = "SADAG Suicide Crisis Helpline",
            description = "South Africa's nationwide 24/7 toll-free crisis helpline for emotional distress, mental health emergencies, and suicide prevention.",
            contactInfo = "Toll-Free: 0800 567 567 | SMS: 31393",
            buttonLabel = "Call 0800 567 567",
        ) { onCallHotline("0800567567") }

        // DSD / SADAG Substance Abuse Helpline
        HelplineCard(
            name = "DSD National Substance Abuse Helpline",
            description = "Dept. of Social Development & SADAG 24hr free guidance, telephonic counseling, and rehabilitation referrals across South Africa.",
            contactInfo = "Toll-Free: 0800 12 13 14 | SMS: 32312",
            buttonLabel = "Call 0800 12 13 14",
        ) { onCallHotline("0800121314") }

        // LifeLine South Africa
        HelplineCard(
            name = "LifeLine South Africa (24/7)",
            description = "National 24-hour crisis counseling, emotional support, and trauma intervention across all South African provinces.",
            contactInfo = "Call: 0861 322 322 (or 0800 150 150)",
            buttonLabel = "Call 0861 322 322",
        ) { onCallHotline("0861322322") }

        // SANCA Alcohol & Drug Support
        HelplineCard(
            name = "SANCA Alcohol & Drug Support",
            description = "South African National Council on Alcoholism & Drug Dependence. Specialist addiction guidance and rehabilitation assistance.",
            contactInfo = "Call: 086 14 72622 | WhatsApp: 076 535 1701",
            buttonLabel = "Call SANCA",
        ) { onCallHotline("0861472622") }

        // Cipla Mental Health Helpline
        HelplineCard(
            name = "Cipla Mental Health Helpline",
            description = "Free 24-hour telephonic mental wellness care and crisis counseling in partnership with SADAG.",
            contactInfo = "Toll-Free: 0800 456 789 | WhatsApp: 076 882 2775",
            buttonLabel = "Call 0800 456 789",
        ) { onCallHotline("0800456789") }

        // South African SMS & WhatsApp Support Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Free SMS & WhatsApp Support in SA",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "If you cannot speak on the phone, free text-based support is available:",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "• SMS 31393 — SADAG Mental Health Support\n• SMS 32312 — Substance Abuse Assistance\n• WhatsApp 087 163 2025 — Ke Moja Substance Abuse Chat\n• WhatsApp 076 882 2775 — Cipla SADAG Chat",
                    fontSize = 12.5.sp,
                    lineHeight = 18.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(20.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .clickable { onSendSms("31393") }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Sms,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "SMS 31393",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(20.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .clickable { onSendSms("32312") }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Sms,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "SMS 32312",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HelplineCard(
    name: String,
    description: String,
    contactInfo: String,
    buttonLabel: String,
    onAction: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f),
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable { onAction() }
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(16.dp),
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = buttonLabel,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = description,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = contactInfo,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}
