package com.example.kiosk.Preference

import android.content.Context
import android.content.SharedPreferences

class PreferenceUtil(context : Context) {
    private val prefs : SharedPreferences = context.getSharedPreferences("prefs_name",Context.MODE_PRIVATE)

    var myBrightMode : String
        get() = prefs.getString("brightnessMode","light")!!
        set(value) = prefs.edit().putString("brightnessMode",value).apply()

    var myLanguageMode : String
        get() = prefs.getString("languageMode","ko")!!
        set(value) = prefs.edit().putString("languageMode",value).apply()
}