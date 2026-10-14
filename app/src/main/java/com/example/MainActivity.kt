package com.example

import android.app.Activity
import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CrisisAlert
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.MilitaryTech
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.presentation.TaalRoodoViewModel
import com.example.presentation.mind.MindCoachScreen
import com.example.presentation.panic.PanicDialog
import com.example.presentation.path.PathScreen
import com.example.presentation.rise.RiseScreen
import com.example.presentation.shield.BlockedOverlayDialog
import com.example.presentation.shield.ShieldScreen
import com.example.ui.theme.CrimsonAlert
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.TaalRoodoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaalRoodoTheme {
                TaalRoodoApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaalRoodoApp(
    viewModel: TaalRoodoViewModel = viewModel()
) {
    val haptic = LocalHapticFeedback.current
    val context = LocalContext.current
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val shieldState by viewModel.shieldState.collectAsStateWithLifecycle()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val chatMessages by viewModel.chatMessages.collectAsStateWithLifecycle()
    val isChatLoading by viewModel.isChatLoading.collectAsStateWithLifecycle()
    val triggers by viewModel.triggers.collectAsStateWithLifecycle()
    val challenges by viewModel.challenges.collectAsStateWithLifecycle()
    val forumPosts by viewModel.forumPosts.collectAsStateWithLifecycle()
    val isPanicOpen by viewModel.isPanicOpen.collectAsStateWithLifecycle()
    val blockedOverlayText by viewModel.blockedOverlayText.collectAsStateWithLifecycle()

    val vpnLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.startVpnService()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = DarkBackground,
        contentColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_app_logo),
                            contentDescription = "Taal Roodo Logo",
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .border(1.dp, GoldPrimary, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        androidx.compose.foundation.layout.Column {
                            Text(
                                text = "Taal Roodo",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = GoldPrimary
                            )
                            Text(
                                text = "TR-Core • Reconstruction & Discipline",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }
                    }
                },
                actions = {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(CrimsonAlert.copy(alpha = 0.2f))
                            .border(1.dp, CrimsonAlert, RoundedCornerShape(14.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .testTag("top_bar_panic_button")
                    ) {
                        androidx.compose.material3.IconButton(
                            onClick = {
                                haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                                viewModel.openPanic()
                            },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Bouton Panique",
                                tint = CrimsonAlert,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBackground,
                    titleContentColor = GoldPrimary
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = DarkSurfaceElevated,
                contentColor = GoldPrimary,
                tonalElevation = 8.dp,
                windowInsets = WindowInsets.navigationBars
            ) {
                val navItems = listOf(
                    Triple(0, "Bouclier", Pair(Icons.Filled.Security, Icons.Outlined.Security)),
                    Triple(1, "Coach Agnes", Pair(Icons.Filled.Psychology, Icons.Outlined.Psychology)),
                    Triple(2, "Parcours", Pair(Icons.Filled.Timeline, Icons.Outlined.Timeline)),
                    Triple(3, "Redirection", Pair(Icons.Filled.MilitaryTech, Icons.Outlined.MilitaryTech))
                )

                navItems.forEach { (index, title, icons) ->
                    val isSelected = currentTab == index
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove)
                            viewModel.setTab(index)
                        },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) icons.first else icons.second,
                                contentDescription = title
                            )
                        },
                        label = {
                            Text(
                                text = title,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black,
                            selectedTextColor = GoldPrimary,
                            indicatorColor = GoldPrimary,
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray
                        ),
                        modifier = Modifier.testTag("nav_item_$index")
                    )
                }
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                    viewModel.openPanic()
                },
                containerColor = CrimsonAlert,
                contentColor = Color.White,
                icon = {
                    Icon(
                        imageVector = Icons.Default.CrisisAlert,
                        contentDescription = "Urgence",
                        modifier = Modifier.size(20.dp)
                    )
                },
                text = { Text("PANIQUE SOS", fontWeight = FontWeight.Bold, fontSize = 12.sp) },
                modifier = Modifier.testTag("fab_panic_button")
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = currentTab,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "screen_tab_switch"
            ) { tab ->
                when (tab) {
                    0 -> ShieldScreen(
                        shieldState = shieldState,
                        onToggleVpn = {
                            if (shieldState.isVpnActive) {
                                viewModel.stopVpnService()
                            } else {
                                val vpnIntent = VpnService.prepare(context)
                                if (vpnIntent != null) {
                                    vpnLauncher.launch(vpnIntent)
                                } else {
                                    viewModel.startVpnService()
                                }
                            }
                        },
                        onToggleAccessibility = {
                            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            }
                            context.startActivity(intent)
                        },
                        onToggleSafeSearch = { viewModel.toggleSafeSearch() },
                        onToggleNsfwBlur = { viewModel.toggleNsfwBlur() },
                        onToggleFortress = { viewModel.toggleFortress() }
                    )
                    1 -> MindCoachScreen(
                        chatMessages = chatMessages,
                        isLoading = isChatLoading,
                        onSendMessage = { prompt -> viewModel.sendChatMessage(prompt) },
                        currentCustomApiKey = userProfile.customAgnesApiKey,
                        onUpdateApiKey = { key -> viewModel.updateCustomAgnesApiKey(key) },
                        onDynamicActionTriggered = { action ->
                            when (action) {
                                "OPEN_BREATHING" -> viewModel.openPanic()
                                "ACTIVATE_FORTRESS" -> viewModel.toggleFortress()
                                "COMPLETE_PHYSICAL" -> {
                                    val physicalChallenge = challenges.firstOrNull { it.category == "Physique" }
                                    if (physicalChallenge != null) {
                                        viewModel.completeChallenge(physicalChallenge)
                                    }
                                }
                                "REFLECT_PROVERB" -> {
                                    viewModel.sendChatMessage("Explique-moi ce proverbe plus en détail.")
                                }
                            }
                        }
                    )
                    2 -> PathScreen(
                        userProfile = userProfile,
                        triggers = triggers,
                        onLogTrigger = { mood, stress, loc, notes, resisted ->
                            viewModel.logTrigger(mood, stress, loc, notes, resisted)
                        },
                        onResetStreak = { viewModel.resetStreakWithLearning() }
                    )
                    3 -> RiseScreen(
                        challenges = challenges,
                        forumPosts = forumPosts,
                        onCompleteChallenge = { challenge -> viewModel.completeChallenge(challenge) },
                        onAddForumPost = { alias, content, tag -> viewModel.addForumPost(alias, content, tag) },
                        onLikePost = { postId -> viewModel.likePost(postId) }
                    )
                }
            }

            if (blockedOverlayText != null) {
                BlockedOverlayDialog(
                    warningMessage = blockedOverlayText!!,
                    onDismiss = { viewModel.dismissBlockedOverlay() },
                    onOpenPanic = { viewModel.openPanic() }
                )
            }

            if (isPanicOpen) {
                PanicDialog(
                    onDismiss = { viewModel.closePanic() },
                    emergencyContactName = userProfile.emergencyContactName,
                    emergencyContactPhone = userProfile.emergencyContactPhone,
                    onUrgeResisted = {
                        viewModel.logTrigger(
                            mood = "Urgence Surmontée",
                            stressLevel = 9,
                            location = "Protocole Panique",
                            notes = "Victoire remportée avec succès.",
                            resisted = true
                        )
                    }
                )
            }
        }
    }
}
