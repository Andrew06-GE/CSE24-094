package com.gabsstudentstay.gabsstudentstay.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.gabsstudentstay.gabsstudentstay.R
import com.gabsstudentstay.gabsstudentstay.ui.auth.home.HomeActivity

object NotificationHelper {

    private const val CHANNEL_ID   = "listing_alerts"
    private const val CHANNEL_NAME = "Listing Alerts"
    private const val CHANNEL_DESC = "Notifies you when a listing matches your preferences"

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply { description = CHANNEL_DESC }

            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    fun sendListingMatchNotification(
        context: Context,
        listingTitle: String,
        location: String,
        price: Double,
        notificationId: Int = System.currentTimeMillis().toInt()
    ) {
        val intent = Intent(context, HomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("🏠 New Listing Match!")
            .setContentText("$listingTitle in $location — BWP ${price.toInt()}/mo")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("A new listing matches your saved preferences:\n\n" +
                            "\"$listingTitle\"\n📍 $location\n💰 BWP ${price.toInt()}/month")
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(notificationId, notification)
    }
}