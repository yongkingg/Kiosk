package com.example.kiosk.Fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.kiosk.R

class NullBasketPageBody: Fragment() {
    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        var view: View = inflater.inflate(R.layout.nullbasketpagebody, container, false)
        initEvent(view)
        return view
    }

    fun initEvent(view:View){
        var payBtn : Button? = view.findViewById<Button>(R.id.nullBasketPayBtn)
        payBtn!!.setOnClickListener{
            var popupView = getLayoutInflater().inflate(R.layout.popupwindow, null);
            var alertdialog = AlertDialog.Builder(context).create()
            var popupBackBtn = popupView.findViewById<Button>(R.id.backBtn)
            var popupText = popupView.findViewById<TextView>(R.id.popuptext)
            popupText.setText("장바구니에 메뉴를 담아주세요.")
            popupBackBtn!!.setOnClickListener {
                alertdialog.hide()
            }
            alertdialog.setView(popupView)
            alertdialog.show()
            alertdialog.window!!.setLayout(600, 400)
        }
    }
}

