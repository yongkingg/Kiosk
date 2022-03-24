package com.example.kiosk.Activity

import android.app.Application
import com.example.kiosk.Preference.PreferenceUtil


class MyApplication : Application() {
    companion object {
        lateinit var prefs : PreferenceUtil
    }

    override fun onCreate() {
        prefs = PreferenceUtil(applicationContext)
        super.onCreate()
    }
}