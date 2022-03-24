package com.example.kiosk.Service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.kiosk.Activity.BasketPage
import com.example.kiosk.R

class ForegroundService: Service() {
    var beverageCost : Int = 0
    var menuCount : Int = 0
    var notiviction_id = 1
    lateinit var notificationManager: NotificationManager

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        var name = intent?.getStringExtra("name")
        var cost = intent?.getStringExtra("totalCost")
        var count = intent?.getStringExtra("count")
        beverageCost += cost!!.toInt()
        menuCount += count!!.toInt()
        startNotification(name!!)
        return super.onStartCommand(intent, flags, startId)
    }

    fun startNotification(name : String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannel()
            var intent = Intent(this,BasketPage::class.java)
            var contentIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)

            var builder = NotificationCompat.Builder(this, "channel")
                .setContentTitle("이디야 알림")
                .setContentText("장바구니에 ${name}가 담겼습니다")
                .setSmallIcon(R.drawable.ade)
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText("장바구니에 ${name}가 담겼습니다\n총합 금액 : $beverageCost       음료 개수 : $menuCount")
                )
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(contentIntent)
                .build()
            startForeground(notiviction_id, builder)
        }
    }

    fun createChannel() {
        var name = "channelname"
        var text = "42142636346"
        var importance = NotificationManager.IMPORTANCE_DEFAULT
        var channel = NotificationChannel("channel", name, importance).apply {
            description = text
        }
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}