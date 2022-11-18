package com.biggestAsk.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.biggestAsk.ui.activity.MainActivity
import com.example.biggestAsk.R
import com.google.firebase.messaging.RemoteMessage

/**
 * notification utils
 */
object NotificationUtils {

    fun buildSimpleNotification(context: Context, message: RemoteMessage) {
        val data = message.notification
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, 1, intent, 0
        )

        val channelId = "111"

        val titleText = data?.title
        val contentText = data?.body
        val symActionChat = R.drawable.logo_setting_privacy_policy

        val myBitMap = BitmapFactory.decodeResource(
            context.resources,
            R.mipmap.ic_launcher
        )

        val nb = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder(context, channelId)
        } else {
            NotificationCompat.Builder(context)
        }

        nb.apply {
            setContentIntent(pendingIntent)
            setSmallIcon(symActionChat)
            setContentTitle(titleText)
            color = ContextCompat.getColor(
                context,
                R.color.blue_light
            )
            setContentText(contentText)
            myBitMap
            setLargeIcon(myBitMap)
            setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(contentText)
            )
            setGroup("GROUP_KEY")
            priority = NotificationCompat.PRIORITY_HIGH
            setAutoCancel(true)
        }

        val mNotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "channelName",
                NotificationManager.IMPORTANCE_HIGH
            )
            mNotificationManager?.createNotificationChannel(channel)
            channelId.let { nb.setChannelId(it) }
        }
        mNotificationManager?.notify(
            titleText.hashCode(),
            nb.build()
        )
    }

}
