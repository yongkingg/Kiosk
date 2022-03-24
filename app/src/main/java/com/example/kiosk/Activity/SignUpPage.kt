package com.example.kiosk.Activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kiosk.Fragment.SignUpPageBody
import com.example.kiosk.R

class SignUpPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nullbackground)
        supportFragmentManager.beginTransaction().replace(R.id.background, SignUpPageBody()).commit()
    }

    fun exitActivity() {
        finish()
    }
}