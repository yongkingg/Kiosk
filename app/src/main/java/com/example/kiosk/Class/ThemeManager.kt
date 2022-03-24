package com.example.kiosk.Class

import android.os.Build
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatDelegate

object ThemeManager {
    enum class ThemeMode { LIGHT, DARK, DEFAULT }

    fun applyTheme(themeMode: ThemeMode) {
        when (themeMode) {
            ThemeMode.LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            ThemeMode.DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
                }
        }
    }
}
//
//object ThemeHelper {
//    public const val LIGHT_MODE = "light"
//    public const val DARK_MODE = "dark"
//    public const val DEFAULT_MODE = "default"
//
//    fun applyTheme(@NonNull themePref: String) {
//        when (themePref) {
//            LIGHT_MODE -> {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//            }
//
//            DARK_MODE -> {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//            }
//
//            else -> {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
//                } else {
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
//                }
//            }
//        }
//    }
//}