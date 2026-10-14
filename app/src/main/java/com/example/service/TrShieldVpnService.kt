package com.example.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.VpnService
import android.os.Build
import android.os.ParcelFileDescriptor
import android.util.Log

class TrShieldVpnService : VpnService() {

    private var vpnInterface: ParcelFileDescriptor? = null

    companion object {
        const val ACTION_START = "com.example.service.START_VPN"
        const val ACTION_STOP = "com.example.service.STOP_VPN"
        private const val NOTIFICATION_CHANNEL_ID = "tr_shield_vpn_channel"
        private const val NOTIFICATION_ID = 2261
        var isRunning: Boolean = false
            private set
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_STOP -> {
                stopVpn()
                stopSelf()
            }
            else -> {
                startVpn()
            }
        }
        return START_STICKY
    }

    private fun startVpn() {
        if (vpnInterface != null) return

        try {
            createNotificationChannel()
            val notification = buildForegroundNotification()
            startForeground(NOTIFICATION_ID, notification)

            val builder = Builder()
                .setSession("TR-Shield VPN")
                .addAddress("10.0.0.2", 32)
                .addDnsServer("1.1.1.3") // Cloudflare Family DNS (bloque le contenu adulte et malveillant nativement)
                .addDnsServer("1.0.0.3")
                .setBlocking(false)

            vpnInterface = builder.establish()
            isRunning = true
            Log.i("TrShieldVpnService", "Tunnel VPN TR-Shield local établi avec succès.")
        } catch (e: Exception) {
            Log.e("TrShieldVpnService", "Échec de configuration du VpnService", e)
            stopSelf()
        }
    }

    private fun stopVpn() {
        try {
            vpnInterface?.close()
            vpnInterface = null
            isRunning = false
            stopForeground(STOP_FOREGROUND_REMOVE)
            Log.i("TrShieldVpnService", "Tunnel VPN TR-Shield fermé.")
        } catch (e: Exception) {
            Log.e("TrShieldVpnService", "Erreur lors de la fermeture du VPN", e)
        }
    }

    override fun onDestroy() {
        stopVpn()
        super.onDestroy()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "Bouclier Réseau TR-Shield",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Protection DNS et filtrage actif"
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    private fun buildForegroundNotification(): Notification {
        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(this, NOTIFICATION_CHANNEL_ID)
        } else {
            @Suppress("DEPRECATION")
            Notification.Builder(this)
        }
        return builder
            .setContentTitle("TR-Shield — Protection Active")
            .setContentText("Filtrage DNS Cloudflare Family actif sur l'appareil.")
            .setSmallIcon(android.R.drawable.ic_lock_lock)
            .build()
    }
}
