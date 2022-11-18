package com.biggestAsk.notification

import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.biggestAsk.util.Constants
import com.biggestAsk.util.NotificationUtils
import com.biggestAsk.util.PreferenceProvider
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * Firebase notification class
 */
class TheBiggestAskMessagingService : FirebaseMessagingService() {
    private var broadcaster: LocalBroadcastManager? = null

    override fun onCreate() {
        super.onCreate()
        broadcaster = LocalBroadcastManager.getInstance(this)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        try {
            if (message.notification?.title!!.isNotEmpty()) {
                makeClNotification(message)
            }
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    private fun makeClNotification(message: RemoteMessage) {
        NotificationUtils.buildSimpleNotification(this, message)
    }

    override fun onNewToken(token: String) {
        PreferenceProvider(this).setValue(Constants.NOTIFICATION_TOKEN, token)
    }
}
