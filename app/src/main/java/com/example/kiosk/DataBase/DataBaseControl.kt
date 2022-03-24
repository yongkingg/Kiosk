package com.example.kiosk.DataBase

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log

class DataBaseControl {
    fun read(database: SQLiteDatabase,table: String,column: ArrayList<String>,data: ArrayList<String>) : ArrayList<ArrayList<String>>{
        var sql = "SELECT * FROM '$table' WHERE ("
        for (index in 0 until column.count()) {
            sql += column[index] + " = '" + data[index] + "'"
            if (index != column.count() - 1 ) {
                sql += " and "
            }
        }
        sql += ")"
        var result : Cursor = database.rawQuery(sql, null)
        val dataList = ArrayList<ArrayList<String>>()
        while (result.moveToNext()) {
            if (table == "Info"){
                var row = arrayListOf<String>(result.getString(0),result.getString(1),result.getString(2),result.getString(3),result.getString(4),result.getString(5))
                dataList.add(row)
            } else if (table == "Account") {
                var row = arrayListOf<String>(result.getString(0),result.getString(1),result.getInt(2).toString())
                dataList.add(row)
            } else if (table == "History") {
                var row = arrayListOf<String>(result.getString(0),result.getString(1),result.getString(2),result.getString(3),result.getString(4),result.getString(5),result.getString(6),result.getString(7))
                dataList.add(row)
            }
            Log.d("t124124124ag","$dataList")
        }
        result.close()
        return dataList
    }

    fun create(database : SQLiteDatabase, table: String, column:ArrayList<String>,data:ArrayList<String>) {
        var sql = "INSERT INTO $table ("
        for (index in 0 until column.count()) {
            sql += "'" + column[index] + "'"
            if (index != column.count() - 1) {
                sql += ", "
            }
        }
        sql += ") VALUES ("
        for (index in 0 until column.count()){
            sql += "'" + data[index] + "'"
            if (index != column.count() - 1) {
                sql += ", "
            }
        }
        sql += ")"
        database.execSQL(sql)
    }

    fun update(database: SQLiteDatabase, table: String, column: ArrayList<String>, data: ArrayList<String>, conColumn : String, conData : String) {
        var sql = "UPDATE $table SET ("
        for (index in 0 until column.count()) {
            sql += "'" + column[index] + "'" + " = " + data[index]
            if (index != column.count() -1) {
                sql += ", "
            }
        }
        sql += ") VALUES ("
        for (index in 0 until column.count()) {
            sql += "'" + data[index] + "'" + " = " + data[index]
            if (index != column.count() -1) {
                sql += ", "
            }
        }
        sql += ") WHERE ($conColumn = $conData)"
        database.execSQL(sql)
    }

    fun delete(database: SQLiteDatabase, table: String, column: ArrayList<String>, data: ArrayList<String>) {
        var sql = "DELETE FROM '$table' WHERE ("
        for (index in 0 until column.count()) {
            sql += column[index] + " = " + data[index]
            if (index != column.count() - 1) {
                sql += ", "
            }
        }
        sql += ")"
        database.execSQL(sql)
    }
}