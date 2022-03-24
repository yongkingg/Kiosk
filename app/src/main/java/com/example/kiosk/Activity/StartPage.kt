package com.example.kiosk.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.drawerlayout.widget.DrawerLayout
import com.example.kiosk.ChangeActivitiy
import com.example.kiosk.DataInterface
import com.example.kiosk.R
import com.example.kiosk.Fragment.StartPageBody
import com.example.kiosk.Service.ForegroundService

class StartPage : AppCompatActivity(), ChangeActivitiy, DataInterface {
    lateinit var notification : Intent
    lateinit var userId : String
    lateinit var lang : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menubtn_fragment)
        supportFragmentManager.beginTransaction().replace(R.id.mainlayout, StartPageBody()).commit()
        userId = intent.getStringExtra("userId")!!
        lang = intent.getStringExtra("lang")!!
        initEvent()
    }


    override fun onDestroy() {
        super.onDestroy()
        notification = Intent(this,ForegroundService::class.java)
        stopService(notification)
    }

    fun initEvent() {
        var drawerLayout : DrawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        var drawerView : View = findViewById<View>(R.id.drawer)

        var basketBtn:Button? = findViewById<Button>(R.id.basketBtn)
        basketBtn!!.setOnClickListener{
            var intent = Intent(this, BasketPage::class.java)
            intent.putExtra("userId",userId)
            startActivity(intent)
        }

        var menuBtn: Button? = findViewById<Button>(R.id.menuBtn)
        menuBtn!!.setOnClickListener{
            drawerLayout.openDrawer(Gravity.LEFT)
        }

        var closeDrawerBtn: Button? = findViewById<Button>(R.id.closeDrawer)
        closeDrawerBtn!!.setOnClickListener{
            drawerLayout.closeDrawer(drawerView)
        }

        var beverageOrderBtn : LinearLayout? = findViewById(R.id.orderBeverage)
        beverageOrderBtn!!.setOnClickListener{
            var intent = Intent(this, BeverageOrderPage::class.java)
            intent.putExtra("userId",userId)
            intent.putExtra("lang",lang)
            startActivity(intent)
            drawerLayout.closeDrawer(drawerView)
        }
    }

    override fun signal(dummy:String) {
        var intent = Intent(this, BeverageOrderPage::class.java)
        intent.putExtra("userId",userId)
        intent.putExtra("lang",lang)
        startActivity(intent)
    }

    override fun sendSignal(data: Int, category: Int) {
        var intent = Intent(this, BeverageSetPage::class.java)
        intent.putExtra("menu","$data")
        intent.putExtra("category","$category")
        intent.putExtra("userId",userId)
        startActivity(intent)
    }
}