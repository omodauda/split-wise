package com.omodauda.splitwise.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.omodauda.splitwise.MainActivity
import com.omodauda.splitwise.R
import com.omodauda.splitwise.SplitWiseApplication
import com.omodauda.splitwise.data.network.model.UpdateFcmTokenRequest
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch

class AppMessagingService: FirebaseMessagingService() {
    @OptIn(DelicateCoroutinesApi::class)
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        val appContainer = (application as SplitWiseApplication).appContainer
        val repo = appContainer.authRepository
        val authPreference = appContainer.authPreference

        kotlinx.coroutines.GlobalScope.launch {
            if (authPreference.getAccessTokenSync() != null) {
                repo.updateFcmToken(UpdateFcmTokenRequest(token))
            }
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        message.notification?.let {
            showNotification(it.title, it.body)
        }
    }

    private fun showNotification(title: String?, message: String?) {
        val channelId = "default_notification_channel"
        val notificationManager = getSystemService(NotificationManager::class.java)

        // 1. Create the Notification Channel (Required for Android 8.0+)
        val channel = NotificationChannel(
            channelId,
            "Default Notifications",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)

        // 2. Create an Intent to open the app when the notification is clicked
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        // 3. Build the Notification
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true) // Removes notification when clicked
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // 4. Show the notification
        // Use a unique ID (like current time) so multiple notifications don't overwrite each other
        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }
}