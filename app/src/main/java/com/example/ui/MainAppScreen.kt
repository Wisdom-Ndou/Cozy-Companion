package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CompanionType
import com.example.ui.customize.CustomizeScreen
import com.example.ui.home.HomeScreen
import com.example.ui.journal.JournalScreen
import com.example.ui.onboarding.OnboardingScreen
import com.example.ui.progress.ProgressScreen
import com.example.ui.security.AppLockScreen
import com.example.ui.support.RelapseReflectionScreen
import com.example.ui.support.SupportScreen

enum class MainTab(val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    HOME("Home", Icons.Default.Home),
    SUPPORT("Support", Icons.Default.Spa),
    JOURNAL("Journal", Icons.Default.Book),
    WARDROBE("Wardrobe", Icons.Default.Checkroom),
    GROWTH("Growth", Icons.Default.TrendingUp)
}

@Composable
fun MainAppScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
) {
    val userPrefs by viewModel.userPreferences.collectAsState()
    val customItems by viewModel.customItems.collectAsState()

    var selectedTab by remember { mutableStateOf(MainTab.HOME) }
    var isUnlocked by remember(userPrefs.appLockEnabled) { mutableStateOf(!userPrefs.appLockEnabled) }
    var showRelapseReflection by remember { mutableStateOf(value = false) }

    val companionType = try {
        CompanionType.valueOf(userPrefs.companionType)
    } catch (_: Exception) {
        CompanionType.BEAR
    }

    // 1. Onboarding check
    if (!userPrefs.onboardingCompleted) {
        OnboardingScreen(
            onCompleteOnboarding = { name, type ->
                viewModel.completeOnboarding(name, type)
            },
        )
        return
    }

    // 2. App Lock check
    if (userPrefs.appLockEnabled && !isUnlocked) {
        AppLockScreen(
            correctPin = userPrefs.appLockPin,
            onUnlocked = { isUnlocked = true },
        )
        return
    }

    // 3. Main Navigation Experience
    Scaffold(
        bottomBar = {
            Surface(
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 4.dp,
            ) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.primary,
                    tonalElevation = 0.dp,
                ) {
                    MainTab.entries.forEach { tab ->
                        val isSelected = selectedTab == tab
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { selectedTab = tab },
                            icon = {
                                Icon(
                                    imageVector = tab.icon,
                                    contentDescription = tab.title,
                                )
                            },
                            label = {
                                Text(
                                    text = tab.title,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            ),
                            modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}"),
                        )
                    }
                }
            }
        },
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            when (selectedTab) {
                MainTab.HOME -> HomeScreen(
                    viewModel = viewModel,
                    onNavigateToJournal = { selectedTab = MainTab.JOURNAL },
                    onNavigateToSupport = { selectedTab = MainTab.SUPPORT },
                    onNavigateToCustomize = { selectedTab = MainTab.WARDROBE },
                    onOpenRelapseReflection = { showRelapseReflection = true },
                )
                MainTab.SUPPORT -> SupportScreen(
                    viewModel = viewModel,
                ) { selectedTab = MainTab.HOME }
                MainTab.JOURNAL -> JournalScreen(
                    viewModel = viewModel,
                )
                MainTab.WARDROBE -> CustomizeScreen(
                    viewModel = viewModel,
                )
                MainTab.GROWTH -> ProgressScreen(
                    viewModel = viewModel,
                )
            }

            // Full-screen non-punishment relapse reflection overlay
            AnimatedVisibility(
                visible = showRelapseReflection,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                RelapseReflectionScreen(
                    companionName = userPrefs.companionName,
                    companionType = companionType,
                    equippedItems = customItems,
                    onDismiss = { showRelapseReflection = false },
                    onSaveReflection = { whatHappened, feelings, trigger, needsNow ->
                        viewModel.recordRelapseReflection(whatHappened, feelings, trigger, needsNow)
                    },
                )
            }
        }
    }
}
