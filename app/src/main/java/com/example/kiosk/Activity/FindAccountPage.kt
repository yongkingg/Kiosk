package com.example.kiosk.Activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kiosk.Fragment.IdFindBody
import com.example.kiosk.Fragment.PWFindBody
import com.example.kiosk.R
import com.example.kiosk.Service.KeyService

class FindAccountPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nullbackground)
        var key = intent.getStringExtra("key")
        if (key == "id") {
            supportFragmentManager.beginTransaction().replace(R.id.background, IdFindBody()).commit()

        } else if (key == "password") {
            supportFragmentManager.beginTransaction().replace(R.id.background, PWFindBody()).commit()
        }
    }

    fun pwFind(random : Int) {
        var notification = Intent(this, KeyService::class.java)
        notification.putExtra("key","$random")
        startForegroundService(notification)
    }

    fun hideNoti() {
        var notification = Intent(this, KeyService::class.java)
        stopService(notification)
    }

}