package com.example.kiosk.Fragment

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.kiosk.Activity.PayPage
import com.example.kiosk.Activity.StartPage
import com.example.kiosk.Activity.UserPage
import com.example.kiosk.R
import com.example.kiosk.Service.BoundService

class EndPageBody: Fragment() {
    lateinit var userId : String

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        var view : View =  inflater.inflate(R.layout.endpagebody,container,false)

        var cost = arguments?.getInt("cost")
        var payment = arguments?.getString("payment")
        var receipt = arguments?.getString("receipt")
        var takeType = arguments?.getString("takeType")
        userId = arguments?.getString("userId")!!

        fetchUi(view,cost!!,payment!!,receipt!!,takeType!!)
        initEvent(view)
        return view
    }

    fun fetchUi(view:View,cost:Int,payment:String,receipt:String,takeType:String){
        var costView = view.findViewById<TextView>(R.id.totalCost)
        costView.setText(cost.toString() + "원")
        var paymentValue = view.findViewById<TextView>(R.id.payment)
        paymentValue.setText(payment)
        var isTakeout = view.findViewById<TextView>(R.id.isTakeOut)
        isTakeout.setText(takeType)
        var isReceipt = view.findViewById<TextView>(R.id.isReceipt)
        isReceipt.setText(receipt)
    }
    fun initEvent(view:View){
        var backBtn: Button? = view.findViewById<Button>(R.id.endPayBtn)
        backBtn!!.setOnClickListener{
            var intent = Intent(context,UserPage::class.java)
            var payPage = context as PayPage
            payPage.clearBasket()
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.putExtra("userKey",userId)
            intent.putExtra("lang","ko")
            startActivity(intent)
        }
    }
}


