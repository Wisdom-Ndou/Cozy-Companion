package com.example.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.ripple
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CompanionType
import com.example.ui.MainViewModel
import com.example.ui.companion.CompanionMoodState
import com.example.ui.companion.RoomView
import com.example.ui.security.AppLockScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onNavigateToJournal: () -> Unit,
    onNavigateToSupport: () -> Unit,
    onNavigateToCustomize: () -> Unit,
    onOpenRelapseReflection: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val userPrefs by viewModel.userPreferences.collectAsState()
    val companionUiState by viewModel.companionUiState.collectAsState()
    val customItems by viewModel.customItems.collectAsState()

    var showStrugglingDialog by remember { mutableStateOf(value = false) }
    var showSettingsDialog by remember { mutableStateOf(value = false) }

    val companionType = try {
        CompanionType.valueOf(userPrefs.companionType)
    } catch (_: Exception) {
        CompanionType.BEAR
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "WELCOME BACK",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 11.sp,
                                letterSpacing = 1.3.sp,
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.65f),
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${userPrefs.companionName}’s Nook",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontSize = 21.sp,
                                    fontStyle = FontStyle.Italic,
                                    fontFamily = FontFamily.Serif,
                                ),
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                            ) {
                                Text(
                                    text = when (companionUiState.moodState) {
                                        CompanionMoodState.SLEEPING -> "Resting"
                                        CompanionMoodState.WAKING -> "Waking up"
                                        CompanionMoodState.CALM -> "Calm & Present"
                                        CompanionMoodState.HAPPY -> "Happy"
                                        CompanionMoodState.EXCITED -> "Cheering"
                                        CompanionMoodState.CELEBRATING -> "Celebrating"
                                        CompanionMoodState.COMFORTING -> "With you"
                                        CompanionMoodState.PROUD -> "Proud"
                                    },
                                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 11.sp,
                                    ),
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                )
                            }
                        }
                    }
                },
                actions = {
                    // Points balance badge (Natural Tones Pill)
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        color = MaterialTheme.colorScheme.surface,
                        shadowElevation = 1.dp,
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { onNavigateToCustomize() }
                            .testTag("home_points_badge"),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Points",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(15.dp),
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = userPrefs.points.toString(),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    IconButton(
                        onClick = { showSettingsDialog = true },
                        modifier = Modifier.testTag("home_settings_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings & Privacy",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            // Speech Bubble from Companion (Warm Sand & Subtle Outline)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "“${companionUiState.currentMessage}”",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontStyle = FontStyle.Italic,
                            lineHeight = 20.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // The Cozy Room & Companion View (Tappable area)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(28.dp))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(bounded = true, color = MaterialTheme.colorScheme.primary)
                    ) {
                        viewModel.onCompanionTap()
                    }
                    .testTag("cozy_room_tappable_area"),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                RoomView(
                    companionType = companionType,
                    moodState = companionUiState.moodState,
                    equippedItems = customItems,
                    bounceTrigger = companionUiState.bounceTrigger
                )
            }

            // Natural Tones Companion Status & Subtitle
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "${userPrefs.companionName.uppercase()} IS ${
                    when (companionUiState.moodState) {
                        CompanionMoodState.SLEEPING -> "RESTING PEACEFULLY"
                        CompanionMoodState.WAKING -> "STRETCHING & WAKING"
                        CompanionMoodState.CALM -> "PEACEFUL & MINDFUL"
                        CompanionMoodState.HAPPY -> "HAPPY TO SEE YOU"
                        CompanionMoodState.EXCITED -> "CHEERING YOU ON"
                        CompanionMoodState.CELEBRATING -> "CELEBRATING WITH YOU"
                        CompanionMoodState.COMFORTING -> "HOLDING SPACE FOR YOU"
                        CompanionMoodState.PROUD -> "DEEPLY PROUD OF YOU"
                    }
                }",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 11.5.sp,
                    letterSpacing = 1.2.sp,
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.65f)
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = when (companionUiState.moodState) {
                    CompanionMoodState.SLEEPING -> "TAP ROOM TO WAKE UP"
                    CompanionMoodState.WAKING -> "KEEP TAPPING TO SAY HELLO"
                    else -> "PET COMPANION & BREATHE TOGETHER"
                },
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 0.8.sp,
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Large Central Interaction Button (Primary CTA for Urge Relief)
            Button(
                onClick = { viewModel.onCompanionTap() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("companion_tap_button"),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                )
            ) {
                Icon(
                    imageVector = Icons.Default.TouchApp,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = when (companionUiState.moodState) {
                        CompanionMoodState.SLEEPING -> "Tap to Wake ${userPrefs.companionName}"
                        CompanionMoodState.WAKING -> "Keep Tapping to Say Hello"
                        CompanionMoodState.CALM -> "Pet Companion & Breathe"
                        CompanionMoodState.HAPPY -> "Connect & Stay with Me"
                        CompanionMoodState.EXCITED -> "Stay Strong Together! (+pts)"
                        CompanionMoodState.CELEBRATING -> "Celebrating Your Strength!"
                        CompanionMoodState.COMFORTING -> "I'm Right Beside You"
                        CompanionMoodState.PROUD -> "You Did So Well"
                    },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Quick Support Row: "I'm Struggling" / "Urge Help" button & "Had a Slip?"
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = {
                        viewModel.onStrugglingClicked()
                        showStrugglingDialog = true
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .testTag("home_struggling_button"),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = Color.White
                    )
                ) {
                    Icon(imageVector = Icons.Default.Spa, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Urge Help", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onOpenRelapseReflection,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .testTag("home_reflection_button"),
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Icon(imageVector = Icons.Default.Healing, contentDescription = null, modifier = Modifier.size(18.dp), tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Had a Slip?", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // Quick Action Navigation Cards
            Text(
                text = "Sanctuary Spaces",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickToolCard(
                    title = "Calm Tools",
                    subtitle = "Breathing & Urge Wave",
                    icon = Icons.Default.Spa,
                    onClick = onNavigateToSupport,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
                QuickToolCard(
                    title = "Private Journal",
                    subtitle = "Safe Device-Only Notes",
                    icon = Icons.Default.Book,
                    onClick = onNavigateToJournal,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickToolCard(
                    title = "Wardrobe & Room",
                    subtitle = "Decorate with earned points",
                    icon = Icons.Default.Checkroom,
                    onClick = onNavigateToCustomize,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }

    // "I'm Struggling" Quick Comfort Dialog
    if (showStrugglingDialog) {
        AlertDialog(
            onDismissRequest = { showStrugglingDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Favorite, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("We're In This Together")
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Take a slow breath. What would feel most supportive to you right now?",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Button(
                        onClick = {
                            showStrugglingDialog = false
                            onNavigateToSupport()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Spa, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Breathing & 5-Min Wave Timer")
                    }

                    OutlinedButton(
                        onClick = {
                            showStrugglingDialog = false
                            onNavigateToJournal()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Book, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Write What You're Feeling")
                    }

                    OutlinedButton(
                        onClick = {
                            showStrugglingDialog = false
                            // Stay with companion and tap
                            viewModel.onCompanionTap()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.TouchApp, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Just Sit & Tap with Companion")
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showStrugglingDialog = false }) {
                    Text("I'm Okay Now")
                }
            }
        )
    }

    // Settings & Privacy Dialog
    if (showSettingsDialog) {
        SettingsDialog(
            userPrefs = userPrefs,
            viewModel = viewModel,
        ) { showSettingsDialog = false }
    }
}

@Composable
fun QuickToolCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 11.5.sp,
                        lineHeight = 15.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f),
                    maxLines = 2,
                    minLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun SettingsDialog(
    userPrefs: com.example.data.preferences.UserPreferences,
    viewModel: MainViewModel,
    onDismiss: () -> Unit
) {
    var nameInput by remember { mutableStateOf(userPrefs.companionName) }
    var appLockEnabled by remember { mutableStateOf(userPrefs.appLockEnabled) }
    var notifsEnabled by remember { mutableStateOf(userPrefs.notificationsEnabled) }
    var showResetConfirm by remember { mutableStateOf(value = false) }
    var showPinSetupDialog by remember { mutableStateOf(false) }
    val uriHandler = LocalUriHandler.current

    if (showPinSetupDialog) {
        androidx.compose.ui.window.Dialog(
            onDismissRequest = { showPinSetupDialog = false },
            properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)
        ) {
            AppLockScreen(
                correctPin = "",
                title = "Set Safe Haven PIN",
                subtitle = "Choose a 4-digit PIN for your journal",
                isSetupMode = true,
                onPinSet = { newPin ->
                    viewModel.setAppLock(newPin, true)
                    appLockEnabled = true
                    showPinSetupDialog = false
                },
                onDismiss = {
                    showPinSetupDialog = false
                },
                onUnlocked = {},
            )
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Settings & Safe Space") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Companion Name
                androidx.compose.material3.OutlinedTextField(
                    value = nameInput,
                    onValueChange = { nameInput = it },
                    label = { Text("Companion Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // App Lock Toggle & Setup
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("App Lock PIN", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                            Text("Protect private journal with PIN", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(
                            checked = appLockEnabled,
                            onCheckedChange = { enabled ->
                                if (enabled) {
                                    if (userPrefs.appLockPin.length == 4) {
                                        appLockEnabled = true
                                        viewModel.setAppLock(userPrefs.appLockPin, true)
                                    } else {
                                        showPinSetupDialog = true
                                    }
                                } else {
                                    appLockEnabled = false
                                    viewModel.setAppLock(userPrefs.appLockPin, false)
                                }
                            }
                        )
                    }

                    if (appLockEnabled) {
                        TextButton(
                            onClick = { showPinSetupDialog = true },
                            modifier = Modifier.align(Alignment.Start)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (userPrefs.appLockPin.isEmpty()) "Set 4-Digit PIN" else "Change 4-Digit PIN",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                // Gentle Reminders Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Gentle Check-ins", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        Text("Encouraging daily notifications", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Switch(
                        checked = notifsEnabled,
                        onCheckedChange = {
                            notifsEnabled = it
                            viewModel.setNotificationsEnabled(it)
                        }
                    )
                }

                // Privacy Note
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("100% Local Privacy", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Your journal entries and urges are stored on this device only and never uploaded to any cloud.",
                            fontSize = 11.sp,
                            lineHeight = 15.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Support the Creator Link
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.clickable {
                        try {
                            uriHandler.openUri("https://buymeacoffee.com/The_Happy_Corner")
                        } catch (_: Exception) {}
                    }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Support the Creator",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Text(
                                    text = "buymeacoffee.com/The_Happy_Corner",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                )
                            }
                        }
                        Icon(
                            imageVector = Icons.Default.OpenInNew,
                            contentDescription = "Open Link",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // Clear Data Button
                OutlinedButton(
                    onClick = { showResetConfirm = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Clear All Data")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (nameInput.isNotBlank()) {
                        val currentType = try {
                            CompanionType.valueOf(userPrefs.companionType)
                        } catch (_: Exception) {
                            CompanionType.BEAR
                        }
                        viewModel.updateCompanionSettings(nameInput, currentType)
                    }
                    onDismiss()
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )

    if (showResetConfirm) {
        AlertDialog(
            onDismissRequest = { showResetConfirm = false },
            title = { Text("Reset Everything?") },
            text = { Text("This will erase all journal reflections and reset items to start fresh.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.clearAllData()
                        showResetConfirm = false
                        onDismiss()
                    }
                ) {
                    Text("Erase Everything", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetConfirm = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
