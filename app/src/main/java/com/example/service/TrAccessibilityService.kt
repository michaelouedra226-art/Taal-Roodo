package com.example.service

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class TrAccessibilityService : AccessibilityService() {

    companion object {
        var isEnabled: Boolean = false
            private set

        const val ACTION_KEYWORD_DETECTED = "com.example.service.KEYWORD_DETECTED"
        const val EXTRA_KEYWORD = "extra_keyword"

        private val FORBIDDEN_KEYWORDS = listOf(
            "porno", "porn", "xxx", "xvideo", "nude", "hentai", "sex", "erotic",
            "yãagre", "bõorga"
        )
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        isEnabled = true
        Log.i("TrAccessibilityService", "Service d'Accessibilité TR-Shield connecté.")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return

        val rootNode = rootInActiveWindow ?: return
        scanNodeHierarchy(rootNode)
    }

    private fun scanNodeHierarchy(node: AccessibilityNodeInfo) {
        val text = node.text?.toString()?.lowercase()
        val contentDesc = node.contentDescription?.toString()?.lowercase()

        for (keyword in FORBIDDEN_KEYWORDS) {
            if ((text != null && text.contains(keyword)) ||
                (contentDesc != null && contentDesc.contains(keyword))
            ) {
                Log.w("TrAccessibilityService", "Contenu explicite intercepté: $keyword")
                notifyKeywordDetected(keyword)
                break
            }
        }

        for (i in 0 until node.childCount) {
            val child = node.getChild(i) ?: continue
            scanNodeHierarchy(child)
        }
    }

    private fun notifyKeywordDetected(keyword: String) {
        val intent = Intent(ACTION_KEYWORD_DETECTED).apply {
            setPackage(packageName)
            putExtra(EXTRA_KEYWORD, keyword)
        }
        sendBroadcast(intent)
    }

    override fun onInterrupt() {
        isEnabled = false
        Log.i("TrAccessibilityService", "Service d'Accessibilité TR-Shield interrompu.")
    }

    override fun onDestroy() {
        isEnabled = false
        super.onDestroy()
    }
}
