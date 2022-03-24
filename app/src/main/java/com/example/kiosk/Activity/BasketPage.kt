package com.example.kiosk.Activity

import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.kiosk.Fragment.BasketPageBody
import com.example.kiosk.Fragment.NullBasketPageBody
import com.example.kiosk.R
import com.example.kiosk.Service.BoundService

class BasketPage : AppCompatActivity() {
    lateinit var myService: BoundService
    lateinit var userId : String
    var mBound: Boolean = false
    val connection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, service: IBinder?) {
            var binder = service as BoundService.Binder
            myService = binder.getService()
            var basket = myService.basket
            var basketCount = basket.count()
            var fragment = BasketPageBody()
            var bundle = Bundle()
            var booleanNum = 0
            for(index in 0 until basketCount) {
                if (basket[index] == arrayOf(0)) {
                    booleanNum += 1
                }
            }
            if (booleanNum == basketCount || basketCount == null || basketCount == 0) {
                supportFragmentManager.beginTransaction().replace(R.id.mainlayout,NullBasketPageBody()).commit()
            } else {
                for (index in 0 until basketCount){
                    bundle.putStringArrayList("menu" + "$index",basket[index])
                }
                bundle.putString("basketCount",basketCount.toString())
                bundle.putString("userId",userId)
                fragment.arguments = bundle
                supportFragmentManager.beginTransaction().replace(R.id.mainlayout,fragment).commit()
            }
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

        var basketBtn:Button? = findViewById<Button>(R.id.basketBtn)
        basketBtn!!.visibility = View.INVISIBLE

        userId = intent.getStringExtra("userId")!!

        var headText = findViewById<TextView>(R.id.textView)
        headText.setText("장바구니")
        initEvent()
    }

    fun deleteMenu(index : Int) {
        myService.deleteMenu(index)
    }

    fun clearList() {
        myService.clearList()
    }

    fun initEvent(){
        var backBtn: Button? = findViewById<Button>(R.id.backBtn)
        backBtn!!.setOnClickListener{
            clearList()
            finish()
        }
    }
}

