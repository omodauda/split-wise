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
import com.omodauda.splitwise.data.local.IAuthPreference
import com.omodauda.splitwise.data.network.model.UpdateFcmTokenRequest
import com.omodauda.splitwise.data.repository.AuthRepository
import com.omodauda.splitwise.data.repository.BillsRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class FcmAction(val key: String) {
    NEW_BILL("NEW_BILL"),
    BILL_SETTLEMENT("BILL_SETTLEMENT"),
    PAYMENT_RECEIVED("PAYMENT_RECEIVED"),
    UNKNOWN("UNKNOWN");

    companion object {
        fun fromString(key: String?): FcmAction {
            return entries.find { it.key == key } ?: UNKNOWN
        }
    }
}
@AndroidEntryPoint
class AppMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var authRepository: AuthRepository

    @Inject
    lateinit var authPreference: IAuthPreference

    @Inject
    lateinit var billsRepository: BillsRepository

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        serviceScope.launch {
            if (authPreference.getAccessTokenSync() != null) {
                authRepository.updateFcmToken(UpdateFcmTokenRequest(token))
            }
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val action = FcmAction.fromString(message.data["action"])

        serviceScope.launch {
            when (action) {
                FcmAction.NEW_BILL -> billsRepository.triggerRefreshOwingBills()
                FcmAction.BILL_SETTLEMENT -> billsRepository.triggerRefreshOwingBills()
                FcmAction.PAYMENT_RECEIVED -> {
                    billsRepository.triggerRefreshOwedBills()
                    billsRepository.triggerRefreshPaymentPendingConfirmation()
                }
                FcmAction.UNKNOWN -> {}
            }
        }
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
        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }
}
