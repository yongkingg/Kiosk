package com.example.kiosk.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.kiosk.Activity.UserPage
import com.example.kiosk.DataBase.DataBase
import com.example.kiosk.DataBase.DataBaseControl
import com.example.kiosk.R

class UserPageBody : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view : View = inflater.inflate(R.layout.userpagebody,container,false)

        var getValue = arguments?.getString("userKey")

        val db = DataBase(context,"EDIYA.db",null,1)
        val readableDB = db.readableDatabase
        val writeableDB = db.writableDatabase
        var dbControl = DataBaseControl()
        var nameValue = dbControl.read(writeableDB,"Info", arrayListOf("id"), arrayListOf("$getValue"))
        var userName : TextView = view.findViewById(R.id.userName)
        userName.setText("기본값" + getString(R.string.grade))

        var imageSpace : ImageView = view.findViewById(R.id.imageSpace)
        var gradeSpace : TextView = view.findViewById(R.id.gradeText)
        var orderCount = dbControl.read(writeableDB,"History", arrayListOf("id"), arrayListOf("$getValue"))
        if (orderCount.count() >= 2) {
            imageSpace.setImageResource(R.drawable.redheart)
            gradeSpace.setText("RED")
        }
        initEvent(view)
        return view
    }

    fun initEvent(view: View) {
        var userPage = context as UserPage
        var orderBtn : Button? = view.findViewById(R.id.orderBtn)
        orderBtn!!.setOnClickListener {
            userPage.goOrder()
        }
        var orderHistory : Button? = view.findViewById(R.id.historyBtn)
        orderHistory!!.setOnClickListener {
            userPage.goOrderHistory()
        }
    }
}