package com.example.kiosk.DataBase

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBase(context: Context?, name:String, factory: SQLiteDatabase.CursorFactory?, version : Int)
    : SQLiteOpenHelper(context,name,factory, version){
    override fun onCreate(dataBase: SQLiteDatabase?) {
        var accountTable = "CREATE TABLE IF NOT EXISTS Account(id TEXT,pw TEXT, seq INTEGER PRIMARY KEY AUTOINCREMENT)"
        dataBase!!.execSQL(accountTable)

        var infoTable = "CREATE TABLE IF NOT EXISTS Info(id TEXT,orderHistory TEXT, grade TEXT, email TEXT, name TEXT, number TEXT)"
        dataBase!!.execSQL(infoTable)

        var historyTable = "CREATE TABLE IF NOT EXISTS History(id TEXT, orderMenu TEXT, count TEXT, temperature TEXT, topping TEXT, size TEXT, date TEXT, totalCost TEXT, seq INTEGER PRIMARY KEY AUTOINCREMENT)"
        dataBase!!.execSQL(historyTable)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
    }
}