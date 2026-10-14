package com.example.data.model

data class ShieldState(
    val isVpnActive: Boolean = false,
    val isAccessibilityActive: Boolean = false,
    val isSafeSearchForced: Boolean = true,
    val isNsfwBlurActive: Boolean = true,
    val blockedDomainsCount: Int = 0,
    val isFortressLocked: Boolean = false,
    val lastBlockedKeyword: String? = null,
    val lastBlockedTime: Long? = null
)

enum class DynamicCardType {
    NONE,
    BREATHING,
    MOORE_PROVERB,
    PHYSICAL_CHALLENGE,
    PROGRESS_MILESTONE,
    FORTRESS_PROPOSAL
}

data class ChatMessage(
    val id: String,
    val isFromUser: Boolean,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val dynamicCardType: DynamicCardType = DynamicCardType.NONE,
    val dynamicPayload: String? = null
)
