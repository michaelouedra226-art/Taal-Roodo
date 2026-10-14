package com.example.presentation.mind

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.BuildConfig
import com.example.data.model.ChatMessage
import com.example.data.model.DynamicCardType
import com.example.presentation.components.GenUiDynamicCard
import com.example.ui.theme.AmberSecondary
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.GoldPrimary

@Composable
fun MindCoachScreen(
    chatMessages: List<ChatMessage>,
    isLoading: Boolean,
    onSendMessage: (String) -> Unit,
    onDynamicActionTriggered: (String) -> Unit,
    currentCustomApiKey: String = "",
    onUpdateApiKey: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var inputText by remember { mutableStateOf("") }
    var isApiKeyDialogOpen by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val haptic = LocalHapticFeedback.current

    val quickPrompts = listOf(
        "Je ressens une envie pressante !",
        "Je me sens stressé et fatigué",
        "Donne-moi un proverbe en mooré",
        "Montre mes progrès de reconstruction",
        "Je veux faire une pause respiration"
    )

    LaunchedEffect(chatMessages.size) {
        if (chatMessages.isNotEmpty()) {
            listState.animateScrollToItem(chatMessages.size - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Coach Banner Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
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
                        .background(GoldPrimary.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Psychology,
                        contentDescription = null,
                        tint = GoldPrimary,
                        modifier = Modifier.size(26.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Agnes AI — Coach",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = GoldPrimary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(AmberSecondary.copy(alpha = 0.2f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "agnes-3.0-flash",
                                fontSize = 10.sp,
                                color = AmberSecondary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    val isApiConfigured = currentCustomApiKey.isNotBlank() || (BuildConfig.AGNES_API_KEY.isNotBlank() && BuildConfig.AGNES_API_KEY != "DEFAULT_KEY")
                    Text(
                        text = if (isApiConfigured)
                            "API Agnes active • Mooré & Français"
                        else
                            "Mode Sagesse On-Device • Hors-ligne",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isApiConfigured) EmeraldSuccess else Color.LightGray
                    )
                }
            }

            // Key settings button
            IconButton(
                onClick = { isApiKeyDialogOpen = true },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(DarkSurfaceElevated)
                    .testTag("open_api_key_dialog")
            ) {
                Icon(
                    imageVector = Icons.Default.Key,
                    contentDescription = "Configuration Clé API",
                    tint = GoldPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Messages list
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(chatMessages, key = { it.id }) { msg ->
                ChatBubbleItem(
                    message = msg,
                    onDynamicAction = onDynamicActionTriggered
                )
            }

            if (isLoading) {
                item {
                    Row(
                        modifier = Modifier.padding(start = 12.dp, top = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            color = GoldPrimary,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Agnes réfléchit avec sagesse...",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray
                        )
                    }
                }
            }
        }

        // Quick suggestions carousel
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(quickPrompts) { prompt ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(DarkSurfaceElevated)
                        .border(1.dp, GoldPrimary.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                        .clickable {
                            haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.TextHandleMove)
                            onSendMessage(prompt)
                        }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = GoldPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = prompt,
                            fontSize = 12.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Message Input Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                placeholder = { Text("Parle librement à Agnes...") },
                modifier = Modifier
                    .weight(1f)
                    .testTag("chat_input_field"),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = DarkSurfaceElevated,
                    unfocusedContainerColor = DarkSurfaceElevated,
                    focusedIndicatorColor = GoldPrimary,
                    unfocusedIndicatorColor = DarkBorder,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                shape = RoundedCornerShape(24.dp),
                maxLines = 3
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = {
                    if (inputText.isNotBlank()) {
                        haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                        onSendMessage(inputText)
                        inputText = ""
                    }
                },
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(GoldPrimary)
                    .testTag("chat_send_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Envoyer",
                    tint = Color.Black,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }

    if (isApiKeyDialogOpen) {
        AgnesApiKeyDialog(
            currentKey = currentCustomApiKey,
            onDismiss = { isApiKeyDialogOpen = false },
            onSave = { key ->
                onUpdateApiKey(key)
                isApiKeyDialogOpen = false
            }
        )
    }
}

@Composable
private fun AgnesApiKeyDialog(
    currentKey: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var keyInput by remember { mutableStateOf(currentKey) }
    val isStudioKeyAvailable = BuildConfig.AGNES_API_KEY.isNotBlank() && BuildConfig.AGNES_API_KEY != "DEFAULT_KEY"

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(DarkSurfaceElevated)
                .border(1.dp, GoldPrimary, RoundedCornerShape(20.dp))
                .padding(20.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Key,
                        contentDescription = null,
                        tint = GoldPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Configuration API Agnes",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = GoldPrimary
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "L'application prend en charge 2 méthodes pour connecter l'API Agnes AI (https://apihub.agnes-ai.com/v1) :",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.LightGray
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF1B1B1B))
                        .padding(10.dp)
                ) {
                    Column {
                        Text(
                            text = "1. Panneau Secrets de Google AI Studio :",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = AmberSecondary
                        )
                        Text(
                            text = "Définir la variable AGNES_API_KEY. Injectée automatiquement au build via BuildConfig.",
                            fontSize = 11.sp,
                            color = Color.LightGray
                        )
                        if (isStudioKeyAvailable) {
                            Text(
                                text = "✓ Clé détectée via Secrets AI Studio",
                                fontSize = 11.sp,
                                color = EmeraldSuccess,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "2. Saisie directe locale dans l'application :",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = AmberSecondary
                )

                OutlinedTextField(
                    value = keyInput,
                    onValueChange = { keyInput = it },
                    placeholder = { Text("Ex: agn_live_xxxxxxxxxx...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF161616),
                        unfocusedContainerColor = Color(0xFF161616),
                        focusedIndicatorColor = GoldPrimary,
                        unfocusedIndicatorColor = DarkBorder,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    singleLine = true
                )

                Text(
                    text = "Si aucune clé n'est fournie, Taal Roodo bascule automatiquement sur le moteur local de sagesse culturelle mooré et français, sans crash.",
                    fontSize = 11.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 6.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(onClick = onDismiss) {
                        Text("Annuler", color = Color.White)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { onSave(keyInput) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GoldPrimary,
                            contentColor = Color.Black
                        )
                    ) {
                        Text("Enregistrer", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun ChatBubbleItem(
    message: ChatMessage,
    onDynamicAction: (String) -> Unit
) {
    val isUser = message.isFromUser

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 320.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 18.dp,
                        topEnd = 18.dp,
                        bottomStart = if (isUser) 18.dp else 4.dp,
                        bottomEnd = if (isUser) 4.dp else 18.dp
                    )
                )
                .background(
                    if (isUser) Color(0xFF3B2E00) else DarkSurfaceElevated
                )
                .border(
                    1.dp,
                    if (isUser) GoldPrimary.copy(alpha = 0.5f) else DarkBorder,
                    RoundedCornerShape(
                        topStart = 18.dp,
                        topEnd = 18.dp,
                        bottomStart = if (isUser) 18.dp else 4.dp,
                        bottomEnd = if (isUser) 4.dp else 18.dp
                    )
                )
                .padding(14.dp)
        ) {
            Text(
                text = message.text,
                color = if (isUser) GoldPrimary else Color.White,
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 20.sp
            )
        }

        // GenUI Dynamic Widget embedded inside the chat flow
        if (message.dynamicCardType != DynamicCardType.NONE) {
            Spacer(modifier = Modifier.height(8.dp))
            GenUiDynamicCard(
                type = message.dynamicCardType,
                onActionTriggered = onDynamicAction,
                modifier = Modifier.widthIn(max = 330.dp)
            )
        }
    }
}
