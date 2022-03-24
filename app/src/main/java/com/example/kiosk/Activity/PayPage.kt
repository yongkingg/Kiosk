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
import androidx.appcompat.app.AppCompatActivity
import com.example.kiosk.DataBase.DataBase
import com.example.kiosk.DataBase.DataBaseControl
import com.example.kiosk.Fragment.EndPageBody
import com.example.kiosk.Fragment.PayPageBody
import com.example.kiosk.R
import com.example.kiosk.Service.BoundService
import com.example.kiosk.changeFragment
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PayPage : AppCompatActivity(), changeFragment {
    lateinit var userId: String
    var basket: ArrayList<ArrayList<String>> = arrayListOf()
    var date = SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.getDefault()).format(Date(System.currentTimeMillis()))

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.backbtn_fragment)

        var headText = findViewById<TextView>(R.id.textView)
        headText.setText("결제")

        userId = intent.getStringExtra("userId")!!

        var backBtn: Button? = findViewById<Button>(R.id.backBtn)
        backBtn!!.visibility = View.INVISIBLE

        var basketBtn: Button? = findViewById<Button>(R.id.basketBtn)
        basketBtn!!.visibility = View.INVISIBLE

        var fragment = PayPageBody()
        var bundle = Bundle()
        var basketCount = intent.getStringExtra("count")
        for (index in 0 until basketCount!!.toInt()) {
            var getMenu = intent.getStringArrayListExtra("menu" + "$index")
            bundle.putStringArrayList("menu" + "$index", getMenu!!)
            basket.add(getMenu)
        }
        bundle.putString("count", basketCount)
        bundle.putString("userId",userId)
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.mainlayout, fragment).commit()
        initEvent()
    }

    override fun signal(cost: Int, payment: String, receipt: Boolean, takeType: String) {
        var fragment = EndPageBody()
        var bundle = Bundle()
        bundle.putInt("cost", cost)
        bundle.putString("payment", payment)
        bundle.putString("takeType", takeType)
        bundle.putString("userId", userId)
        if (receipt == true) {
            bundle.putString("receipt", "출력")
        } else {
            bundle.putString("receipt", "미출력")
        }
        fragment.arguments = bundle
        val db = DataBase(this, "EDIYA.db", null, 1)
        val readableDB = db.readableDatabase
        val writeableDB = db.writableDatabase
        var dbControl = DataBaseControl()

        for (index in 0 until basket.count()) {
            dbControl.create(
                writeableDB,
                "History",
                arrayListOf<String>("id", "orderMenu", "count","temperature","topping","size","date","totalCost"),
                arrayListOf<String>("$userId", basket[index][0], basket[index][3],basket[index][4],basket[index][7]+basket[index][8]+basket[index][9]+basket[index][10],basket[index][5],date, basket[index][2])
            )
        }

        supportFragmentManager.beginTransaction().replace(R.id.mainlayout, fragment).commit()
    }

    fun initEvent() {
        var backBtn: Button? = findViewById<Button>(R.id.backBtn)
        backBtn!!.setOnClickListener {
            finish()
        }
        var intent = Intent(this, BoundService::class.java)
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    fun clearBasket() {
        var intent = Intent(this, BoundService::class.java)
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
        myService.basket.clear()
    }
}