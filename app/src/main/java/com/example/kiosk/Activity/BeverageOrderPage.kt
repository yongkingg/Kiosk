package com.example.kiosk.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.kiosk.DataInterface
import com.example.kiosk.Fragment.BeverageOrderPageBody
import com.example.kiosk.R

class BeverageOrderPage : AppCompatActivity(), DataInterface {
    lateinit var userId : String
    lateinit var lang : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.backbtn_fragment)
        lang = intent.getStringExtra("lang")!!
        Log.d("tag",lang)
        var fragment = BeverageOrderPageBody()
        var bundle = Bundle()
        bundle.putString("lang",lang)
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.mainlayout, fragment).commit()
        var headText = findViewById<TextView>(R.id.textView)
        headText.setText(R.string.beverageOrder)
        userId = intent.getStringExtra("userId")!!
        initEvent()
    }

    fun initEvent() {
        var backBtn:Button? = findViewById<Button>(R.id.backBtn)
        backBtn!!.setOnClickListener{
            finish()
        }
        var basketBtn:Button? = findViewById<Button>(R.id.basketBtn)
        basketBtn!!.setOnClickListener{
            var intent = Intent(this, BasketPage::class.java)
            intent.putExtra("userId",userId)
            startActivity(intent)
        }
    }
    override fun sendSignal(index:Int,category:Int) {
        var intent = Intent(this, BeverageSetPage::class.java)
        intent.putExtra("menu","$index")
        intent.putExtra("category","$category")
        intent.putExtra("userId",userId)
        startActivity(intent)
    }
    fun beverageSet(index: Int, category : String, lang : String) {
        var intent = Intent(this,BeverageSetPage::class.java)
        intent.putExtra("menu","$index")
        intent.putExtra("category",category)
        intent.putExtra("lang",lang)
        startActivity(intent)
    }
}
