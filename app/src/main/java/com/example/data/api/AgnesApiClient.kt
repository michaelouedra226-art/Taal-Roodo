package com.example.data.api

import android.util.Log
import com.example.BuildConfig
import com.example.data.model.ChatMessage
import com.example.data.model.DynamicCardType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.UUID
import java.util.concurrent.TimeUnit

class AgnesApiClient(
    private val apiKey: String? = null
) {
    private val client = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    suspend fun sendMessage(
        userMessage: String,
        preferredLanguage: String,
        streakDays: Int,
        chatHistory: List<ChatMessage>,
        keyOverride: String? = null
    ): ChatMessage = withContext(Dispatchers.IO) {
        val trimmedKey = keyOverride?.trim()?.takeIf { it.isNotBlank() }
            ?: apiKey?.trim()?.takeIf { it.isNotBlank() }
            ?: BuildConfig.AGNES_API_KEY.trim().takeIf { it.isNotBlank() }

        if (trimmedKey.isNullOrEmpty() || trimmedKey == "YOUR_AGNES_API_KEY" || trimmedKey == "DEFAULT_KEY") {
            return@withContext ChatMessage(
                id = UUID.randomUUID().toString(),
                isFromUser = false,
                text = "Clé API Agnes non configurée. Veuillez renseigner votre clé AGNES_API_KEY (via l'icône 🔑 en haut à droite ou le panneau Secrets) pour vous connecter directement à l'API officielle https://apihub.agnes-ai.com/v1.",
                dynamicCardType = DynamicCardType.NONE
            )
        }

        try {
            val apiResponse = callAgnesApi(trimmedKey, userMessage, preferredLanguage, streakDays, chatHistory)
            parseApiResponse(apiResponse)
        } catch (e: Exception) {
            Log.e("AgnesApiClient", "Erreur lors de l'appel à l'API Agnes", e)
            ChatMessage(
                id = UUID.randomUUID().toString(),
                isFromUser = false,
                text = "Erreur de communication avec l'API Agnes (${e.javaClass.simpleName}) : ${e.localizedMessage ?: "Impossible de joindre le serveur"}. Vérifiez votre connexion et la validité de votre clé API.",
                dynamicCardType = DynamicCardType.NONE
            )
        }
    }

    private fun callAgnesApi(
        key: String,
        userMessage: String,
        lang: String,
        streakDays: Int,
        chatHistory: List<ChatMessage>
    ): String {
        val url = "https://apihub.agnes-ai.com/v1/chat/completions"

        val systemPrompt = """
            Tu es Agnes, la conseillère sage et fraternelle de Taal Roodo (reconstruction personnelle et discipline numérique au Burkina Faso).
            L'utilisateur a un streak actuel de $streakDays jours de victoire. Langue préférée: $lang.
            Tu parles avec empathie, fermeté bienveillante et sans aucun jugement, mêlant français et proverbes/expressions en mooré (ex: 'Ned yaa a menga ninsaal', 'Wẽnnaam kõo fãa a pãng', 'Bi yaa yĩmde').
            Si l'utilisateur exprime du stress ou une envie urgente, propose une redirection physique ou de la respiration guidée.
            Tu peux inclure l'une de ces balises pour déclencher un widget d'interface dynamique (GenUI):
            - [GENUI:BREATHING] pour la cohérence cardiaque
            - [GENUI:PROVERB] pour afficher une carte de sagesse traditionnelle
            - [GENUI:CHALLENGE] pour lancer un micro-défi physique immédiat
            - [GENUI:FORTRESS] pour suggérer le verrouillage forteresse
        """.trimIndent()

        val messagesArray = JSONArray().apply {
            put(JSONObject().apply {
                put("role", "system")
                put("content", systemPrompt)
            })

            val recentHistory = chatHistory.takeLast(4)
            for (msg in recentHistory) {
                put(JSONObject().apply {
                    put("role", if (msg.isFromUser) "user" else "assistant")
                    put("content", msg.text)
                })
            }

            put(JSONObject().apply {
                put("role", "user")
                put("content", userMessage)
            })
        }

        val requestBodyJson = JSONObject().apply {
            put("model", "agnes-3.0-flash")
            put("messages", messagesArray)
            put("temperature", 0.7)
            put("max_tokens", 450)
        }

        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $key")
            .addHeader("Content-Type", "application/json")
            .post(requestBodyJson.toString().toRequestBody(jsonMediaType))
            .build()

        client.newCall(request).execute().use { response ->
            val body = response.body?.string() ?: ""
            if (!response.isSuccessful) {
                throw IOException("HTTP ${response.code} ${response.message} : $body")
            }
            val rootJson = JSONObject(body)
            val choices = rootJson.optJSONArray("choices")
            if (choices != null && choices.length() > 0) {
                val messageObj = choices.getJSONObject(0).optJSONObject("message")
                val content = messageObj?.optString("content")
                if (!content.isNullOrBlank()) {
                    return content
                }
            }
            throw IOException("Réponse invalide reçue de l'API Agnes : $body")
        }
    }

    private fun parseApiResponse(rawContent: String): ChatMessage {
        var cleanText = rawContent
        var cardType = DynamicCardType.NONE

        when {
            rawContent.contains("[GENUI:BREATHING]") -> {
                cardType = DynamicCardType.BREATHING
                cleanText = cleanText.replace("[GENUI:BREATHING]", "").trim()
            }
            rawContent.contains("[GENUI:PROVERB]") -> {
                cardType = DynamicCardType.MOORE_PROVERB
                cleanText = cleanText.replace("[GENUI:PROVERB]", "").trim()
            }
            rawContent.contains("[GENUI:CHALLENGE]") -> {
                cardType = DynamicCardType.PHYSICAL_CHALLENGE
                cleanText = cleanText.replace("[GENUI:CHALLENGE]", "").trim()
            }
            rawContent.contains("[GENUI:FORTRESS]") -> {
                cardType = DynamicCardType.FORTRESS_PROPOSAL
                cleanText = cleanText.replace("[GENUI:FORTRESS]", "").trim()
            }
        }

        return ChatMessage(
            id = UUID.randomUUID().toString(),
            isFromUser = false,
            text = cleanText,
            dynamicCardType = cardType,
            dynamicPayload = null
        )
    }
}
