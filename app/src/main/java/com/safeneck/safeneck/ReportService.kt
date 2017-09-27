package com.safeneck.safeneck

import android.app.IntentService
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.safeneck.safeneck.Activity.MainActivity

/**
 * Created by eka on 2017. 9. 27..
 */
class ReportService : IntentService("") {
    override fun onHandleIntent(intent: Intent?) {
        val intent = Intent(applicationContext, MainActivity::class.java)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = Notification.Builder(applicationContext)
                .setContentTitle("Safe Neck")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("리포트를 확인할 시간이에요!")
                .setContentText("오늘 하루 자세에 대해서 되돌아봐요")
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(PendingIntent.getActivity(applicationContext, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT))
                .build()
        notificationManager.notify(1, notification)

    }
}