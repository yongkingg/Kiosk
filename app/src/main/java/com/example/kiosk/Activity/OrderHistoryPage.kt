package com.example.kiosk.Activity

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.kiosk.Fragment.OrderHistoryPageBody
import com.example.kiosk.R

class OrderHistoryPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.backbtn_fragment)

        var basketBtn: Button? = findViewById<Button>(R.id.basketBtn)
        basketBtn!!.visibility = View.INVISIBLE
        var headText = findViewById<TextView>(R.id.textView)
        headText.setText(getString(R.string.history))

        var fragment = OrderHistoryPageBody()
        var bundle = Bundle()
        bundle.putString("userId",intent.getStringExtra("userId"))
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.mainlayout,fragment).commit()

        var backBtn : Button? = findViewById(R.id.backBtn)
        backBtn!!.setOnClickListener {
            finish()
        }
    }
}

