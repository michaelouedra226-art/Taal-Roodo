package com.example.presentation.shield

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Launch
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.VpnLock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ShieldState
import com.example.ui.theme.AmberSecondary
import com.example.ui.theme.CrimsonAlert
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.GoldPrimary

@Composable
fun ShieldScreen(
    shieldState: ShieldState,
    onToggleVpn: () -> Unit,
    onToggleAccessibility: () -> Unit,
    onToggleSafeSearch: () -> Unit,
    onToggleNsfwBlur: () -> Unit,
    onToggleFortress: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            // Top Shield Banner Status
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (shieldState.isVpnActive) DarkSurfaceElevated else Color(0xFF200F0F)
                ),
                shape = RoundedCornerShape(22.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.5.dp,
                        if (shieldState.isVpnActive) GoldPrimary.copy(alpha = 0.7f) else CrimsonAlert,
                        RoundedCornerShape(22.dp)
                    )
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (shieldState.isVpnActive) GoldPrimary.copy(alpha = 0.2f)
                                        else CrimsonAlert.copy(alpha = 0.2f)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (shieldState.isVpnActive) Icons.Default.Security else Icons.Default.Block,
                                    contentDescription = null,
                                    tint = if (shieldState.isVpnActive) GoldPrimary else CrimsonAlert,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = if (shieldState.isVpnActive) "TR-Shield Actif" else "TR-Shield Suspendu",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (shieldState.isVpnActive) GoldPrimary else CrimsonAlert
                                )
                                Text(
                                    text = if (shieldState.isVpnActive) "Filtrage Cloudflare Family actif" else "Bouclier réseau désactivé",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.LightGray
                                )
                            }
                        }

                        Switch(
                            checked = shieldState.isVpnActive,
                            onCheckedChange = {
                                haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                                onToggleVpn()
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = GoldPrimary,
                                checkedTrackColor = Color(0xFF382900),
                                uncheckedThumbColor = Color.Gray,
                                uncheckedTrackColor = Color(0xFF262626)
                            ),
                            modifier = Modifier.testTag("shield_vpn_toggle")
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Shield metrics counter
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${shieldState.blockedDomainsCount}",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = GoldPrimary
                            )
                            Text(
                                text = "Interceptions réelles",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray
                            )
                        }
                        Box(modifier = Modifier.width(1.dp).height(30.dp).background(DarkBorder))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "1.1.1.3",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldSuccess
                            )
                            Text(
                                text = "DNS Sécurisé",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray
                            )
                        }
                        Box(modifier = Modifier.width(1.dp).height(30.dp).background(DarkBorder))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Local",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = AmberSecondary
                            )
                            Text(
                                text = "On-Device",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }

        item {
            Text(
                text = "Couches Défensives du Système",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        // Layer 1: VPN Local
        item {
            ShieldLayerRow(
                icon = Icons.Default.VpnLock,
                title = "2.1.1 Filtrage DNS VpnService",
                subtitle = "Redirige les requêtes de résolution DNS vers Cloudflare Family (1.1.1.3) pour filtrer les domaines adultes.",
                isChecked = shieldState.isVpnActive,
                onCheckedChange = { onToggleVpn() },
                tag = "layer_vpn"
            )
        }

        // Layer 2: Service d'Accessibilité NLP
        item {
            ShieldLayerRow(
                icon = Icons.Default.Policy,
                title = "2.1.2 Gardien Intra-App (Service d'Accessibilité)",
                subtitle = "Interception locale de mots-clés adultes (FR, EN, Mooré) dans Telegram, navigateurs et réseaux sociaux.",
                isChecked = shieldState.isAccessibilityActive,
                onCheckedChange = { onToggleAccessibility() },
                tag = "layer_accessibility"
            )
        }

        // Layer 3: SafeSearch
        item {
            ShieldLayerRow(
                icon = Icons.Default.Search,
                title = "SafeSearch Forcé & Moteurs de recherche",
                subtitle = "Maintien des paramètres de recherche stricte activés sur les moteurs partenaires.",
                isChecked = shieldState.isSafeSearchForced,
                onCheckedChange = { onToggleSafeSearch() },
                tag = "layer_safesearch"
            )
        }

        // Layer 4: NSFW Image Blur
        item {
            ShieldLayerRow(
                icon = Icons.Default.VisibilityOff,
                title = "2.1.3 Floutage On-Device des Contenus Explicites",
                subtitle = "Masquage visuel instantané et délai motivationnel de 30 secondes en cas de détection.",
                isChecked = shieldState.isNsfwBlurActive,
                onCheckedChange = { onToggleNsfwBlur() },
                tag = "layer_nsfw"
            )
        }

        // Fortress Mode Section
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        if (shieldState.isFortressLocked) CrimsonAlert else DarkBorder,
                        RoundedCornerShape(18.dp)
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = if (shieldState.isFortressLocked) CrimsonAlert else AmberSecondary,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Mode Forteresse (TR-Fortress)",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = if (shieldState.isFortressLocked) "Verrouillé. Déconnexion impossible sans temporisation de sécurité."
                                else "Protection maximale pour les plages nocturnes ou les moments de fatigue.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.LightGray
                            )
                        }
                    }

                    Switch(
                        checked = shieldState.isFortressLocked,
                        onCheckedChange = { onToggleFortress() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = CrimsonAlert,
                            checkedTrackColor = Color(0xFF401010)
                        ),
                        modifier = Modifier.testTag("fortress_switch")
                    )
                }
            }
        }

        // Android System Settings Navigation
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, DarkBorder, RoundedCornerShape(18.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Permissions & Services Système Android",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = GoldPrimary
                    )
                    Text(
                        text = "Pour que le filtrage intra-application fonctionne, activez le service TrAccessibilityService dans les paramètres d'accessibilité de votre appareil Android.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.LightGray,
                        modifier = Modifier.padding(vertical = 6.dp)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    OutlinedButton(
                        onClick = {
                            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            }
                            context.startActivity(intent)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Launch,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = GoldPrimary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Ouvrir Paramètres d'Accessibilité Android", color = GoldPrimary)
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

@Composable
private fun ShieldLayerRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    tag: String
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, DarkBorder, RoundedCornerShape(16.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(GoldPrimary.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = GoldPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.LightGray,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }

            Switch(
                checked = isChecked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = GoldPrimary,
                    checkedTrackColor = Color(0xFF382900)
                ),
                modifier = Modifier.testTag(tag)
            )
        }
    }
}
