package com.example.kiosk.Service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.kiosk.Activity.BasketPage
import com.example.kiosk.R
import java.util.*

class KeyService : Service() {
    var notiviction_id = 2
    lateinit var notificationManager: NotificationManager

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        var key = intent!!.getStringExtra("key")
        startNotification(key!!)
        return super.onStartCommand(intent, flags, startId)
    }

    fun startNotification(key: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannel()
            var builder = NotificationCompat.Builder(this, "channel")
                .setContentTitle("이디야 알림")
                .setContentText("본인 인증 번호는 ${key}입니다.")
                .setSmallIcon(R.drawable.ade)
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText("본인 인증 번호는 ${key}입니다.\n인증번호를 입력해 주세요.")
                )
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build()
            startForeground(notiviction_id, builder)
        }
    }
    fun createChannel() {
        var name = "channelName"
        var text = "12211525"
        var importance = NotificationManager.IMPORTANCE_DEFAULT
        var channel = NotificationChannel("channel", name, importance).apply {
            description = text
        }
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}