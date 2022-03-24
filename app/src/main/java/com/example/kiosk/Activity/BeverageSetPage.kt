package com.example.kiosk.Activity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kiosk.Fragment.BeverageSetPageBody
import com.example.kiosk.R
import com.example.kiosk.Service.BoundService
import com.example.kiosk.Service.ForegroundService

class BeverageSetPage : AppCompatActivity() {
    lateinit var myService: BoundService
    var mBound: Boolean = false
    val connection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, service: IBinder?) {
            var binder = service as BoundService.Binder
            myService = binder.getService()
            mBound = true
        }
        override fun onServiceDisconnected(service: ComponentName?) {
            mBound = false
        }
    }

    override fun onStart() {
        super.onStart()
        var intent = Intent(this, BoundService::class.java)
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.backbtn_fragment)
        var headText = findViewById<TextView>(R.id.textView)
        headText.setText("음료 주문")
        var fragment = BeverageSetPageBody()
        var bundle = Bundle()
        bundle.putString("menu", intent.getStringExtra("menu"))
        bundle.putString("category", intent.getStringExtra("category"))
        bundle.putString("lang",intent.getStringExtra("lang"))
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.mainlayout, fragment).commit()
        initEvent()
    }

    fun initEvent() {
        var backBtn: Button? = findViewById<Button>(R.id.backBtn)
        backBtn!!.setOnClickListener {
            finish()
        }
        var basketBtn: Button? = findViewById<Button>(R.id.basketBtn)
        basketBtn!!.visibility = View.INVISIBLE
    }

    fun makeNotification(name: String, totalCost: Int, beverageCount: Int) {
        Toast.makeText(applicationContext, "장바구니에 음료가 담겼습니다.", Toast.LENGTH_SHORT).show()
        var notification = Intent(this, ForegroundService::class.java)
        notification.putExtra("name", name)
        notification.putExtra("totalCost", "$totalCost")
        notification.putExtra("count", "$beverageCount")
        startForegroundService(notification)
    }

    fun putData(name: String,Image: String, cost: Int, count: Int, temp: String, size: String, receive: String, topping: ArrayList<Int>) {
        var intent = Intent(this, BoundService::class.java)
        intent.putExtra("name",name)
        intent.putExtra("Image",Image.toString())
        intent.putExtra("cost", cost.toString())
        intent.putExtra("count",count.toString())
        intent.putExtra("temp",temp)
        intent.putExtra("size",size)
        intent.putExtra("receive",receive)
        intent.putExtra("topping1",topping[0].toString())
        intent.putExtra("topping2",topping[1].toString())
        intent.putExtra("topping3",topping[2].toString())
        intent.putExtra("topping4",topping[3].toString())
        startService(intent)
        finish()
    }
}
