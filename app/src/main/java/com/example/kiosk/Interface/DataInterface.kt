package com.example.kiosk

interface DataInterface {
    fun sendSignal(data:Int,category:Int)
}

interface changeFragment {
    fun signal(cost : Int, payment : String, receipt : Boolean, takeType : String)
}

interface ChangeActivitiy{
    fun signal(dummy:String)
}