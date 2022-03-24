package com.example.kiosk.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.kiosk.Fragment.UserPageBody
import com.example.kiosk.R

class UserPage : AppCompatActivity() {
    lateinit var userKey : String
    lateinit var lang : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.backbtn_fragment)

        var basketBtn: Button? = findViewById<Button>(R.id.basketBtn)
        basketBtn!!.visibility = View.INVISIBLE

        var headText : TextView = findViewById(R.id.textView)
        headText.setText("EDIYA")

        var backBtn : Button = findViewById(R.id.backBtn)
        backBtn.setOnClickListener {
            finish()
        }

        userKey = intent.getStringExtra("userKey")!!
        lang = intent.getStringExtra("lang")!!
        Log.d("tag",lang)
        var fragment = UserPageBody()
        var bundle = Bundle()
        bundle.putString("userKey",userKey)
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.mainlayout, fragment).commit()
    }

    fun goOrder() {
        var intent = Intent(this, StartPage::class.java)
        intent.putExtra("userId",userKey)
        intent.putExtra("lang",lang)
        startActivity(intent)
    }

    fun goOrderHistory() {
        var intent = Intent(this, OrderHistoryPage::class.java)
        intent.putExtra("userId",userKey)
        startActivity(intent)
    }
}