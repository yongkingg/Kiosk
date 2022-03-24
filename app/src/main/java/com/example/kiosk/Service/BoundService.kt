package com.example.kiosk.Service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class BoundService : Service(){
    var binder = Binder()
    var basket : ArrayList<ArrayList<String>> = arrayListOf()
    inner class Binder : android.os.Binder() {
        fun getService(): BoundService {
            return this@BoundService
        }
    }
    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        var name = intent?.getStringExtra("name")
        var image = intent?.getStringExtra("Image")
        var cost = intent?.getStringExtra("cost")
        var count = intent?.getStringExtra("count")
        var temp = intent?.getStringExtra("temp")
        var size = intent?.getStringExtra("size")
        var receive = intent?.getStringExtra("receive")
        var chocolateTopping = intent?.getStringExtra("topping1")
        var creamTopping = intent?.getStringExtra("topping2")
        var pullTopping = intent?.getStringExtra("topping3")
        var shirupTopping = intent?.getStringExtra("topping4")
        basket.add(arrayListOf(name!!,image!!,cost!!,count!!,temp!!,size!!,receive!!,chocolateTopping!!,creamTopping!!,pullTopping!!,shirupTopping!!))
        Log.d("tag","$basket")

        return super.onStartCommand(intent, flags, startId)
    }

    fun deleteMenu(index : Int){
        basket[index] = ArrayList(0)
    }

    fun clearList() {
        for (index in 0 until basket.count()){
            basket.remove(ArrayList(0))
        }
    }
}