package com.example.presentation

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.api.AgnesApiClient
import com.example.data.local.AppDatabase
import com.example.data.model.ChallengeItem
import com.example.data.model.ChatMessage
import com.example.data.model.DynamicCardType
import com.example.data.model.ForumPost
import com.example.data.model.ShieldState
import com.example.data.model.TriggerEntry
import com.example.data.model.UserProfile
import com.example.service.TrAccessibilityService
import com.example.service.TrShieldVpnService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class TaalRoodoViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val triggerDao = db.triggerDao()
    private val challengeDao = db.challengeDao()
    private val forumDao = db.forumDao()
    private val profileDao = db.userProfileDao()

    private val agnesClient = AgnesApiClient(apiKey = null)

    // Shield State (Real counts and hardware service states)
    private val _shieldState = MutableStateFlow(
        ShieldState(
            isVpnActive = TrShieldVpnService.isRunning,
            isAccessibilityActive = TrAccessibilityService.isEnabled,
            isSafeSearchForced = true,
            isNsfwBlurActive = true,
            blockedDomainsCount = 0
        )
    )
    val shieldState: StateFlow<ShieldState> = _shieldState.asStateFlow()

    // Active Navigation Screen Tab (0: Shield, 1: Mind, 2: Path, 3: Rise)
    private val _currentTab = MutableStateFlow(0)
    val currentTab: StateFlow<Int> = _currentTab.asStateFlow()

    // Panic Dialog Modal
    private val _isPanicOpen = MutableStateFlow(false)
    val isPanicOpen: StateFlow<Boolean> = _isPanicOpen.asStateFlow()

    // Blocked Overlay (Real on-device interception)
    private val _blockedOverlayText = MutableStateFlow<String?>(null)
    val blockedOverlayText: StateFlow<String?> = _blockedOverlayText.asStateFlow()

    // Chat with Agnes AI (Real API endpoint)
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                id = UUID.randomUUID().toString(),
                isFromUser = false,
                text = "Ne y kãabo ! Bienvenue sur Taal Roodo (TR-Core). Je suis Agnes, ton coach de reconstruction numérique et personnelle. Pose tes questions ou décris ton état pour démarrer.",
                dynamicCardType = DynamicCardType.NONE
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isChatLoading = MutableStateFlow(false)
    val isChatLoading: StateFlow<Boolean> = _isChatLoading.asStateFlow()

    // Database reactive streams
    val triggers: StateFlow<List<TriggerEntry>> = triggerDao.getAllTriggers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val challenges: StateFlow<List<ChallengeItem>> = challengeDao.getAllChallenges()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val forumPosts: StateFlow<List<ForumPost>> = forumDao.getAllPosts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _userProfile = MutableStateFlow(
        UserProfile(
            id = 1,
            streakDays = 0,
            bestStreakDays = 0,
            emergencyContactName = "",
            emergencyContactPhone = "",
            preferredLanguage = "FR"
        )
    )
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val keywordReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == TrAccessibilityService.ACTION_KEYWORD_DETECTED) {
                val keyword = intent.getStringExtra(TrAccessibilityService.EXTRA_KEYWORD) ?: "terme explicite"
                handleRealKeywordInterception(keyword)
            }
        }
    }

    init {
        // Register receiver for real accessibility detection events
        val filter = IntentFilter(TrAccessibilityService.ACTION_KEYWORD_DETECTED)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            application.registerReceiver(keywordReceiver, filter, Context.RECEIVER_NOT_EXPORTED)
        } else {
            application.registerReceiver(keywordReceiver, filter)
        }

        initializeDatabase()
    }

    private fun initializeDatabase() {
        viewModelScope.launch {
            // Seed base daily challenges if database is empty
            val initialChallenges = listOf(
                ChallengeItem(
                    title = "20 Pompes de Rupture",
                    category = "Physique",
                    description = "Fais 20 pompes dès qu'une pensée parasite surgit. Force le sang vers tes muscles.",
                    durationMinutes = 3,
                    points = 20,
                    dateDay = "Aujourd'hui"
                ),
                ChallengeItem(
                    title = "Douche Froide / Eau au Visage",
                    category = "Physique",
                    description = "Lave ton visage avec de l'eau glacée pour activer le réflexe d'immersion et calmer le rythme cardiaque.",
                    durationMinutes = 2,
                    points = 15,
                    dateDay = "Aujourd'hui"
                ),
                ChallengeItem(
                    title = "Méditation d'Ancrage Mooré",
                    category = "Mental",
                    description = "Répète 10 fois : 'Ned yaa a menga ninsaal' en respirant en cohérence cardiaque.",
                    durationMinutes = 5,
                    points = 25,
                    dateDay = "Aujourd'hui"
                ),
                ChallengeItem(
                    title = "Message d'encouragement à un frère",
                    category = "Social",
                    description = "Envoie un message de bénédiction à un proche ou partage une victoire sur le forum.",
                    durationMinutes = 2,
                    points = 10,
                    dateDay = "Aujourd'hui"
                ),
                ChallengeItem(
                    title = "10 Minutes de Lecture Inspirante",
                    category = "Mental",
                    description = "Nourris ton cortex préfrontal de connaissances nobles avant de dormir.",
                    durationMinutes = 10,
                    points = 30,
                    dateDay = "Aujourd'hui"
                )
            )
            challengeDao.insertAll(initialChallenges)

            // Load saved user profile
            profileDao.getUserProfile().collect { profile ->
                if (profile != null) {
                    _userProfile.value = profile
                }
            }
        }
    }

    fun setTab(index: Int) {
        _currentTab.value = index
    }

    fun openPanic() {
        _isPanicOpen.value = true
    }

    fun closePanic() {
        _isPanicOpen.value = false
    }

    fun dismissBlockedOverlay() {
        _blockedOverlayText.value = null
    }

    // Real Shield Management
    fun startVpnService() {
        val app = getApplication<Application>()
        val intent = Intent(app, TrShieldVpnService::class.java).apply {
            action = TrShieldVpnService.ACTION_START
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            app.startForegroundService(intent)
        } else {
            app.startService(intent)
        }
        _shieldState.value = _shieldState.value.copy(isVpnActive = true)
    }

    fun stopVpnService() {
        val app = getApplication<Application>()
        val intent = Intent(app, TrShieldVpnService::class.java).apply {
            action = TrShieldVpnService.ACTION_STOP
        }
        app.startService(intent)
        _shieldState.value = _shieldState.value.copy(isVpnActive = false)
    }

    fun toggleSafeSearch() {
        _shieldState.value = _shieldState.value.copy(
            isSafeSearchForced = !_shieldState.value.isSafeSearchForced
        )
    }

    fun toggleNsfwBlur() {
        _shieldState.value = _shieldState.value.copy(
            isNsfwBlurActive = !_shieldState.value.isNsfwBlurActive
        )
    }

    fun toggleFortress() {
        val current = _shieldState.value.isFortressLocked
        _shieldState.value = _shieldState.value.copy(
            isFortressLocked = !current
        )
    }

    fun handleRealKeywordInterception(keyword: String) {
        val count = _shieldState.value.blockedDomainsCount + 1
        _shieldState.value = _shieldState.value.copy(
            blockedDomainsCount = count,
            lastBlockedKeyword = keyword,
            lastBlockedTime = System.currentTimeMillis()
        )
        _blockedOverlayText.value = "Contenu explicite intercepté par le service d'accessibilité TR-Shield : '$keyword'."
    }

    // Chat Logic (100% Real API)
    fun sendChatMessage(text: String) {
        if (text.isBlank()) return

        val userMsg = ChatMessage(
            id = UUID.randomUUID().toString(),
            isFromUser = true,
            text = text
        )
        val currentList = _chatMessages.value.toMutableList()
        currentList.add(userMsg)
        _chatMessages.value = currentList
        _isChatLoading.value = true

        viewModelScope.launch {
            try {
                val responseMsg = agnesClient.sendMessage(
                    userMessage = text,
                    preferredLanguage = _userProfile.value.preferredLanguage,
                    streakDays = _userProfile.value.streakDays,
                    chatHistory = currentList,
                    keyOverride = _userProfile.value.customAgnesApiKey
                )
                val updated = _chatMessages.value.toMutableList()
                updated.add(responseMsg)
                _chatMessages.value = updated
            } catch (e: Exception) {
                val errorMsg = ChatMessage(
                    id = UUID.randomUUID().toString(),
                    isFromUser = false,
                    text = "Erreur de connexion à l'API Agnes : ${e.localizedMessage ?: "Serveur injoignable"}.",
                    dynamicCardType = DynamicCardType.NONE
                )
                val updated = _chatMessages.value.toMutableList()
                updated.add(errorMsg)
                _chatMessages.value = updated
            } finally {
                _isChatLoading.value = false
            }
        }
    }

    fun updateCustomAgnesApiKey(key: String) {
        viewModelScope.launch {
            val updated = _userProfile.value.copy(customAgnesApiKey = key.trim())
            _userProfile.value = updated
            profileDao.insertOrUpdate(updated)
        }
    }

    fun updateEmergencyContact(name: String, phone: String) {
        viewModelScope.launch {
            val updated = _userProfile.value.copy(
                emergencyContactName = name.trim(),
                emergencyContactPhone = phone.trim()
            )
            _userProfile.value = updated
            profileDao.insertOrUpdate(updated)
        }
    }

    // Trigger Logging
    fun logTrigger(mood: String, stressLevel: Int, location: String, notes: String, resisted: Boolean) {
        viewModelScope.launch {
            triggerDao.insertTrigger(
                TriggerEntry(
                    mood = mood,
                    stressLevel = stressLevel,
                    location = location,
                    notes = notes,
                    resisted = resisted
                )
            )
            if (resisted) {
                val current = _userProfile.value
                val updated = current.copy(
                    streakDays = current.streakDays + 1,
                    bestStreakDays = maxOf(current.bestStreakDays, current.streakDays + 1)
                )
                _userProfile.value = updated
                profileDao.insertOrUpdate(updated)
            }
        }
    }

    fun resetStreakWithLearning() {
        viewModelScope.launch {
            val current = _userProfile.value
            val updated = current.copy(
                streakDays = 0,
                lastCheckInDate = System.currentTimeMillis()
            )
            _userProfile.value = updated
            profileDao.insertOrUpdate(updated)
        }
    }

    // Challenges
    fun completeChallenge(challenge: ChallengeItem) {
        viewModelScope.launch {
            challengeDao.updateChallenge(challenge.copy(isCompleted = true))
        }
    }

    // Forum
    fun addForumPost(alias: String, content: String, tag: String) {
        viewModelScope.launch {
            forumDao.insertPost(
                ForumPost(
                    authorAlias = if (alias.isNotBlank()) alias else "Guerrier_Anonyme",
                    daysClean = _userProfile.value.streakDays,
                    content = content,
                    tag = tag
                )
            )
        }
    }

    fun likePost(postId: Long) {
        viewModelScope.launch {
            forumDao.incrementLikes(postId)
        }
    }

    override fun onCleared() {
        try {
            getApplication<Application>().unregisterReceiver(keywordReceiver)
        } catch (_: Exception) {}
        super.onCleared()
    }
}
