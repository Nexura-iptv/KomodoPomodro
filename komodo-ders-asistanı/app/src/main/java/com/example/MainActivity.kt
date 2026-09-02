package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SmartDisplay
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material.icons.outlined.SmartDisplay
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.KomodoEvolutions
import com.example.data.model.Subject
import com.example.ui.screens.FocusTimerScreen
import com.example.ui.screens.KomodoMascotDialog
import com.example.ui.screens.StatsScreen
import com.example.ui.screens.TasksScreen
import com.example.ui.screens.VideoChannelsScreen
import com.example.ui.theme.KomodoAmber
import com.example.ui.theme.KomodoEmerald
import com.example.ui.theme.KomodoMint
import com.example.ui.theme.KomodoTeal
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.FocusViewModel
import com.example.util.IntentHelper

enum class MainTab(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    TIMER("Odaklan", Icons.Filled.Timer, Icons.Outlined.Timer),
    CHANNELS("Videolar", Icons.Filled.SmartDisplay, Icons.Outlined.SmartDisplay),
    TASKS("Görevler", Icons.Filled.Checklist, Icons.Outlined.Checklist),
    STATS("İstatistik", Icons.Filled.BarChart, Icons.Outlined.BarChart)
}

class MainActivity : ComponentActivity() {
    private val viewModel: FocusViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                KomodoFocusApp(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KomodoFocusApp(viewModel: FocusViewModel) {
    val context = LocalContext.current
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }
    var channelSubjectFilter by remember { mutableStateOf<Subject?>(null) }
    var showEvolutionDialog by remember { mutableStateOf(false) }

    val totalMinutes by viewModel.totalFocusMinutes.collectAsState()
    val currentRank = remember(totalMinutes) { KomodoEvolutions.getRankForMinutes(totalMinutes) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🐲 Komodo",
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp,
                            color = KomodoMint
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Odak",
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    // Evolution rank badge button
                    IconButton(onClick = { showEvolutionDialog = true }) {
                        Surface(
                            shape = CircleShape,
                            color = KomodoEmerald.copy(alpha = 0.2f),
                            modifier = Modifier.size(34.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = currentRank.emoji,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }

                    // Share button
                    IconButton(onClick = { IntentHelper.shareApp(context) }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Paylaş",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                MainTab.entries.forEachIndexed { index, tab ->
                    val isSelected = selectedTabIndex == index
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { selectedTabIndex = index },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                                contentDescription = tab.title
                            )
                        },
                        label = {
                            Text(
                                text = tab.title,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                }
            }
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Crossfade(targetState = selectedTabIndex, label = "tab_transition") { tabIndex ->
                when (MainTab.entries[tabIndex]) {
                    MainTab.TIMER -> {
                        FocusTimerScreen(
                            viewModel = viewModel,
                            onNavigateToChannels = { subject ->
                                channelSubjectFilter = subject
                                selectedTabIndex = MainTab.CHANNELS.ordinal
                            },
                            onOpenEvolutionDialog = { showEvolutionDialog = true }
                        )
                    }
                    MainTab.CHANNELS -> {
                        VideoChannelsScreen(
                            viewModel = viewModel,
                            initialSubjectFilter = channelSubjectFilter,
                            onStartFocusWithChannel = { channel ->
                                selectedTabIndex = MainTab.TIMER.ordinal
                            }
                        )
                    }
                    MainTab.TASKS -> {
                        TasksScreen(
                            viewModel = viewModel,
                            onStartFocusForTask = { subject, taskTitle ->
                                selectedTabIndex = MainTab.TIMER.ordinal
                            }
                        )
                    }
                    MainTab.STATS -> {
                        StatsScreen(viewModel = viewModel)
                    }
                }
            }
        }
    }

    if (showEvolutionDialog) {
        KomodoMascotDialog(
            totalMinutes = totalMinutes,
            onDismiss = { showEvolutionDialog = false }
        )
    }
}
